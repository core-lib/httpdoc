package io.httpdoc.spring.mvc;

import io.httpdoc.core.translation.Container;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * ServletContext 持有
 *
 * @author 钟宝林
 * @date 2018-05-14 11:20
 **/
public class ServletContextContainer implements Container {
    private final ServletContext servletContext;

    public ServletContextContainer(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
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
