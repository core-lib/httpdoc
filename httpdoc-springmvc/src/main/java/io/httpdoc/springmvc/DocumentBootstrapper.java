package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件启动加载器
 *
 * @author 钟宝林
 * @date 2018-04-17 13:58
 **/
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DocumentBootstrapper implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private List<RequestMappingHandlerMapping> requestMappingHandlerMappings;

    @Resource
    private List<RequestMappingInfoHandlerMapping> handlerMappings;

    @Resource
    private ControllerTranslator controllerTranslator;

    @Resource
    private SpringMVCHttpdocConfig springMVCHttpdocConfig;

    private SpringMVCDocument springMVCDocument = SpringMVCDocument.getInstance();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (RequestMappingHandlerMapping requestMappingHandlerMapping : requestMappingHandlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
            ControllerInfoHolder.buildControllerInfo(map);
        }

        for (RequestMappingInfoHandlerMapping handlerMapping : handlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            ControllerInfoHolder.buildControllerInfo(handlerMethods);
        }

        TranslationContext translationContext = new TranslationContext();
        translationContext.setControllerInfoHolders(ControllerInfoHolder.controllerInfoHolders);
        translationContext.setInterpreter(springMVCHttpdocConfig.getInterpreter());
        Set<Controller> controllers = controllerTranslator.translate(translationContext);
        Document document = springMVCDocument.getDocument();
        if (document == null) {
            document = new Document();
            document.setHttpdoc(springMVCHttpdocConfig.getHttpdoc());
            document.setVersion(springMVCHttpdocConfig.getVersion());
            document.setContext(springMVCHttpdocConfig.getContext());
            document.setProtocol(springMVCHttpdocConfig.getProtocol());
            document.setPort(springMVCHttpdocConfig.getPort());
            document.setHostname(springMVCHttpdocConfig.getHostname());
            springMVCDocument.setDocument(document);
        }
        if (document.getControllers() == null) {
            document.setControllers(controllers);
        } else {
            document.getControllers().addAll(controllers);
        }
    }

}
