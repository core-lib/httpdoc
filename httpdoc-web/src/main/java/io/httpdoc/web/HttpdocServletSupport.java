package io.httpdoc.web;

import javax.servlet.*;
import java.io.IOException;

/**
 * Httpdoc Servlet 支持
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 11:21
 **/
public class HttpdocServletSupport extends HttpdocWebSupport implements Servlet {
    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(new HttpdocServletConfig(this.config = config));
    }

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.handle(req, res);
    }

    @Override
    public String getServletInfo() {
        return config.getServletName();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
