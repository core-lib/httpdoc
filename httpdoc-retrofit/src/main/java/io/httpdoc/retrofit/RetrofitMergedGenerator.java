package io.httpdoc.retrofit;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Operation;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import io.httpdoc.core.provider.Provider;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.util.*;

/**
 * Jestful Client 合并生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:42
 **/
public class RetrofitMergedGenerator extends RetrofitAbstractGenerator {
    private final Map<Class<? extends RetrofitAbstractGenerator>, RetrofitAbstractGenerator> generators = new LinkedHashMap<>();

    public RetrofitMergedGenerator() {
        this(Collections.singleton(new RetrofitCallGenerator()));
    }

    public RetrofitMergedGenerator(Collection<? extends RetrofitAbstractGenerator> generators) {
        this(new SimpleModeler(), generators);
    }

    public RetrofitMergedGenerator(Modeler modeler) {
        this(modeler, Collections.singleton(new RetrofitCallGenerator()));
    }

    public RetrofitMergedGenerator(Modeler modeler, Collection<? extends RetrofitAbstractGenerator> generators) {
        super(modeler);
        if (generators == null) throw new NullPointerException();
        for (RetrofitAbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        for (RetrofitAbstractGenerator generator : generators.values()) generator.generate(pkg, pkgForced, provider, interfase, document, controller, operation);
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        Set<Class<? extends CallAdapter.Factory>> callAdapterFactories = new LinkedHashSet<>();
        for (RetrofitAbstractGenerator generator : generators.values()) callAdapterFactories.addAll(generator.getCallAdapterFactories());
        return callAdapterFactories;
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

    public RetrofitMergedGenerator add(Class<? extends Converter.Factory> converterFactory) {
        converterFactories.add(converterFactory);
        return this;
    }

    public RetrofitMergedGenerator remove(Class<? extends Converter.Factory> converterFactory) {
        converterFactories.remove(converterFactory);
        return this;
    }

}
