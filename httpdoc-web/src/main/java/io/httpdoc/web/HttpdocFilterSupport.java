package io.httpdoc.web;

import io.httpdoc.core.Translator;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.provider.SystemProvider;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:13
 **/
public class HttpdocFilterSupport extends HttpdocWebSupport implements Filter {
    private Translator translator = new SmartTranslator();
    private Provider provider = new SystemProvider();
    private Interpreter interpreter = new SourceInterpreter();

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            String translator = config.getInitParameter("translator");
            if (translator != null && translator.trim().length() > 0) {
                this.translator = Class.forName(translator).asSubclass(Translator.class).newInstance();
            }
            String provider = config.getInitParameter("provider");
            if (provider != null && provider.trim().length() > 0) {
                this.provider = Class.forName(provider).asSubclass(Provider.class).newInstance();
            }
            String interpreter = config.getInitParameter("interpreter");
            if (interpreter != null && interpreter.trim().length() > 0) {
                this.interpreter = Class.forName(interpreter).asSubclass(Interpreter.class).newInstance();
            }
            String src = config.getInitParameter("src");
            if (src != null && src.trim().length() > 0) {
                System.setProperty("httpdoc.src.path", config.getServletContext().getRealPath(src));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        handle(request, response, translator, provider, interpreter);
    }

    @Override
    public void destroy() {
        System.getProperties().remove("httpdoc.src.path");
    }

}
