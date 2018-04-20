package io.httpdoc.core;

import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.provider.Provider;

import javax.servlet.ServletContext;

/**
 * 翻译对象
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:35
 **/
public class Translation {
    private ServletContext servletContext;
    private Provider provider;
    private Interpreter interpreter;

    public Translation() {
    }

    public Translation(ServletContext servletContext, Provider provider, Interpreter interpreter) {
        this.servletContext = servletContext;
        this.provider = provider;
        this.interpreter = interpreter;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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
