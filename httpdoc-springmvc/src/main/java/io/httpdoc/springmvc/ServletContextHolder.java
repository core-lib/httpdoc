package io.httpdoc.springmvc;

import io.httpdoc.core.translation.Container;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * ServletContext 持有
 *
 * @author 钟宝林
 * @date 2018-05-14 11:20
 **/
public class ServletContextHolder implements Container {

    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
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
