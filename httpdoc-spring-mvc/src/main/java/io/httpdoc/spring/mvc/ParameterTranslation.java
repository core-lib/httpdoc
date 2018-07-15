package io.httpdoc.spring.mvc;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

public class ParameterTranslation extends SpringMVCTranslation {
    private final RequestMappingInfo mapping;
    private final HandlerMethod handler;
    private final Method method;
    private final Controller controller;
    private final Operation operation;

    public ParameterTranslation(SpringMVCTranslation parent, RequestMappingInfo mapping, HandlerMethod handler, Method method, Controller controller, Operation operation) {
        super(parent);
        this.mapping = mapping;
        this.handler = handler;
        this.method = method;
        this.controller = controller;
        this.operation = operation;
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

    public Operation getOperation() {
        return operation;
    }
}
