package io.httpdoc.web;

import io.httpdoc.core.Context;
import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.encode.DefaultEncoder;
import io.httpdoc.core.encode.Encoder;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.provider.DefaultProvider;
import io.httpdoc.core.provider.Provider;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:13
 **/
public class HttpdocFilterSupport implements Filter {
    private Translator translator = new DefaultTranslator();

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            ServletContext context = config.getServletContext();
            String path = context.getResource("").getPath();
            System.setProperty("java.src.path", path);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Context context = new HttpdocServletContext(request.getServletContext());
            Provider provider = new DefaultProvider();
            Interpreter interpreter = new SourceInterpreter();
            Translation translation = new Translation(context, provider, interpreter);
            Document document = translator.translate(translation);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            Encoder encoder = new DefaultEncoder();
            encoder.encode(document, response.getOutputStream());
        } catch (DocumentTranslationException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
