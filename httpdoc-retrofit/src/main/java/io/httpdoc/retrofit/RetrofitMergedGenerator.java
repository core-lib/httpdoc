package io.httpdoc.retrofit;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Operation;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.provider.Provider;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
        if (generators == null) throw new NullPointerException();
        for (RetrofitAbstractGenerator generator : generators) include(generator);
    }

    @Override
    protected void generate(String pkg, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        for (RetrofitAbstractGenerator generator : generators.values()) generator.generate(pkg, provider, interfase, document, controller, operation);
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
