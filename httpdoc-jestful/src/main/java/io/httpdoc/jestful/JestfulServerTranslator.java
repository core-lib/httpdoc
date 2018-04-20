package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.Result;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.provider.Provider;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
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

        ServletContext servletContext = translation.getServletContext();
        Provider provider = translation.getProvider();
        Interpreter interpreter = translation.getInterpreter();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        MappingRegistry mappingRegistry = applicationContext.getBean(MappingRegistry.class);
        Enumeration<Mapping> mappings = mappingRegistry.enumeration();
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
            for (Parameter parameter : mapping.getParameters()) {
                io.httpdoc.core.Parameter param = new io.httpdoc.core.Parameter();
                param.setName(parameter.getName());
                int position = parameter.getPosition();
                switch (position) {
                    case Position.HEADER:
                        param.setScope("header");
                        break;
                    case Position.PATH:
                        param.setScope("path");
                        break;
                    case Position.QUERY:
                        param.setScope("query");
                        break;
                    case Position.BODY:
                        param.setScope("body");
                        break;
                    case Position.COOKIE:
                        param.setScope("cookie");
                        break;
                    default:
                        continue loop;
                }
                Schema type = Schema.valueOf(parameter.getType(), provider, interpreter);
                param.setType(type);
                operation.getParameters().add(param);
            }
            Result result = new Result();
            Schema type = Schema.valueOf(mapping.getResult().getType(), provider, interpreter);
            result.setType(type);
            operation.setResult(result);
            operation.setDescription(interpreter.interpret(method));

            controller.getOperations().add(operation);
        }

        document.setControllers(new ArrayList<>(controllers.values()));

        for (Controller controller : controllers.values()) {
            for (Operation operation : controller.getOperations()) {
                for (io.httpdoc.core.Parameter parameter : operation.getParameters()) {
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
