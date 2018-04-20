package io.httpdoc.springmvc;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    private List<ControllerInfoHolder> controllerInfoHolders = new ArrayList<>();

    @Resource
    private List<RequestMappingHandlerMapping> requestMappingHandlerMappings;

    @Resource
    private List<RequestMappingInfoHandlerMapping> handlerMappings;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (RequestMappingHandlerMapping requestMappingHandlerMapping : requestMappingHandlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
            buildControllers(map);
        }

        for (RequestMappingInfoHandlerMapping handlerMapping : handlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            buildControllers(handlerMethods);
        }
        handle();
    }

    private void buildControllers(Map<RequestMappingInfo, HandlerMethod> map) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) entry.getKey();
            HandlerMethod handlerMethod = (HandlerMethod) entry.getValue();

            ControllerInfoHolder controllerInfoHolder = new ControllerInfoHolder();
            controllerInfoHolder.setHandlerMethod(handlerMethod);
            controllerInfoHolder.setRequestMappingInfo(requestMappingInfo);
            controllerInfoHolders.add(controllerInfoHolder);
        }
    }

    public void handle() {
        for (ControllerInfoHolder controllerInfoHolder : controllerInfoHolders) {
            if (controllerInfoHolder.isHandled()) {
                continue;
            }
            controllerInfoHolder.setHandled(true);

            HandlerMethod handlerMethod = controllerInfoHolder.getHandlerMethod();
            RequestMappingInfo requestMappingInfo = controllerInfoHolder.getRequestMappingInfo();

            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            ParamsRequestCondition paramsCondition = requestMappingInfo.getParamsCondition();
            ConsumesRequestCondition consumesCondition = requestMappingInfo.getConsumesCondition();
            ProducesRequestCondition producesCondition = requestMappingInfo.getProducesCondition();
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
            HeadersRequestCondition headersCondition = requestMappingInfo.getHeadersCondition();
            RequestCondition<?> customCondition = requestMappingInfo.getCustomCondition();

            MethodParameter returnType = handlerMethod.getReturnType();
            Type genericParameterType = returnType.getGenericParameterType();

            if (genericParameterType != null && genericParameterType instanceof ParameterizedType) {
                // 接口返回值包含泛型的Java类, 比如ResponseEntity<?>
                ParameterizedType responseEntityType = (ParameterizedType) genericParameterType;
                Class<?> rawType = (Class<?>) responseEntityType.getRawType();
                Type[] actualTypeArguments = responseEntityType.getActualTypeArguments();
                System.out.println(responseEntityType);
            } else {
                // 接口返回普通Java类
                Class<?> objectType = (Class<?>) genericParameterType;
                System.out.println(objectType);
            }

            HandlerMethod resolvedFromHandlerMethod = handlerMethod.getResolvedFromHandlerMethod();
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

            System.out.println("hh");
        }
    }
}
