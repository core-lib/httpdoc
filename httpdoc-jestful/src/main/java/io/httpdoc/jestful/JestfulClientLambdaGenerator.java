package io.httpdoc.jestful;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;
import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;

import java.util.List;

/**
 * Jestful Client 回调生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:32
 **/
public class JestfulClientLambdaGenerator extends JestfulClientAbstractGenerator {

    public JestfulClientLambdaGenerator() {
        super("", "");
    }

    public JestfulClientLambdaGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulClientLambdaGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulClientLambdaGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        method.setType(HDType.valueOf(void.class));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, supplier, method, parameters);

        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, supplier) : null;

        {
            ParameterFragment onSuccess = new ParameterFragment();
            onSuccess.setType(new HDParameterizedType(HDType.valueOf(OnSuccess.class), null, type != null ? type : HDType.valueOf(Entity.class)));
            onSuccess.setName("onSuccess");
            method.getParameterFragments().add(onSuccess);
        }

        {
            ParameterFragment onFail = new ParameterFragment();
            onFail.setType(HDType.valueOf(OnFail.class));
            onFail.setName("onFail");
            method.getParameterFragments().add(onFail);
        }

        {
            ParameterFragment onCompleted = new ParameterFragment();
            onCompleted.setType(new HDParameterizedType(HDType.valueOf(OnCompleted.class), null, type != null ? type : HDType.valueOf(Entity.class)));
            onCompleted.setName("onCompleted");
            method.getParameterFragments().add(onCompleted);
        }

        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }

}
