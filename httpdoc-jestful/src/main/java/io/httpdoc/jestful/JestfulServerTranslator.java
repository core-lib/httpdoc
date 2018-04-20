package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.DocumentTranslationException;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.server.MappingRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

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
        }

        return document;
    }

}
