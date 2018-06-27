package io.httpdoc.retrofit;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Jestful Client Observable 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:39
 **/
public class RetrofitRxJavaGenerator extends RetrofitAbstractGenerator {

    public RetrofitRxJavaGenerator() {
        super("", "ForRxJava");
    }

    public RetrofitRxJavaGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public RetrofitRxJavaGenerator(Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(converterFactories);
    }

    public RetrofitRxJavaGenerator(String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(prefix, suffix, converterFactories);
    }

    public RetrofitRxJavaGenerator(Modeler modeler) {
        super(modeler);
    }

    public RetrofitRxJavaGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    public RetrofitRxJavaGenerator(Modeler modeler, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, converterFactories);
    }

    public RetrofitRxJavaGenerator(Modeler modeler, String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, prefix, suffix, converterFactories);
    }

    @Override
    protected void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(document, controller, operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, provider) : null;
        method.setType(new HDParameterizedType(HDType.valueOf(Observable.class), null, type != null ? type : HDType.valueOf(ResponseBody.class)));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, pkgForced, provider, method, parameters);

        describe(operation, method, parameters);

        interfase.getMethodFragments().add(method);
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        return Collections.<Class<? extends CallAdapter.Factory>>singleton(RxJava2CallAdapterFactory.class);
    }

}
