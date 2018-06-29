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
    private DefaultControllerTranslator springmvcDocumentTranslator;

    @Resource
    private SpringmvcHttpdocConfig springmvcHttpdocConfig;

    private SpringmvcDocument springmvcDocument = SpringmvcDocument.getInstance();

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

        TranslateContext translateContext = new TranslateContext();
        translateContext.setControllerInfoHolders(ControllerInfoHolder.controllerInfoHolders);
        translateContext.setInterpreter(springmvcHttpdocConfig.getInterpreter());
        Set<Controller> controllers = springmvcDocumentTranslator.translator(translateContext);
        Document document = springmvcDocument.getDocument();
        if (document == null) {
            document = new Document();
            document.setHttpdoc(springmvcHttpdocConfig.getHttpdoc());
            document.setVersion(springmvcHttpdocConfig.getVersion());
            document.setContext(springmvcHttpdocConfig.getContext());
            document.setProtocol(springmvcHttpdocConfig.getProtocol());
            document.setPort(springmvcHttpdocConfig.getPort());
            document.setHostname(springmvcHttpdocConfig.getHostname());
            springmvcDocument.setDocument(document);
        }
        if (document.getControllers() == null) {
            document.setControllers(controllers);
        } else {
            document.getControllers().addAll(controllers);
        }
    }

}
