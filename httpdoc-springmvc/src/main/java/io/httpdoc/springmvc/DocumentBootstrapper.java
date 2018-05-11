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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件启动加载器
 *
 * @author 钟宝林
 * @date 2018-04-17 13:58
 **/
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DocumentBootstrapper implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * SpringMVC 接口信息持有
     */
    private static final List<ControllerInfoHolder> controllerInfoHolders = new ArrayList<>();

    @Resource
    private List<RequestMappingHandlerMapping> requestMappingHandlerMappings;

    @Resource
    private List<RequestMappingInfoHandlerMapping> handlerMappings;

    @Resource
    private DefaultControllerTranslator springmvcDocumentTranslator;

    private SpringmvcDocument springmvcDocument = SpringmvcDocument.getInstance();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (RequestMappingHandlerMapping requestMappingHandlerMapping : requestMappingHandlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
            buildControllerInfo(map);
        }

        for (RequestMappingInfoHandlerMapping handlerMapping : handlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            buildControllerInfo(handlerMethods);
        }

        TranslateContext translateContext = new TranslateContext();
        translateContext.setControllerInfoHolders(controllerInfoHolders);
        List<Controller> controllers = springmvcDocumentTranslator.translator(translateContext);
        Document document = springmvcDocument.getDocument();
        if (document == null) {
            document = new Document();
            springmvcDocument.setDocument(document);
        }
        if (document.getControllers() == null) {
            document.setControllers(controllers);
        } else {
            document.getControllers().addAll(controllers);
        }
    }

    /**
     * 封装从 SpringMVC 取到的接口信息到{@link DocumentBootstrapper#controllerInfoHolders}
     *
     * @param map 接口信息
     */
    private void buildControllerInfo(Map<RequestMappingInfo, HandlerMethod> map) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) entry.getKey();
            HandlerMethod handlerMethod = (HandlerMethod) entry.getValue();

            ControllerInfoHolder controllerInfoHolder = new ControllerInfoHolder();
            controllerInfoHolder.setHandlerMethod(handlerMethod);
            controllerInfoHolder.setRequestMappingInfo(requestMappingInfo);
            if (controllerInfoHolders.contains(controllerInfoHolder)) {
                continue;
            }
            controllerInfoHolders.add(controllerInfoHolder);
        }
    }

}
