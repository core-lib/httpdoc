package io.httpdoc.jestful.client;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Header;

import java.util.List;

/**
 * Jestful Client 报头 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:39
 **/
public class JestfulHeaderGenerator extends JestfulAbstractGenerator {

    public JestfulHeaderGenerator() {
        super("", "ForHeader");
    }

    public JestfulHeaderGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulHeaderGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulHeaderGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        method.setType(HDType.valueOf(Header.class));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, supplier, method, parameters);

        Result result = operation.getResult();
        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }
}
