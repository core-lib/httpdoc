package io.httpdoc.objective.c.bridge;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;

import java.util.List;

/**
 * Objective-C Operation
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 21:04
 **/
public class ObjCOperation extends Operation {
    private final Operation operation;

    public ObjCOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public List<Parameter> getParameters() {
        return super.getParameters();
    }

    @Override
    public Result getResult() {
        return super.getResult();
    }
}
