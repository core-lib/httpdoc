package io.httpdoc.jestful.client;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Message;

import java.util.List;

/**
 * Jestful Client 标准生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:29
 **/
public class JestfulStandardGenerator extends JestfulAbstractGenerator {

    public JestfulStandardGenerator() {
        super("", "");
    }

    public JestfulStandardGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulStandardGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulStandardGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, supplier) : null;
        method.setType(type != null ? type : HDType.valueOf(Message.class));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, supplier, method, parameters);

        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }

}
