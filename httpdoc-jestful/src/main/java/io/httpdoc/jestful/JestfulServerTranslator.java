package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import io.httpdoc.core.provider.Provider;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Jestful 翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 9:29
 **/
public class JestfulServerTranslator implements Translator {

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();

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
                controller.setPath(resource.getExpression());
                controllers.put(clazz, controller);
            }
            Operation operation = new Operation();
            operation.setName(method.getName());
            operation.setPath(mapping.getExpression());
            for (MediaType produce : mapping.getProduces()) operation.getProduces().add(produce.toString());
            for (MediaType produce : mapping.getConsumes()) operation.getConsumes().add(produce.toString());
            operation.setMethod(mapping.getRestful().getMethod());

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
                operation.getParameters().add(parameter);
            }
            Result result = new Result();
            Schema type = Schema.valueOf(mapping.getResult().getType(), provider, interpreter);
            result.setType(type);
            operation.setResult(result);
            MethodInterpretation interpretation = interpreter.interpret(method);
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

}
