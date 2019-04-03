package io.httpdoc.retrofit2;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.generation.OperationGenerateContext;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.util.*;

/**
 * Jestful Client 合并生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:42
 **/
public class Retrofit2MergedGenerator extends Retrofit2AbstractGenerator {
    private final Map<Class<? extends Retrofit2AbstractGenerator>, Retrofit2AbstractGenerator> generators = new LinkedHashMap<>();

    public Retrofit2MergedGenerator() {
        this(Collections.singleton(new Retrofit2CallGenerator()));
    }

    public Retrofit2MergedGenerator(Collection<? extends Retrofit2AbstractGenerator> generators) {
        this(new SimpleModeler(), generators);
    }

    public Retrofit2MergedGenerator(Modeler<ClassFragment> modeler) {
        this(modeler, Collections.singleton(new Retrofit2CallGenerator()));
    }

    public Retrofit2MergedGenerator(Modeler<ClassFragment> modeler, Collection<? extends Retrofit2AbstractGenerator> generators) {
        super(modeler);
        if (generators == null) throw new NullPointerException();
        for (Retrofit2AbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        Collection<MethodFragment> fragments = new LinkedHashSet<>();
        for (Retrofit2AbstractGenerator generator : generators.values()) fragments.addAll(generator.generate(context));
        return fragments;
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        Set<Class<? extends CallAdapter.Factory>> callAdapterFactories = new LinkedHashSet<>();
        for (Retrofit2AbstractGenerator generator : generators.values()) callAdapterFactories.addAll(generator.getCallAdapterFactories());
        return callAdapterFactories;
    }

    public Retrofit2MergedGenerator include(Class<? extends Retrofit2AbstractGenerator> clazz) {
        try {
            return include(clazz.newInstance());
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }

    public Retrofit2MergedGenerator include(Retrofit2AbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        generators.put(generator.getClass(), generator);
        return this;
    }

    public Retrofit2MergedGenerator exclude(Class<? extends Retrofit2AbstractGenerator> clazz) {
        if (clazz == null) throw new NullPointerException();
        generators.remove(clazz);
        return this;
    }

    public Retrofit2MergedGenerator exclude(Retrofit2AbstractGenerator generator) {
        if (generator == null) throw new NullPointerException();
        return exclude(generator.getClass());
    }

    public Retrofit2MergedGenerator add(Class<? extends Converter.Factory> converterFactory) {
        converterFactories.add(converterFactory);
        return this;
    }

    public Retrofit2MergedGenerator remove(Class<? extends Converter.Factory> converterFactory) {
        converterFactories.remove(converterFactory);
        return this;
    }

}
