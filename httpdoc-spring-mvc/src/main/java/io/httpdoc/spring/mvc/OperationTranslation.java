package io.httpdoc.spring.mvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.translation.Translation;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

public class OperationTranslation extends SpringMVCTranslation {
    private final RequestMappingInfo mapping;
    private final HandlerMethod handler;
    private final Method method;
    private final Controller controller;

    public OperationTranslation(Translation translation, RequestMappingInfo mapping, HandlerMethod handler, Method method, Controller controller) {
        super(translation);
        this.mapping = mapping;
        this.handler = handler;
        this.method = method;
        this.controller = controller;
    }

    public RequestMappingInfo getMapping() {
        return mapping;
    }

    public HandlerMethod getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

    public Controller getController() {
        return controller;
    }
}
