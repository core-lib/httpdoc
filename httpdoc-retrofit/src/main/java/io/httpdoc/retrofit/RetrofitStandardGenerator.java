package io.httpdoc.retrofit;

import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;

import java.util.List;

/**
 * Jestful Client 标准生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:29
 **/
public class RetrofitStandardGenerator extends RetrofitAbstractGenerator {

    public RetrofitStandardGenerator() {
        this("", "");
    }

    public RetrofitStandardGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    @Override
    protected void generate(String pkg, Provider provider, ClassFragment interfase, Operation operation) {
        MethodFragment method = new MethodFragment(0);
        annotate(operation, method);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().toType(pkg, provider) : HDType.valueOf(ResponseBody.class);
        method.setType(type);
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) generate(pkg, provider, method, parameters);

        describe(operation, method, parameters);

        interfase.getMethodFragments().add(method);
    }

}
