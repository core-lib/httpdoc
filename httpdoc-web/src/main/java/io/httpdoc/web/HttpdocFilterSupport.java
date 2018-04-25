package io.httpdoc.web;

import javax.servlet.*;
import java.io.IOException;

/**
 * Httpdoc Filter 支持
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:13
 **/
public class HttpdocFilterSupport extends HttpdocWebSupport implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(new HttpdocFilterConfig(config));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.handle(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
