package io.httpdoc.jestful;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;

import java.util.List;

/**
 * Retrofit Completable Future 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 17:40
 **/
public class JestfulClientJava8Generator extends JestfulClientAbstractGenerator {

    public JestfulClientJava8Generator() {
        super("", "ForJava8");
    }

    public JestfulClientJava8Generator(Modeler modeler) {
        super(modeler);
    }

    public JestfulClientJava8Generator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulClientJava8Generator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        Class<?> clazz;
        try {
            clazz = Class.forName("java.util.concurrent.CompletableFuture");
        } catch (ClassNotFoundException e) {
            throw new HttpdocRuntimeException("this generator can only used in jdk 8 or greater versions");
        }
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, supplier) : null;
        method.setType(new HDParameterizedType(HDType.valueOf(clazz), null, type != null ? type : HDType.valueOf(Entity.class)));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, supplier, method, parameters);

        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }

}
