package io.httpdoc.springmvc;

import io.httpdoc.core.translation.AbstractContainer;
import io.httpdoc.core.translation.Container;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * ServletContext 持有
 *
 * @author 钟宝林
 * @date 2018-05-14 11:20
 **/
public class SpringMVCHttpdocContainer extends AbstractContainer implements Container {
    private final ServletContext servletContext;

    SpringMVCHttpdocContainer(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public Object get(String name) {
        return servletContext.getAttribute(name);
    }

    @Override
    public Enumeration<String> names() {
        return servletContext.getAttributeNames();
    }

    @Override
    public void remove(String name) {
        servletContext.removeAttribute(name);
    }

    @Override
    public void set(String name, Object value) {
        servletContext.setAttribute(name, value);
    }
}
