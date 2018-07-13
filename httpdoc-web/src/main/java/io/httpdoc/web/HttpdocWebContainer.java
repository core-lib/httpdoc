package io.httpdoc.web;

import io.httpdoc.core.translation.AbstractContainer;
import io.httpdoc.core.translation.Container;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Httpdoc Servlet Container
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:14
 **/
public class HttpdocWebContainer extends AbstractContainer implements Container {
    private final ServletContext servletContext;

    HttpdocWebContainer(ServletContext servletContext) {
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
