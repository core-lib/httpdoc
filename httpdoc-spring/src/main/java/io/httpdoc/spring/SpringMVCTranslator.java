package io.httpdoc.spring;

import io.httpdoc.core.*;
import io.httpdoc.core.annotation.*;
import io.httpdoc.core.annotation.Package;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.Interpretation;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import io.httpdoc.core.interpretation.Note;
import io.httpdoc.core.reflection.ParameterizedTypeImpl;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringMVCTranslator implements Translator {

    private static final Collection<Class<?>> IGNORED_PARAMETER_TYPES = Arrays.asList(
            Void.TYPE,
            Void.class,
            Class.class,
            ServletContext.class,
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class,
            HttpHeaders.class,
            BindingResult.class,
            UriComponentsBuilder.class
    );

    private final Pattern pattern = Pattern.compile("\\{([^{}]+?)(:([^{}]+?))?}");
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());

        Map<RequestMappingInfo, HandlerMethod> map = new LinkedHashMap<>();
        Container container = translation.getContainer();
        Collection<ApplicationContext> applications = container.get(ApplicationContext.class).values();
        for (ApplicationContext application : applications) {
            Collection<RequestMappingHandlerMapping> mappings = application.getBeansOfType(RequestMappingHandlerMapping.class).values();
            for (RequestMappingHandlerMapping mapping : mappings) map.putAll(mapping.getHandlerMethods());
            Collection<RequestMappingInfoHandlerMapping> infos = application.getBeansOfType(RequestMappingInfoHandlerMapping.class).values();
            for (RequestMappingInfoHandlerMapping info : infos) map.putAll(info.getHandlerMethods());
        }

        Map<Class<?>, Controller> controllers = new LinkedHashMap<>();
        Interpreter interpreter = translation.getInterpreter();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo mapping = entry.getKey();
            HandlerMethod handler = entry.getValue();
            Class<?> clazz = handler.getBeanType();
            Method method = handler.getMethod();
            // 如果Controller或方法上有Ignore注解 则忽略
            if (clazz.isAnnotationPresent(Ignore.class) || method.isAnnotationPresent(Ignore.class)) continue;

            // Controller 解析
            Controller controller = controllers.get(clazz);
            if (controller == null) {
                controller = new Controller();
                controller.setPkg(clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName());
                controller.setName(clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName());
                Interpretation interpretation = interpreter.interpret(clazz);
                controller.setDescription(interpretation == null ? null : interpretation.getContent());
                Tag tag = clazz.getAnnotation(Tag.class);
                if (tag == null || tag.value().length == 0 || !tag.override()) controller.getTags().add(controller.getName());
                if (tag != null) controller.getTags().addAll(Arrays.asList(tag.value()));
                controllers.put(clazz, controller);
            }

            // 方法解析 只取第一个
            String expression = mapping.getPatternsCondition().getPatterns().iterator().next();
            Operation operation = new Operation();
            // 重定义方法名
            operation.setName(method.isAnnotationPresent(Name.class) ? method.getAnnotation(Name.class).value() : method.getName());
            operation.setPath(normalize(expression));
            // produces
            Set<MediaTypeExpression> produces = mapping.getProducesCondition().getExpressions();
            for (MediaTypeExpression produce : produces) operation.getProduces().add(produce.getMediaType().toString());
            Set<MediaTypeExpression> consumes = mapping.getConsumesCondition().getExpressions();
            for (MediaTypeExpression consume : consumes) operation.getConsumes().add(consume.getMediaType().toString());
            // 请求方法 只取第一个 缺省情况下采用POST 更加通用
            Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();
            operation.setMethod(methods.isEmpty() ? RequestMethod.POST.name() : methods.iterator().next().name());

            MethodInterpretation interpretation = interpreter.interpret(method);
            operation.setDescription(interpretation == null ? null : interpretation.getContent());

            Tag tag = method.getAnnotation(Tag.class);
            if (tag == null || tag.value().length == 0 || !tag.override()) operation.getTags().addAll(controller.getTags());
            if (tag != null) operation.getTags().addAll(Arrays.asList(tag.value()));

            // 返回值解析
            Supplier supplier = translation.getSupplier();
            Result result = new Result();
            Type type = getReturnType(handler);
            Schema schema = Schema.valueOf(type, supplier, interpreter);
            result.setType(schema);
            result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
            operation.setResult(result);

            // 参数解析
            Map<String, String> descriptions = new HashMap<>();
            Note[] notes = interpretation != null ? interpretation.getParamNotes() : null;
            for (int i = 0; notes != null && i < notes.length; i++) descriptions.put(notes[i].getName(), notes[i].getText());
            String[] names = discoverer.getParameterNames(method);

            MethodParameter[] params = handler.getMethodParameters();
            HttpMethod httpMethod = HttpMethod.constantOf(operation.getMethod());
            for (int i = 0; i < params.length; i++) {
                MethodParameter param = params[i];
                // 忽略
                if (param.hasParameterAnnotation(Ignore.class)) continue;
                Type paramType = param.getGenericParameterType();
                if (paramType instanceof Class<?> && IGNORED_PARAMETER_TYPES.contains(paramType)) continue;

                Parameter parameter = new Parameter();
                parameter.setName(getName(param));
                parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                parameter.setScope(getScope(param));
                parameter.setDescription(descriptions.get(names[i]));

                // 处理文件上传的定义
                if (paramType instanceof Class<?> && MultipartRequest.class.isAssignableFrom((Class<?>) paramType)) {
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                } else if (parameter.getScope() != null && MultipartKit.isMultipartFile(paramType)) {
                    parameter.setType(Schema.valueOf(File.class));
                } else if (parameter.getScope() != null && MultipartKit.isMultipartFiles(paramType)) {
                    parameter.setType(Schema.valueOf(File[].class));
                } else if (parameter.getScope() != null && MultipartKit.isMultipartFileMap(paramType)) {
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                } else if (parameter.getScope() != null && MultipartKit.isMultipartFileMaps(paramType)) {
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File[].class)));
                } else if (parameter.getScope() != null) {
                    parameter.setType(Schema.valueOf(paramType, supplier, interpreter));
                } else {
                    parameter.setScope(httpMethod.acceptBody ? Parameter.HTTP_PARAM_SCOPE_BODY : Parameter.HTTP_PARAM_SCOPE_QUERY);
                    parameter.setType(Schema.valueOf(paramType, supplier, interpreter));
                }

                operation.getParameters().add(parameter);
            }

            controller.getOperations().add(operation);
        }

        document.getControllers().addAll(controllers.values());

        return document;
    }

    private Type getReturnType(HandlerMethod handler) {
        Type returnType = handler.getReturnType().getGenericParameterType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type rawType = parameterizedType.getRawType();
            if (rawType == ResponseEntity.class) return parameterizedType.getActualTypeArguments()[0];
        }
        return returnType;
    }

    //cookie < header < path < query < body
    private String getScope(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestBody.class)) return Parameter.HTTP_PARAM_SCOPE_BODY;
        if (parameter.hasParameterAnnotation(RequestParam.class)) return Parameter.HTTP_PARAM_SCOPE_QUERY;
        if (parameter.hasParameterAnnotation(PathVariable.class)) return Parameter.HTTP_PARAM_SCOPE_PATH;
        if (parameter.hasParameterAnnotation(RequestHeader.class)) return Parameter.HTTP_PARAM_SCOPE_HEADER;
        if (parameter.hasParameterAnnotation(CookieValue.class)) return Parameter.HTTP_PARAM_SCOPE_COOKIE;
        return null;
    }

    // cookie < header < path < query < body
    private String getName(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestBody.class)) return "";
        if (parameter.hasParameterAnnotation(RequestParam.class)) return parameter.getParameterAnnotation(RequestParam.class).value();
        if (parameter.hasParameterAnnotation(PathVariable.class)) return parameter.getParameterAnnotation(PathVariable.class).value();
        if (parameter.hasParameterAnnotation(RequestHeader.class)) return parameter.getParameterAnnotation(RequestHeader.class).value();
        if (parameter.hasParameterAnnotation(CookieValue.class)) return parameter.getParameterAnnotation(CookieValue.class).value();
        return "";
    }

    @Override
    public String normalize(String path) {
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            String name = matcher.group(1);
            path = path.replace(matcher.group(), "{" + name + "}");
        }
        return path;
    }

}
