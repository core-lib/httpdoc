package io.httpdoc.jestful.client;

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
public class JestfulMergedGenerator extends JestfulAbstractGenerator {
    private final Map<Class<? extends JestfulAbstractGenerator>, JestfulAbstractGenerator> generators = new LinkedHashMap<>();

    public JestfulMergedGenerator() {
        this(Arrays.asList(new JestfulStandardGenerator(), new JestfulCallbackGenerator()));
    }

    public JestfulMergedGenerator(Collection<? extends JestfulAbstractGenerator> generators) {
        this(new SimpleModeler(), generators);
    }

    public JestfulMergedGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulMergedGenerator(Modeler modeler, Collection<? extends JestfulAbstractGenerator> generators) {
        super(modeler);
        if (generators == null) throw new NullPointerException();
        for (JestfulAbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Supplier supplier, ClassFragment interfase, Operation operation) {
        for (JestfulAbstractGenerator generator : generators.values()) generator.generate(pkg, pkgForced, supplier, interfase, operation);
    }

    public JestfulMergedGenerator include(Class<? extends JestfulAbstractGenerator> clazz) {
        try {
            return include(clazz.newInstance());
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }

    public JestfulMergedGenerator include(JestfulAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        generators.put(generator.getClass(), generator);
        return this;
    }

    public JestfulMergedGenerator exclude(Class<? extends JestfulAbstractGenerator> clazz) {
        if (clazz == null) throw new NullPointerException();
        generators.remove(clazz);
        return this;
    }

    public JestfulMergedGenerator exclude(JestfulAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        return exclude(generator.getClass());
    }

}
