package io.httpdoc.jestful;

import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.description.Describer;
import io.httpdoc.core.description.SourceDescriber;
import io.httpdoc.core.encode.DefaultEncoder;
import io.httpdoc.core.encode.Encoder;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.provider.DefaultProvider;
import io.httpdoc.core.provider.Provider;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:13
 **/
public class HttpdocFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletContext servletContext = request.getServletContext();
        Provider provider = new DefaultProvider();
        Describer describer = new SourceDescriber();
        Translator translator = new JestfulServerTranslator();
        try {
            Document document = translator.translate(new Translation(servletContext, provider, describer));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            Encoder encoder = new DefaultEncoder();
            encoder.encode(document, response.getOutputStream());
        } catch (DocumentTranslationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
