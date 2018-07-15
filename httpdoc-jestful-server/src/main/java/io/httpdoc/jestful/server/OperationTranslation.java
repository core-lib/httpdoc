package io.httpdoc.jestful.server;

import io.httpdoc.core.Controller;
import org.qfox.jestful.core.Mapping;

import java.lang.reflect.Method;

public class OperationTranslation extends JestfulTranslation {
    private final Mapping mapping;
    private final Method method;
    private final Controller controller;

    public OperationTranslation(JestfulTranslation parent, Mapping mapping, Method method, Controller controller) {
        super(parent);
        this.mapping = mapping;
        this.method = method;
        this.controller = controller;
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
}
