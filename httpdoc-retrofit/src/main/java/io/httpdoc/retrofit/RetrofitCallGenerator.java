package io.httpdoc.retrofit;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Jestful Client Future 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:39
 **/
public class RetrofitCallGenerator extends RetrofitAbstractGenerator {

    public RetrofitCallGenerator() {
        super("", "");
    }

    public RetrofitCallGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public RetrofitCallGenerator(Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(converterFactories);
    }

    public RetrofitCallGenerator(String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(prefix, suffix, converterFactories);
    }

    public RetrofitCallGenerator(Modeler modeler) {
        super(modeler);
    }

    public RetrofitCallGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    public RetrofitCallGenerator(Modeler modeler, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, converterFactories);
    }

    public RetrofitCallGenerator(Modeler modeler, String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, prefix, suffix, converterFactories);
    }

    @Override
    protected void generate(String pkg, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(document, controller, operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, provider) : null;
        method.setType(new HDParameterizedType(HDType.valueOf(Call.class), null, type != null ? type : HDType.valueOf(ResponseBody.class)));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, provider, method, parameters);

        describe(operation, method, parameters);

        interfase.getMethodFragments().add(method);
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        return Collections.emptySet();
    }
}
