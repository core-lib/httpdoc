package io.httpdoc.core.generation;

import io.httpdoc.core.Controller;

/**
 * Controller生成上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 10:43
 **/
public class ControllerGenerateContext extends GenerateContext {
    private final Controller controller;

    public ControllerGenerateContext(Generation generation, Controller controller) {
        super(generation);
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

}
