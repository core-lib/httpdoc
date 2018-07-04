package io.httpdoc.jestful;

import io.httpdoc.core.*;
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
public class JestfulServerTranslator implements Translator {
    private final Pattern pattern = Pattern.compile("\\{([^{}]+?)(:([^{}]+?))?}");
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Document translate(Translation translation) {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());

        Map<Class<?>, Controller> controllers = new LinkedHashMap<>();

        Container container = translation.getContainer();
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        ApplicationContext application = (ApplicationContext) container.get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (application == null) return document;
        MappingRegistry registry = application.getBean(MappingRegistry.class);
        if (registry == null) return document;
        Enumeration<Mapping> mappings = registry.enumeration();
        while (mappings.hasMoreElements()) {
            Mapping mapping = mappings.nextElement();
            Method method = mapping.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            Controller controller = controllers.get(clazz);
            if (controller == null) {
                controller = new Controller();
                controller.setPkg(clazz.getPackage().getName());
                controller.setName(clazz.getSimpleName());
                Resource resource = mapping.getResource();
                controller.setPath(normalize(resource.getExpression()));
                Interpretation interpretation = interpreter.interpret(clazz);
                controller.setDescription(interpretation != null ? interpretation.getContent() : null);
                controllers.put(clazz, controller);
            }
            Operation operation = new Operation();
            operation.setName(method.getName());
            operation.setPath(normalize(mapping.getExpression()));
            for (MediaType produce : mapping.getProduces()) operation.getProduces().add(produce.toString());
            for (MediaType produce : mapping.getConsumes()) operation.getConsumes().add(produce.toString());
            operation.setMethod(mapping.getRestful().getMethod());

            Map<String, String> map = new HashMap<>();
            MethodInterpretation interpretation = interpreter.interpret(method);
            Note[] notes = interpretation != null ? interpretation.getParamNotes() : null;
            for (int i = 0; notes != null && i < notes.length; i++) map.put(notes[i].getName(), notes[i].getText());
            String[] names = discoverer.getParameterNames(method);

            for (org.qfox.jestful.core.Parameter param : mapping.getParameters()) {
                Parameter parameter = new Parameter();
                parameter.setName(param.getName().equals(String.valueOf(param.getIndex())) ? "" : param.getName());
                int position = param.getPosition();
                switch (position) {
                    case Position.HEADER:
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_HEADER);
                        break;
                    case Position.PATH:
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_PATH);
                        break;
                    case Position.QUERY:
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_QUERY);
                        break;
                    case Position.BODY:
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
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

                Type type = param.getType();
                // 处理文件上传的定义
                if (type instanceof Class<?> && MultipartRequest.class.isAssignableFrom((Class<?>) type)) {
                    parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                } else if (parameter.getScope() != null && JestfulKit.isMultipartFile(type)) {
                    parameter.setType(Schema.valueOf(File.class));
                } else if (parameter.getScope() != null && JestfulKit.isMultipartFiles(type)) {
                    parameter.setType(Schema.valueOf(File[].class));
                } else if (parameter.getScope() != null && JestfulKit.isMultipartFileMap(type)) {
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                } else if (parameter.getScope() != null && JestfulKit.isMultipartFileMaps(type)) {
                    parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File[].class)));
                } else if (parameter.getScope() != null) {
                    parameter.setType(Schema.valueOf(type, supplier, interpreter));
                } else {
                    continue;
                }

                String name = names != null && names.length > param.getIndex() ? names[param.getIndex()] : param.getName();
                String description = map.get(name);
                parameter.setDescription(description);

                operation.getParameters().add(parameter);
            }
            Result result = new Result();
            Schema type = Schema.valueOf(mapping.getResult().getType(), supplier, interpreter);
            result.setType(type);
            result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
            operation.setResult(result);

            operation.setDescription(interpretation != null ? interpretation.getContent() : null);
            controller.getOperations().add(operation);
        }

        document.setControllers(new LinkedHashSet<>(controllers.values()));

        return document;
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
