package io.httpdoc.objective.c.bridge;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;

import java.util.List;

/**
 * Objective-C Controller
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 21:03
 **/
public class ObjCController extends Controller {
    private final Controller controller;

    public ObjCController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public List<Operation> getOperations() {
        return controller.getOperations();
    }
}
