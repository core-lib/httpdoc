package io.httpdoc.springmvc;

import io.httpdoc.core.*;
import io.httpdoc.core.interpretation.ClassInterpretation;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * SpringMVC Controller 翻译器
 *
 * @author 钟宝林
 * @date 2018-05-11 16:59
 **/
@Component
public class DefaultControllerTranslator implements ControllerTranslator {

    /**
     * 解析接口时需要忽略的参数列表
     */
    private static Set<Class<?>> ignoredParameters;

    static {
        ignoredParameters = new HashSet<>();
        ignoredParameters.add(ServletRequest.class);
        ignoredParameters.add(Class.class);
        ignoredParameters.add(Void.class);
        ignoredParameters.add(Void.TYPE);
        ignoredParameters.add(ServletResponse.class);
        ignoredParameters.add(HttpServletRequest.class);
        ignoredParameters.add(HttpServletResponse.class);
        ignoredParameters.add(HttpSession.class);
        ignoredParameters.add(HttpHeaders.class);
        ignoredParameters.add(BindingResult.class);
        ignoredParameters.add(ServletContext.class);
        ignoredParameters.add(UriComponentsBuilder.class);
    }

    private Interpreter interpreter;

    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Set<Controller> translator(TranslateContext translateContext) {
        List<ControllerInfoHolder> controllerInfoHolders = translateContext.getControllerInfoHolders();
        this.interpreter = translateContext.getInterpreter();

        Map<Class<?>, Controller> controllerMap = new LinkedHashMap<>();

        Set<Controller> controllers = new LinkedHashSet<>();
        for (ControllerInfoHolder controllerInfoHolder : controllerInfoHolders) {
            if (controllerInfoHolder.isHandled()) {
                controllers.add(controllerInfoHolder.getController());
                continue;
            }
            HandlerMethod handlerMethod = controllerInfoHolder.getHandlerMethod();

            Class<?> beanType = handlerMethod.getBeanType();
            Controller controller = controllerMap.get(beanType);
            if (controller == null) {
                controller = new Controller();
                controller.setName(beanType.getSimpleName());
                controllerMap.put(beanType, controller);
                controllerInfoHolder.setController(controller);
                controllers.add(controller);
                if (interpreter != null) {
                    ClassInterpretation interpret = interpreter.interpret(beanType);
                    controller.setDescription(interpret == null ? null : interpret.getText());
                }
                controllerInfoHolder.setHandled(true);
            }

            RequestMappingInfo requestMappingInfo = controllerInfoHolder.getRequestMappingInfo();

            List<Operation> operations = buildOperations(requestMappingInfo, handlerMethod);
            controller.getOperations().addAll(operations);
        }
        return controllers;
    }

    private List<Operation> buildOperations(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        List<Operation> operations = new ArrayList<>();

        Type returnType = getReturnType(handlerMethod);

        Schema resultSchema = Schema.valueOf(returnType);

        RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
        // ParamsRequestCondition paramsCondition = requestMappingInfo.getParamsCondition(); TODO 参数条件暂时不支持
        ConsumesRequestCondition consumesCondition = requestMappingInfo.getConsumesCondition();
        ProducesRequestCondition producesCondition = requestMappingInfo.getProducesCondition();
        // HeadersRequestCondition headersCondition = requestMappingInfo.getHeadersCondition(); TODO 请求头条件暂时不支持
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

        Set<RequestMethod> methods = methodsCondition.getMethods();
        if (methods == null || methods.size() <= 0) {
            // 如果没写请求方法, 默认为所有请求方法
            methods = new HashSet<>(Arrays.asList(RequestMethod.values()));
        }
        for (RequestMethod requestMethod : methods) {
            Set<String> patterns = patternsCondition.getPatterns();
            for (String pattern : patterns) {
                Operation operation = new Operation();
                operation.setName(handlerMethod.getMethod().getName());
                operation.setMethod(requestMethod.name());
                operation.setPath(pattern);
                operation.setConsumes(new ArrayList<String>());
                if (interpreter != null) {
                    MethodInterpretation interpret = interpreter.interpret(handlerMethod.getMethod());
                    operation.setDescription(interpret == null ? null : interpret.getText());
                }
                for (MediaTypeExpression expression : consumesCondition.getExpressions()) {
                    operation.getConsumes().add(expression.getMediaType().toString());
                }
                operation.setProduces(new ArrayList<String>());
                for (MediaTypeExpression expression : producesCondition.getExpressions()) {
                    operation.getProduces().add(expression.getMediaType().toString());
                }
                Result result = new Result();
                result.setType(resultSchema);
                operation.setResult(result);

                List<Parameter> parameters = buildParameters(Operation.HttpMethod.resolve(operation.getMethod()), handlerMethod);
                operation.setParameters(parameters);

                operations.add(operation);
            }
        }

        return operations;
    }

    /**
     * 获取接口方法的返回值类型, 如果是ResponseEntity则取泛型内的类型
     *
     * @param handlerMethod 接口方法
     * @return 返回值类型
     */
    private Type getReturnType(HandlerMethod handlerMethod) {
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
        return returnType;
    }

    /**
     * 根据一个接口方法封装接口文档的参数
     *
     * @param httpMethod    HTTP方法
     * @param handlerMethod 接口方法
     * @return 参数列表
     */
    private List<Parameter> buildParameters(Operation.HttpMethod httpMethod, HandlerMethod handlerMethod) {
        List<Parameter> parameters = new LinkedList<>();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());

        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = methodParameters[i];
            Class<?> parameterType = methodParameter.getParameterType();
            if (parameterType.isInterface() || ignoredParameters.contains(parameterType)) {
                continue;
            }
            Parameter parameter = new Parameter();
            parameter.setName(parameterNames[i]);
            parameter.setType(Schema.valueOf(methodParameter.getParameterType()));
            parameter.setScope(getScope(httpMethod, methodParameter));
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 获取接口参数的http位置
     *
     * @param methodParameter 接口参数
     * @param httpMethod      HTTP方法
     * @return http位置
     */
    private String getScope(Operation.HttpMethod httpMethod, MethodParameter methodParameter) {
        // cookie < header < path < query < body
        if (methodParameter.hasMethodAnnotation(RequestBody.class)) {
            return Parameter.HTTP_PARAM_SCOPE_BODY;
        }
        if (methodParameter.hasMethodAnnotation(RequestParam.class)) {
            return Parameter.HTTP_PARAM_SCOPE_QUERY;
        }
        if (methodParameter.hasMethodAnnotation(PathVariable.class)) {
            return Parameter.HTTP_PARAM_SCOPE_PATH;
        }
        if (methodParameter.hasMethodAnnotation(RequestHeader.class)) {
            return Parameter.HTTP_PARAM_SCOPE_HEADER;
        }
        if (methodParameter.hasMethodAnnotation(CookieValue.class)) {
            return Parameter.HTTP_PARAM_SCOPE_COOKIE;
        }
        return httpMethod.isRequiresRequestBody() ? Parameter.HTTP_PARAM_SCOPE_BODY : Parameter.HTTP_PARAM_SCOPE_QUERY;
    }
}
