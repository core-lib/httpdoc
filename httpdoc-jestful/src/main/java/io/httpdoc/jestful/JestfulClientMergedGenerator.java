package io.httpdoc.jestful;

import io.httpdoc.core.Operation;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import io.httpdoc.core.supplier.Supplier;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jestful Client 合并生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:42
 **/
public class JestfulClientMergedGenerator extends JestfulClientAbstractGenerator {
    private final Map<Class<? extends JestfulClientAbstractGenerator>, JestfulClientAbstractGenerator> generators = new LinkedHashMap<>();

    public JestfulClientMergedGenerator() {
        this(Arrays.asList(new JestfulClientStandardGenerator(), new JestfulClientCallbackGenerator()));
    }

    public JestfulClientMergedGenerator(Collection<? extends JestfulClientAbstractGenerator> generators) {
        this(new SimpleModeler(), generators);
    }

    public JestfulClientMergedGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulClientMergedGenerator(Modeler modeler, Collection<? extends JestfulClientAbstractGenerator> generators) {
        super(modeler);
        if (generators == null) throw new NullPointerException();
        for (JestfulClientAbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        for (JestfulClientAbstractGenerator generator : generators.values()) generator.generate(pkg, pkgForced, supplier, interfase, operation);
    }

    public JestfulClientMergedGenerator include(Class<? extends JestfulClientAbstractGenerator> clazz) {
        try {
            return include(clazz.newInstance());
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }

    public JestfulClientMergedGenerator include(JestfulClientAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        generators.put(generator.getClass(), generator);
        return this;
    }

    public JestfulClientMergedGenerator exclude(Class<? extends JestfulClientAbstractGenerator> clazz) {
        if (clazz == null) throw new NullPointerException();
        generators.remove(clazz);
        return this;
    }

    public JestfulClientMergedGenerator exclude(JestfulClientAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        return exclude(generator.getClass());
    }

}
