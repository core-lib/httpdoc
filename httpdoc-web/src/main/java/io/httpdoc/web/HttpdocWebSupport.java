package io.httpdoc.web;

import io.httpdoc.core.Container;
import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.encode.DefaultEncoder;
import io.httpdoc.core.encode.Encoder;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.provider.Provider;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Httpdoc web 容器支持
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:26
 **/
public abstract class HttpdocWebSupport {

    public void handle(
            ServletRequest request,
            ServletResponse response,
            Translator translator,
            Provider provider,
            Interpreter interpreter
    ) throws IOException, ServletException {
        try {
            Container container = new HttpdocServletContainer(request.getServletContext());
            Translation translation = new Translation(container, provider, interpreter);
            Document document = translator.translate(translation);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            Encoder encoder = new DefaultEncoder();
            encoder.encode(document, response.getOutputStream());
        } catch (DocumentTranslationException e) {
            throw new ServletException(e);
        }
    }

}
