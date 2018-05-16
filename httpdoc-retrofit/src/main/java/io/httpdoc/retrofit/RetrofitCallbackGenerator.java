package io.httpdoc.retrofit;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import java.util.List;

/**
 * Retrofit Callback 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-16 13:29
 **/
public class RetrofitCallbackGenerator extends RetrofitAbstractGenerator {

    public RetrofitCallbackGenerator() {
        this("", "");
    }

    public RetrofitCallbackGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    @Override
    protected void generate(String pkg, Provider provider, ClassFragment interfase, Document document, Controller controller, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(document, controller, operation, method);
        Result result = operation.getResult();
        method.setType(HDType.valueOf(void.class));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, provider, method, parameters);

        HDType type = result != null && result.getType() != null ? result.getType().toType(pkg, provider) : null;
        ParameterFragment callback = new ParameterFragment();
        callback.setType(new HDParameterizedType(HDType.valueOf(Callback.class), null, type != null ? type : HDType.valueOf(ResponseBody.class)));
        callback.setName("callback");
        method.getParameterFragments().add(callback);

        describe(operation, method, parameters);

        interfase.getMethodFragments().add(method);
    }

}
