package io.httpdoc.core;

import javax.servlet.ServletContext;

/**
 * 翻译对象
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:35
 **/
public class Translation {
    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
