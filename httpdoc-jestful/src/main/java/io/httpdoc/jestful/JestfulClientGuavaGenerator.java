package io.httpdoc.jestful;

import com.google.common.util.concurrent.ListenableFuture;
import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;

import java.util.List;

/**
 * Retrofit Listenable Future 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 17:40
 **/
public class JestfulClientGuavaGenerator extends JestfulClientAbstractGenerator {

    public JestfulClientGuavaGenerator() {
        super("", "ForGuava");
    }

    public JestfulClientGuavaGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulClientGuavaGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulClientGuavaGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, Provider provider, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, provider) : null;
        method.setType(new HDParameterizedType(HDType.valueOf(ListenableFuture.class), null, type != null ? type : HDType.valueOf(Entity.class)));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, provider, method, parameters);

        describe(operation, method, parameters);

        interfase.getMethodFragments().add(method);
    }

}
