package io.httpdoc.core;

import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.provider.Provider;

/**
 * 翻译对象
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:35
 **/
public class Translation {
    private Context context;
    private Provider provider;
    private Interpreter interpreter;

    public Translation() {
    }

    public Translation(Context context, Provider provider, Interpreter interpreter) {
        this.context = context;
        this.provider = provider;
        this.interpreter = interpreter;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
}
