package io.httpdoc.retrofit;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.generation.OperationGenerateContext;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;

import java.util.*;

/**
 * Retrofit Client 合并生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:42
 **/
public class RetrofitMergedGenerator extends RetrofitAbstractGenerator {
    private final Map<Class<? extends RetrofitAbstractGenerator>, RetrofitAbstractGenerator> generators = new LinkedHashMap<>();

    public RetrofitMergedGenerator() {
        this(Collections.singleton(new RetrofitStandardGenerator()));
    }

    public RetrofitMergedGenerator(Collection<? extends RetrofitAbstractGenerator> generators) {
        this(new SimpleModeler(), generators);
    }

    public RetrofitMergedGenerator(Modeler<ClassFragment> modeler) {
        this(modeler, Collections.singleton(new RetrofitStandardGenerator()));
    }

    public RetrofitMergedGenerator(Modeler<ClassFragment> modeler, Collection<? extends RetrofitAbstractGenerator> generators) {
        super(modeler);
        if (generators == null) throw new NullPointerException();
        for (RetrofitAbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        Collection<MethodFragment> fragments = new LinkedHashSet<>();
        for (RetrofitAbstractGenerator generator : generators.values()) fragments.addAll(generator.generate(context));
        return fragments;
    }

    public RetrofitMergedGenerator include(Class<? extends RetrofitAbstractGenerator> clazz) {
        try {
            return include(clazz.newInstance());
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }

    public RetrofitMergedGenerator include(RetrofitAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        generators.put(generator.getClass(), generator);
        return this;
    }

    public RetrofitMergedGenerator exclude(Class<? extends RetrofitAbstractGenerator> clazz) {
        if (clazz == null) throw new NullPointerException();
        generators.remove(clazz);
        return this;
    }

    public RetrofitMergedGenerator exclude(RetrofitAbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        return exclude(generator.getClass());
    }

}
