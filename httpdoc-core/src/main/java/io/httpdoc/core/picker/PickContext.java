package io.httpdoc.core.picker;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import io.httpdoc.core.translation.Translation;

/**
 * 挑选上下文
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 11:23
 */
public class PickContext {
    private final Translation translation;
    private final Controller controller;
    private final Operation operation;

    public PickContext(Translation translation, Controller controller, Operation operation) {
        this.translation = translation;
        this.controller = controller;
        this.operation = operation;
    }

    public Translation getTranslation() {
        return translation;
    }

    public Controller getController() {
        return controller;
    }

    public Operation getOperation() {
        return operation;
    }
}
