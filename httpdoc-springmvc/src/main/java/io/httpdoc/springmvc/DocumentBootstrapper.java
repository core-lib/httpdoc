package io.httpdoc.springmvc;

import io.httpdoc.core.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
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
        Type returnType = handlerMethod.getReturnType().getGenericParameterType();

        if (returnType != null && returnType instanceof ParameterizedType) {
            // 接口返回值包含泛型的Java类, 比如ResponseEntity<?>
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (rawType.equals(ResponseEntity.class)) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                    returnType = actualTypeArguments[0];
                }
            }
        }

        Schema resultSchema = Schema.valueOf(returnType);

        RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
        ParamsRequestCondition paramsCondition = requestMappingInfo.getParamsCondition();
        ConsumesRequestCondition consumesCondition = requestMappingInfo.getConsumesCondition();
        ProducesRequestCondition producesCondition = requestMappingInfo.getProducesCondition();
        HeadersRequestCondition headersCondition = requestMappingInfo.getHeadersCondition();
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

        Set<RequestMethod> methods = methodsCondition.getMethods();
        for (RequestMethod requestMethod : methods) {
            Set<String> patterns = patternsCondition.getPatterns();
            for (String pattern : patterns) {
                Operation operation = new Operation();
                operation.setName(handlerMethod.getMethod().getName());
                operation.setMethod(requestMethod.name());
                operation.setPath(pattern);
                Set<MediaTypeExpression> expressions = consumesCondition.getExpressions();
                operation.setConsumes(new ArrayList<String>());
                for (MediaTypeExpression expression : expressions) {
                    operation.getConsumes().add(expression.getMediaType().toString());
                }
                operation.setProduces(new ArrayList<String>());
                for (MediaTypeExpression expression : expressions) {
                    operation.getProduces().add(expression.getMediaType().toString());
                }
                Result result = new Result();
                result.setType(resultSchema);

                List<Parameter> parameters = buildParameters(handlerMethod);
                operation.setParameters(parameters);
            }
        }


        HandlerMethod resolvedFromHandlerMethod = handlerMethod.getResolvedFromHandlerMethod();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        return null;
    }

    private List<Parameter> buildParameters(HandlerMethod handlerMethod) {
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            String parameterName = methodParameter.getParameterName();
            System.out.println(parameterName);
        }
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
