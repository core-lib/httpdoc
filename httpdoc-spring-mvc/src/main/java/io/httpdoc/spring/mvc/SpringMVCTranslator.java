package io.httpdoc.spring.mvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * SpringMVC 文档翻译器
 *
 * @author 钟宝林
 * @date 2018-04-27 21:20
 **/
public class SpringMVCTranslator implements Translator {

    private static final ControllerTranslator controllerTranslator = new DefaultControllerTranslator();

    @Override
    public Document translate(Translation translation) {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());

        List<ApplicationContext> applicationContexts = listApplicationContext(translation.getContainer());
        for (ApplicationContext applicationContext : applicationContexts) {
            Collection<RequestMappingHandlerMapping> requestMappingHandlerMappings = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class).values();
            for (RequestMappingHandlerMapping requestMappingHandlerMapping : requestMappingHandlerMappings) {
                Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
                ControllerInfoHolder.buildControllerInfo(map);
            }

            Collection<RequestMappingInfoHandlerMapping> requestMappingInfoHandlerMappings = applicationContext.getBeansOfType(RequestMappingInfoHandlerMapping.class).values();
            for (RequestMappingInfoHandlerMapping handlerMapping : requestMappingInfoHandlerMappings) {
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
                ControllerInfoHolder.buildControllerInfo(handlerMethods);
            }
        }

        TranslationContext translationContext = new TranslationContext();
        translationContext.setControllerInfoHolders(ControllerInfoHolder.controllerInfoHolders);
        translationContext.setInterpreter(translation.getInterpreter());
        Set<Controller> controllers = controllerTranslator.translate(translationContext);
        document.setControllers(controllers);
        return document;
    }

    /**
     * 获取当前环境下的所有 Spring 容器
     *
     * @param container 环境
     * @return {@link ApplicationContext} 列表
     */
    private List<ApplicationContext> listApplicationContext(Container container) {
        List<ApplicationContext> applicationContexts = new ArrayList<>();
        Enumeration<String> attrNames = container.names();
        while (attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            Object attrValue = container.get(attrName);
            if (attrValue instanceof WebApplicationContext) {
                applicationContexts.add((ApplicationContext) attrValue);
            }
        }
        return applicationContexts;
    }

    @Override
    public String normalize(String path) {
        return new PathProcessor(path).process();
    }

}
