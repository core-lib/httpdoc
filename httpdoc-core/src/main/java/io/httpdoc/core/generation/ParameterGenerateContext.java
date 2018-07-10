package io.httpdoc.core.generation;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;

import java.util.List;

/**
 * Parameter生成上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 11:23
 **/
public class ParameterGenerateContext extends OperationGenerateContext {
    private final List<Parameter> parameters;

    public ParameterGenerateContext(Generation generation, Controller controller, Operation operation, List<Parameter> parameters) {
        super(generation, controller, operation);
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
