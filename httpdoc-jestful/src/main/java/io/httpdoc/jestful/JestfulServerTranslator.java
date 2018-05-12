package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.interpretation.Interpretation;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import io.httpdoc.core.interpretation.Note;
import io.httpdoc.core.provider.Provider;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
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
        Provider provider = translation.getProvider();
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

            loop:
            for (org.qfox.jestful.core.Parameter param : mapping.getParameters()) {
                Parameter parameter = new Parameter();
                parameter.setName(param.getName());
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
                    default:
                        continue loop;
                }
                Schema type = Schema.valueOf(param.getType(), provider, interpreter);
                parameter.setType(type);

                String name = names != null && names.length > param.getIndex() ? names[param.getIndex()] : param.getName();
                String description = map.get(name);
                parameter.setDescription(description);

                operation.getParameters().add(parameter);
            }
            Result result = new Result();
            Schema type = Schema.valueOf(mapping.getResult().getType(), provider, interpreter);
            result.setType(type);
            result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
            operation.setResult(result);

            operation.setDescription(interpretation != null ? interpretation.getContent() : null);
            controller.getOperations().add(operation);
        }

        document.setControllers(new ArrayList<>(controllers.values()));

        for (Controller controller : controllers.values()) {
            for (Operation operation : controller.getOperations()) {
                for (Parameter parameter : operation.getParameters()) {
                    Schema type = parameter.getType();
                    Collection<Schema> dependencies = type.getDependencies();
                    for (Schema schema : dependencies) document.getSchemas().put(schema.getName(), schema);
                }
                Schema type = operation.getResult().getType();
                Collection<Schema> dependencies = type.getDependencies();
                for (Schema schema : dependencies) document.getSchemas().put(schema.getName(), schema);
            }
        }

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
