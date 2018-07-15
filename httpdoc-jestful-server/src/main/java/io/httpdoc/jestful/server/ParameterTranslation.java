package io.httpdoc.jestful.server;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import org.qfox.jestful.core.Mapping;

import java.lang.reflect.Method;

public class ParameterTranslation extends JestfulTranslation {
    private final Mapping mapping;
    private final Method method;
    private final Controller controller;
    private final Operation operation;

    public ParameterTranslation(JestfulTranslation parent, Mapping mapping, Method method, Controller controller, Operation operation) {
        super(parent);
        this.mapping = mapping;
        this.method = method;
        this.controller = controller;
        this.operation = operation;
    }

    public Mapping getMapping() {
        return mapping;
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
