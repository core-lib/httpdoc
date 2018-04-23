package io.httpdoc.web;

import io.httpdoc.core.Context;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Httpdoc Servlet Context
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:14
 **/
public class HttpdocServletContext implements Context {
    private final ServletContext context;

    HttpdocServletContext(ServletContext context) {
        this.context = context;
    }

    @Override
    public Object get(String name) {
        return context.getAttribute(name);
    }

    @Override
    public Enumeration<String> names() {
        return context.getAttributeNames();
    }

    @Override
    public void remove(String name) {
        context.removeAttribute(name);
    }

    @Override
    public void set(String name, Object value) {
        context.setAttribute(name, value);
    }
}
