package io.httpdoc.jestful.server;

import io.httpdoc.core.*;
import io.httpdoc.core.annotation.*;
import io.httpdoc.core.annotation.Package;
import io.httpdoc.core.interpretation.Interpretation;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import io.httpdoc.core.interpretation.Note;
import io.httpdoc.core.reflection.ParameterizedTypeImpl;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.server.MappingRegistry;
import org.qfox.jestful.server.annotation.Field;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jestful 翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 9:29
 **/
public class JestfulTranslator implements Translator {
    protected final Pattern pattern = Pattern.compile("\\{([^{}]+?)(:([^{}]+?))?}");
    protected final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Document translate(Translation translation) {
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

    protected void translate(ControllerTranslation translation) {
        Map<Class<?>, Controller> controllers = new LinkedHashMap<>();

        Document document = translation.getDocument();
        Container container = translation.getContainer();
        Interpreter interpreter = translation.getInterpreter();
        ApplicationContext application = (ApplicationContext) container.get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (application == null) return;
        MappingRegistry registry = application.getBean(MappingRegistry.class);
        if (registry == null) return;
        Enumeration<Mapping> mappings = registry.enumeration();
        while (mappings.hasMoreElements()) {
            Mapping mapping = mappings.nextElement();
            Method method = mapping.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            // 是否忽略
            if (clazz.isAnnotationPresent(Ignore.class) || method.isAnnotationPresent(Ignore.class)) continue;
            Controller controller = controllers.get(clazz);
            if (controller == null) {
                controller = new Controller();
                // 重定义包名
                controller.setPkg(clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName());
                controller.setName(clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName());
                Resource resource = mapping.getResource();
                controller.setPath(normalize(resource.getExpression()));
                Interpretation interpretation = interpreter.interpret(clazz);
                controller.setDescription(interpretation != null ? interpretation.getContent() : null);
                Tag tag = clazz.getAnnotation(Tag.class);
                if (tag == null || tag.value().length == 0 || !tag.override()) controller.getTags().add(controller.getName());
                if (tag != null) controller.getTags().addAll(Arrays.asList(tag.value()));
                controllers.put(clazz, controller);
            }
            translate(new OperationTranslation(translation, mapping, method, controller));
        }

        document.getControllers().addAll(controllers.values());
    }

    protected void translate(OperationTranslation translation) {
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        Mapping mapping = translation.getMapping();
        Method method = translation.getMethod();
        Controller controller = translation.getController();

        Operation operation = new Operation();
        // 重定义方法名
        operation.setName(method.isAnnotationPresent(Name.class) ? method.getAnnotation(Name.class).value() : method.getName());
        operation.setPath(normalize(mapping.getExpression()));
        for (MediaType produce : mapping.getProduces()) operation.getProduces().add(produce.toString());
        for (MediaType consume : mapping.getConsumes()) operation.getConsumes().add(consume.toString());
        operation.setMethod(mapping.getRestful().getMethod());

        Tag tag = method.getAnnotation(Tag.class);
        if (tag == null || tag.value().length == 0 || !tag.override()) operation.getTags().addAll(controller.getTags());
        if (tag != null) operation.getTags().addAll(Arrays.asList(tag.value()));

        MethodInterpretation interpretation = interpreter.interpret(method);
        operation.setDescription(interpretation != null ? interpretation.getContent() : null);

        Result result = new Result();
        Type resultType = mapping.getResult().getType();
        Schema type = Schema.valueOf(resultType, supplier, interpreter);
        result.setType(type);
        result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
        operation.setResult(result);

        translate(new ParameterTranslation(translation, mapping, method, controller, operation));

        controller.getOperations().add(operation);
    }

    protected void translate(ParameterTranslation translation) {
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        Mapping mapping = translation.getMapping();
        Method method = translation.getMethod();
        Operation operation = translation.getOperation();

        Map<String, String> map = new HashMap<>();
        MethodInterpretation interpretation = interpreter.interpret(method);
        Note[] notes = interpretation != null ? interpretation.getParamNotes() : null;
        for (int i = 0; notes != null && i < notes.length; i++) map.put(notes[i].getName(), notes[i].getText());
        String[] names = discoverer.getParameterNames(method);

        for (org.qfox.jestful.core.Parameter param : mapping.getParameters()) {
            // 忽略
            if (param.isAnnotationPresent(Ignore.class)) continue;

            Parameter parameter = new Parameter();
            parameter.setName(param.getName().equals(String.valueOf(param.getIndex())) ? "" : param.getName());
            parameter.setAlias(param.isAnnotationPresent(Alias.class) ? param.getAnnotation(Alias.class).value() : parameter.getName());
            int bodies = 0;
            int position = param.getPosition();
            switch (position) {
                case Position.HEADER:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_HEADER);
                    break;
                case Position.PATH:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_PATH);
                    break;
                case Position.MATRIX:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_MATRIX);
                    parameter.setPath((String) param.property("path"));
                    break;
                case Position.QUERY:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_QUERY);
                    break;
                case Position.BODY:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    bodies++;
                    break;
                case Position.COOKIE:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_COOKIE);
                    break;
                case Field.POSITION:
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_FIELD);
                    break;
                default:
                    break;
            }

            boolean upload = false;
            Type type = param.getType();
            // 处理文件上传的定义
            if (type instanceof Class<?> && MultipartRequest.class.isAssignableFrom((Class<?>) type)) {
                parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                upload = true;
            } else if (parameter.getScope() != null && JestfulKit.isMultipartFile(type)) {
                parameter.setType(Schema.valueOf(File.class));
                upload = true;
            } else if (parameter.getScope() != null && JestfulKit.isMultipartFiles(type)) {
                parameter.setType(Schema.valueOf(File[].class));
                upload = true;
            } else if (parameter.getScope() != null && JestfulKit.isMultipartFileMap(type)) {
                parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                upload = true;
            } else if (parameter.getScope() != null && JestfulKit.isMultipartFileMaps(type)) {
                parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File[].class)));
                upload = true;
            } else if (parameter.getScope() != null) {
                parameter.setType(Schema.valueOf(type, supplier, interpreter));
            } else {
                continue;
            }
            // 如果来源于请求体的参数数量大于 1 个 或者有上传文件的参数 则为 multipart 方法
            operation.setMultipart(bodies > 1 || upload);

            String name = names != null && names.length > param.getIndex() ? names[param.getIndex()] : param.getName();
            String description = map.get(name);
            parameter.setDescription(description);

            operation.getParameters().add(parameter);
        }
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
