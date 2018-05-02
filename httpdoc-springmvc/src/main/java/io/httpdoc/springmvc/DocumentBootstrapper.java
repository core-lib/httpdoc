package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Operation;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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

        List<Controller> controllers = new LinkedList<>();
        document.setControllers(controllers);
        for (ControllerInfoHolder controllerInfoHolder : controllerInfoHolders) {
            if (controllerInfoHolder.isHandled()) {
                continue;
            }
            controllerInfoHolder.setHandled(true);
            Controller controller = new Controller();

            HandlerMethod handlerMethod = controllerInfoHolder.getHandlerMethod();
            Method method = handlerMethod.getMethod();
            controller.setName(method.getName());

            RequestMappingInfo requestMappingInfo = controllerInfoHolder.getRequestMappingInfo();

            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            // SpringMVC 一个接口只会有一个操作注解
            Operation operation = new Operation();
//            operation.setMethod(methodsCondition.);

            ParamsRequestCondition paramsCondition = requestMappingInfo.getParamsCondition();
            ConsumesRequestCondition consumesCondition = requestMappingInfo.getConsumesCondition();
            Set<MediaType> consumableMediaTypes = consumesCondition.getConsumableMediaTypes();
            List<String> consumes = new LinkedList<>();
            for (MediaType consumableMediaType : consumableMediaTypes) {
                consumes.add(consumableMediaType.toString());
            }
            controller.setConsumes(consumes);
            ProducesRequestCondition producesCondition = requestMappingInfo.getProducesCondition();
            Set<MediaTypeExpression> expressions = producesCondition.getExpressions();
            List<String> produces = new LinkedList<>();
            for (MediaTypeExpression expression : expressions) {
                produces.add(expression.getMediaType().toString());
            }
            controller.setProduces(produces);
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
            Set<String> patterns = patternsCondition.getPatterns();
            if (patterns != null && patterns.size() > 0) {
                controller.setPath(patterns.iterator().next());
            }
            HeadersRequestCondition headersCondition = requestMappingInfo.getHeadersCondition();
            RequestCondition<?> customCondition = requestMappingInfo.getCustomCondition();

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

            System.out.println("hh");
        }
        return document;
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
