package io.httpdoc.spring.mvc;

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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
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
import javax.servlet.http.Part;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringMVCTranslator implements Translator {

    private static final String EMPTY = "";

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
            UriComponentsBuilder.class,
            Model.class,
            ModelMap.class,
            ExtendedModelMap.class
    );

    private static final Collection<Class<?>> PRIMITIVE_TYPES = Arrays.<Class<?>>asList(
            boolean.class,
            byte.class,
            short.class,
            char.class,
            int.class,
            float.class,
            long.class,
            double.class
    );

    private static final Collection<Class<?>> WRAPPER_TYPES = Arrays.<Class<?>>asList(
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Float.class,
            Long.class,
            Double.class
    );

    private static final Collection<Class<?>> NUMBER_TYPES = Arrays.<Class<?>>asList(
            BigInteger.class,
            BigDecimal.class,
            AtomicInteger.class,
            AtomicLong.class
    );

    private static final Collection<Class<?>> STRING_TYPES = Arrays.<Class<?>>asList(
            String.class,
            StringBuilder.class,
            StringBuffer.class
    );

    private static final Collection<Class<?>> DATE_TYPES = Arrays.<Class<?>>asList(
            java.util.Date.class,
            java.sql.Date.class,
            java.sql.Time.class,
            java.sql.Timestamp.class
    );

    private static final Pattern PATTERN = Pattern.compile("\\{([^{}]+?)(:([^{}]+?))?}");
    private static final ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 获取多请求体的该参数部分的绑定名称， 缺省情况下采用参数变量名， 如果有@RequestPart 和 @RequestParam 则 @RequestParam 要优先于 @RequestPart
     *
     * @param parameter 参数
     * @return 多请求体情况下的该参数部分的绑定名称
     */
    private static String getPartName(MethodParameter parameter) {
        Method method = parameter.getMethod();
        int index = parameter.getParameterIndex();
        String[] names = DISCOVERER.getParameterNames(method);
        String bindName = names[index];

        RequestPart part = parameter.getParameterAnnotation(RequestPart.class);
        String partValue = part != null ? part.value() : EMPTY;
        String partName = part != null ? part.name() : EMPTY;
        bindName = partValue.isEmpty() ? partName.isEmpty() ? bindName : partName : partValue;

        RequestParam param = parameter.getParameterAnnotation(RequestParam.class);
        String paramValue = param != null ? param.value() : EMPTY;
        String paramName = param != null ? param.name() : EMPTY;
        bindName = paramValue.isEmpty() ? paramName.isEmpty() ? bindName : paramName : paramValue;

        return bindName;
    }

    /**
     * 获取方法的返回值结果类型， 如果是 ResponseEntity<T> 则采用类型参数的实际类型
     *
     * @param handler 处理器方法
     * @return 方法的返回值结果类型
     */
    private static Type getReturnType(HandlerMethod handler) {
        Type returnType = handler.getReturnType().getGenericParameterType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type rawType = parameterizedType.getRawType();
            if (rawType == ResponseEntity.class) return parameterizedType.getActualTypeArguments()[0];
        }
        return returnType;
    }

    /**
     * 是否为简单类型
     *
     * @param type 类型
     * @return 是否为简单类型
     */
    private static boolean isSimpleType(Type type) {
        // 普通类
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            // 数组类型则看数组最底层是否是基本类型
            if (clazz.isArray()) return isSimpleType(clazz.getComponentType());
            // 枚举类型
            if (clazz.isEnum()) return true;
            // 基本类型
            if (PRIMITIVE_TYPES.contains(clazz)) return true;
            // 基本类型的封装类型
            if (WRAPPER_TYPES.contains(clazz)) return true;
            // 数字类型
            if (NUMBER_TYPES.contains(clazz)) return true;
            // 字符串类型
            if (STRING_TYPES.contains(clazz)) return true;
            // 日期类型
            if (DATE_TYPES.contains(clazz)) return true;
            // 否则都不是简单类型
            return false;
        }
        // 泛型类
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            // 未知类型
            if (!Class.class.isInstance(rawType)) return false;
            Class<?> clazz = (Class<?>) rawType;
            // 集合类型 则看元素类型是否是简单类型
            if (Collection.class.isAssignableFrom(clazz)) return isSimpleType(parameterizedType.getActualTypeArguments()[0]);
            // Map类型 则看Key 和 Value的类型是否为简单类型
            if (Map.class.isAssignableFrom(clazz)) return isSimpleType(parameterizedType.getActualTypeArguments()[0]) && isSimpleType(parameterizedType.getActualTypeArguments()[1]);
            // 否则都不是简单类型
            return false;
        }
        // 其他的都统一认为是非简单类型
        return false;
    }

    private static boolean isMultipartFile(Type type) {
        return type instanceof Class<?>
                && (MultipartFile.class.isAssignableFrom((Class<?>) type) || Part.class.isAssignableFrom((Class<?>) type));
    }

    private static boolean isMultipartFiles(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return clazz.isArray() && isMultipartFile(clazz.getComponentType());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class<?>)) return false;
            Class<?> clazz = (Class<?>) rawType;
            if (!Collection.class.isAssignableFrom(clazz)) return false;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            return isMultipartFile(actualTypeArgument);
        } else {
            return false;
        }
    }

    /**
     * 获取参数的绑定域
     * 如果存在多个指定绑定域的注解则优先级按cookie < header < path < query < body
     * 如果没有指定绑定域的注解被声明则返回{@code null}调用者需要额外处理， 可能是文件上传
     *
     * @param parameter 参数
     * @return 绑定域
     */
    private static String getBindScope(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestBody.class)) return Parameter.HTTP_PARAM_SCOPE_BODY;
        if (parameter.hasParameterAnnotation(RequestPart.class)) return Parameter.HTTP_PARAM_SCOPE_BODY;
        if (parameter.hasParameterAnnotation(PathVariable.class)) return Parameter.HTTP_PARAM_SCOPE_PATH;
        if (parameter.hasParameterAnnotation(RequestHeader.class)) return Parameter.HTTP_PARAM_SCOPE_HEADER;
        if (parameter.hasParameterAnnotation(CookieValue.class)) return Parameter.HTTP_PARAM_SCOPE_COOKIE;
        return null;
    }

    /**
     * 获取参数的绑定名称
     * 如果同时存在多个同类注解则优先级按cookie < header < path < query < body
     * 如果存在符合要求的注解但没有指定 value 和 name 则采用参数的变量名
     *
     * @param parameter 参数
     * @return 参数名
     */
    private static String getBindName(MethodParameter parameter) {
        Method method = parameter.getMethod();
        int index = parameter.getParameterIndex();
        String[] names = DISCOVERER.getParameterNames(method);
        if (parameter.hasParameterAnnotation(RequestBody.class)) {
            return EMPTY;
        }
        if (parameter.hasParameterAnnotation(RequestPart.class)) {
            return getPartName(parameter);
        }
        if (parameter.hasParameterAnnotation(PathVariable.class)) {
            PathVariable variable = parameter.getParameterAnnotation(PathVariable.class);
            String value = variable.value();
            String name = variable.name();
            return value.isEmpty() ? name.isEmpty() ? names[index] : name : value;
        }
        if (parameter.hasParameterAnnotation(RequestHeader.class)) {
            RequestHeader header = parameter.getParameterAnnotation(RequestHeader.class);
            String value = header.value();
            String name = header.name();
            return value.isEmpty() ? name.isEmpty() ? names[index] : name : value;
        }
        if (parameter.hasParameterAnnotation(CookieValue.class)) {
            CookieValue cookie = parameter.getParameterAnnotation(CookieValue.class);
            String value = cookie.value();
            String name = cookie.name();
            return value.isEmpty() ? name.isEmpty() ? names[index] : name : value;
        }
        return EMPTY;
    }

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());

        translate(new ControllerTranslation(translation, document));

        return document;
    }

    private void translate(ControllerTranslation translation) {
        Document document = translation.getDocument();

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
            translate(new OperationTranslation(translation, mapping, handler, method, controller));
        }

        document.getControllers().addAll(controllers.values());
    }

    private void translate(OperationTranslation translation) {
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        RequestMappingInfo mapping = translation.getMapping();
        HandlerMethod handler = translation.getHandler();
        Method method = translation.getMethod();
        Controller controller = translation.getController();
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
        Result result = new Result();
        Type resultType = getReturnType(handler);
        Schema schema = Schema.valueOf(resultType, supplier, interpreter);
        result.setType(schema);
        result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
        operation.setResult(result);

        translate(new ParameterTranslation(translation, mapping, handler, method, controller, operation));

        controller.getOperations().add(operation);
    }

    private void translate(ParameterTranslation translation) {
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        HandlerMethod handler = translation.getHandler();
        Method method = translation.getMethod();
        Operation operation = translation.getOperation();
        // 参数解析
        Map<String, String> descriptions = new HashMap<>();
        MethodInterpretation interpretation = interpreter.interpret(method);
        Note[] notes = interpretation != null ? interpretation.getParamNotes() : null;
        for (int i = 0; notes != null && i < notes.length; i++) descriptions.put(notes[i].getName(), notes[i].getText());
        String[] names = DISCOVERER.getParameterNames(method);

        MethodParameter[] params = handler.getMethodParameters();
        for (int i = 0; i < params.length; i++) {
            MethodParameter param = params[i];
            // 忽略
            if (param.hasParameterAnnotation(Ignore.class)) continue;
            Type paramType = param.getGenericParameterType();
            if (paramType instanceof Class<?> && IGNORED_PARAMETER_TYPES.contains(paramType)) continue;

            int bodies = 0;
            Parameter parameter = new Parameter();
            String name = getBindName(param);
            parameter.setName(name);
            parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
            String scope = getBindScope(param);
            bodies = bodies + (Parameter.HTTP_PARAM_SCOPE_BODY.equals(scope) ? 1 : 0);
            parameter.setScope(scope);
            parameter.setDescription(descriptions.get(names[i]));

            // 上面已经处理了明确的参数来源域 下面处理文件上传的参数和没有声明注解的参数

            boolean upload = false;
            // 1. 如果是文件上传的请求
            if (paramType instanceof Class<?> && MultipartRequest.class.isAssignableFrom((Class<?>) paramType)) {
                parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                upload = true;
            }
            // 2. 如果是MultipartFile或者Part的子类 则也是文件上传 同时参数的名称优先采用 @RequestParam 注解的名称 其次采用参数的变量名
            else if (isMultipartFile(paramType)) {
                parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                parameter.setType(Schema.valueOf(File.class));
                String paramName = getPartName(param);
                parameter.setName(paramName);
                parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                upload = true;
            }
            // 3. 如果是MultipartFile或者Part的数组 则也是文件上传 同时参数的名称优先采用 @RequestParam 注解的名称 其次采用参数的变量名
            else if (isMultipartFiles(paramType)) {
                parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                parameter.setType(Schema.valueOf(File[].class));
                String paramName = getPartName(param);
                parameter.setName(paramName);
                parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                upload = true;
            }
            // 4. 如果绑定域不是空的话 则表明用户明确指定过参数绑定域 框架也默认用户声明这个参数来源于客户端 框架不予忽略
            else if (scope == null) {
                // 没有指定绑定域的参数统一当作参数绑定域来自于 Query 主要考虑更加通用
                parameter.setScope(Parameter.HTTP_PARAM_SCOPE_QUERY);
                parameter.setType(Schema.valueOf(paramType, supplier, interpreter));

                // 4-1. 如果指定了 @RequestParam 注解而且指定了 value 或 name 的任意一个 则采用注解的声明名称
                RequestParam annotation = param.getParameterAnnotation(RequestParam.class);
                String annoValue = annotation != null ? annotation.value() : EMPTY;
                String annoName = annotation != null ? annotation.name() : EMPTY;
                if (!annoValue.isEmpty() || !annoName.isEmpty()) {
                    parameter.setName(annoValue.isEmpty() ? annoName : annoValue);
                    parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                }
                // 4-2. 如果没有指定那么还会分两种情况
                // 1. 简单类型-采用参数变量名称
                else if (isSimpleType(paramType)) {
                    int index = param.getParameterIndex();
                    parameter.setName(names[index]);
                    parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                }
                //  2. 自定义类型则无名称
                else {
                    parameter.setName(EMPTY);
                    parameter.setAlias(param.hasParameterAnnotation(Alias.class) ? param.getParameterAnnotation(Alias.class).value() : parameter.getName());
                }
            }
            // 5. 绑定域不为空 则参数名也不会为空 只需要指定参数类型即可
            else {
                parameter.setType(Schema.valueOf(paramType, supplier, interpreter));
            }

            operation.setMultipart(bodies > 1 || upload);
            operation.getParameters().add(parameter);
        }
    }

    @Override
    public String normalize(String path) {
        Matcher matcher = PATTERN.matcher(path);
        while (matcher.find()) {
            String name = matcher.group(1);
            path = path.replace(matcher.group(), "{" + name + "}");
        }
        return path;
    }

}
