package io.httpdoc.jestful.server;

import io.httpdoc.core.*;
import io.httpdoc.core.annotation.*;
import io.httpdoc.core.annotation.Package;
import io.httpdoc.core.interpretation.ClassInterpretation;
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
import org.springframework.util.StringUtils;
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
        document.setDescription(translation.getDescription());

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

            // 如果Controller或方法的注释上有@skip标签则忽略
            {
                ClassInterpretation interpretation = interpreter.interpret(clazz);
                if (interpretation != null && interpretation.isSkip()) continue;
            }
            {
                MethodInterpretation interpretation = interpreter.interpret(method);
                if (interpretation != null && interpretation.isSkip()) continue;
            }

            // 是否忽略
            if (clazz.isAnnotationPresent(Skip.class) || method.isAnnotationPresent(Skip.class)) continue;
            Controller controller = controllers.get(clazz);
            if (controller == null) {
                ClassInterpretation interpretation = interpreter.interpret(clazz);

                controller = new Controller();

                // 重定义包名
                String pkg = interpretation != null ? interpretation.getPackage() : null;
                if (StringUtils.hasText(pkg)) controller.setPkg(pkg);
                else controller.setPkg(clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName());

                String name = interpretation != null ? interpretation.getName() : null;
                if (StringUtils.hasText(name)) controller.setName(name);
                else controller.setName(clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName());

                Resource resource = mapping.getResource();
                controller.setPath(normalize(resource.getExpression()));

                controller.setSummary(interpretation != null ? interpretation.getSummary() : null);
                controller.setDescription(interpretation != null ? interpretation.getContent() : null);
                controller.setDeprecated(
                        interpretation != null && interpretation.getDeprecated() != null
                                ? interpretation.getDeprecated()
                                : clazz.isAnnotationPresent(Deprecated.class)
                                ? "deprecated"
                                : null
                );

                List<String> tags = interpretation != null ? interpretation.getTags() : null;
                if (tags != null && !tags.isEmpty()) {
                    controller.setTags(tags);
                } else {
                    Tag tag = clazz.getAnnotation(Tag.class);
                    if (tag == null || tag.value().length == 0 || !tag.override()) controller.getTags().add(controller.getName());
                    if (tag != null) controller.getTags().addAll(Arrays.asList(tag.value()));
                }
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

        MethodInterpretation interpretation = interpreter.interpret(method);

        Operation operation = new Operation();
        // 重定义方法名
        String name = interpretation != null ? interpretation.getName() : null;
        if (StringUtils.hasText(name)) operation.setName(name);
        else operation.setName(method.isAnnotationPresent(Name.class) ? method.getAnnotation(Name.class).value() : method.getName());
        operation.setPath(normalize(mapping.getExpression()));
        for (MediaType produce : mapping.getProduces()) operation.getProduces().add(produce.toString());
        for (MediaType consume : mapping.getConsumes()) operation.getConsumes().add(consume.toString());
        operation.setMethod(mapping.getRestful().getMethod());

        operation.setSummary(interpretation != null ? interpretation.getSummary() : null);
        operation.setDescription(interpretation != null ? interpretation.getContent() : null);
        operation.setDeprecated(
                interpretation != null && interpretation.getDeprecated() != null
                        ? interpretation.getDeprecated()
                        : method.isAnnotationPresent(Deprecated.class)
                        ? "deprecated"
                        : null
        );

        List<String> tags = interpretation != null ? interpretation.getTags() : null;
        if (tags != null && !tags.isEmpty()) {
            operation.setTags(tags);
        } else {
            Tag tag = method.getAnnotation(Tag.class);
            if (tag == null || tag.value().length == 0 || !tag.override()) operation.getTags().addAll(controller.getTags());
            if (tag != null) operation.getTags().addAll(Arrays.asList(tag.value()));
        }

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

        // 忽略的参数名称
        List<String> ignores = interpretation != null ? interpretation.getIgnores() : Collections.<String>emptyList();
        Map<String, String> aliases = interpretation != null ? interpretation.getAliases() : Collections.<String, String>emptyMap();

        for (org.qfox.jestful.core.Parameter param : mapping.getParameters()) {
            // 忽略
            int index = param.getIndex();
            if ((names.length > index && ignores.contains(names[index])) || param.isAnnotationPresent(Ignore.class)) continue;

            Parameter parameter = new Parameter();
            parameter.setName(param.getName().equals(String.valueOf(index)) ? "" : param.getName());
            if (StringUtils.hasText(names.length > index ? aliases.get(names[index]) : "")) {
                parameter.setAlias(aliases.get(names[index]).trim());
            } else {
                parameter.setAlias(param.isAnnotationPresent(Alias.class) ? param.getAnnotation(Alias.class).value() : parameter.getName());
            }
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

            String name = names.length > index ? names[index] : param.getName();
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
