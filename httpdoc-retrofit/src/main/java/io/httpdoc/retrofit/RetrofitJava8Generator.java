package io.httpdoc.retrofit;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.java8.Java8CallAdapterFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Retrofit Completable Future 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 17:40
 **/
public class RetrofitJava8Generator extends RetrofitAbstractGenerator {

    public RetrofitJava8Generator() {
        super("", "ForJava8");
    }

    public RetrofitJava8Generator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public RetrofitJava8Generator(Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(converterFactories);
    }

    public RetrofitJava8Generator(String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(prefix, suffix, converterFactories);
    }

    public RetrofitJava8Generator(Modeler modeler) {
        super(modeler);
    }

    public RetrofitJava8Generator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    public RetrofitJava8Generator(Modeler modeler, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, converterFactories);
    }

    public RetrofitJava8Generator(Modeler modeler, String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, prefix, suffix, converterFactories);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        Class<?> clazz;
        try {
            clazz = Class.forName("java.util.concurrent.CompletableFuture");
        } catch (ClassNotFoundException e) {
            throw new HttpdocRuntimeException("this generator can only used in jdk 8 or greater versions");
        }
        MethodFragment method = new MethodFragment(0);
        annotate(document, controller, operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, provider) : null;
        method.setType(new HDParameterizedType(HDType.valueOf(clazz), null, type != null ? type : HDType.valueOf(ResponseBody.class)));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, provider, method, parameters);

        describe(operation, method, parameters, result);

        interfase.getMethodFragments().add(method);
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        return Collections.<Class<? extends CallAdapter.Factory>>singleton(Java8CallAdapterFactory.class);
    }
}
