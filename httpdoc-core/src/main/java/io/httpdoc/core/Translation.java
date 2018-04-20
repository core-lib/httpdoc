package io.httpdoc.core;

import io.httpdoc.core.description.Describer;
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
    private Describer describer;

    public Translation() {
    }

    public Translation(ServletContext servletContext, Provider provider, Describer describer) {
        this.servletContext = servletContext;
        this.provider = provider;
        this.describer = describer;
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

    public Describer getDescriber() {
        return describer;
    }

    public void setDescriber(Describer describer) {
        this.describer = describer;
    }
}
