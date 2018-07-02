package io.httpdoc.jestful;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;
import org.qfox.jestful.client.scheduler.Callback;

import java.util.List;

/**
 * Jestful Client 回调生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:32
 **/
public class JestfulClientCallbackGenerator extends JestfulClientAbstractGenerator {

    public JestfulClientCallbackGenerator() {
        super("", "");
    }

    public JestfulClientCallbackGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulClientCallbackGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulClientCallbackGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        method.setType(HDType.valueOf(void.class));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, provider, method, parameters);

        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, provider) : null;
        ParameterFragment callback = new ParameterFragment();
        callback.setType(new HDParameterizedType(HDType.valueOf(Callback.class), null, type != null ? type : HDType.valueOf(Entity.class)));
        callback.setName("callback");
        method.getParameterFragments().add(callback);

        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }

}
