package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Operation;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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

    @Resource
    private SpringmvcDocument document;

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

        document.setDocument(translate());
    }

    private Document translate() {
        Document document = new Document();

        Map<Class<?>, Controller> controllerMap = new LinkedHashMap<>();

        List<Controller> controllers = new LinkedList<>();
        document.setControllers(controllers);
        for (ControllerInfoHolder controllerInfoHolder : controllerInfoHolders) {
            if (controllerInfoHolder.isHandled()) {
                continue;
            }
            controllerInfoHolder.setHandled(true);
            HandlerMethod handlerMethod = controllerInfoHolder.getHandlerMethod();

            Class<?> beanType = handlerMethod.getBeanType();
            Controller controller = controllerMap.get(beanType);
            if (controller == null) {
                controller = new Controller();
                controller.setName(beanType.getSimpleName());
            }

            RequestMappingInfo requestMappingInfo = controllerInfoHolder.getRequestMappingInfo();

            List<Operation> operations = buildOperations(requestMappingInfo, handlerMethod);
            controller.setOperations(operations);
        }
        return document;
    }

    private List<Operation> buildOperations(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();

        RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
        ParamsRequestCondition paramsCondition = requestMappingInfo.getParamsCondition();
        ConsumesRequestCondition consumesCondition = requestMappingInfo.getConsumesCondition();
        ProducesRequestCondition producesCondition = requestMappingInfo.getProducesCondition();
        HeadersRequestCondition headersCondition = requestMappingInfo.getHeadersCondition();

        Set<RequestMethod> methods = methodsCondition.getMethods();
        for (RequestMethod requestMethod : methods) {
            Operation operation = new Operation();
            operation.setName(method.getName());
            operation.setMethod(requestMethod.name());

//            operation.setPath();
        }


        MethodParameter returnType = handlerMethod.getReturnType();
        Type genericParameterType = returnType.getGenericParameterType();

        if (genericParameterType != null && genericParameterType instanceof ParameterizedType) {
            // 接口返回值包含泛型的Java类, 比如ResponseEntity<?>
            ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            System.out.println(parameterizedType);
        } else {
            // 接口返回普通Java类
            Class<?> objectType = (Class<?>) genericParameterType;
            System.out.println(objectType);
        }

        HandlerMethod resolvedFromHandlerMethod = handlerMethod.getResolvedFromHandlerMethod();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        return null;
    }

    private void buildControllers(Map<RequestMappingInfo, HandlerMethod> map) {
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
