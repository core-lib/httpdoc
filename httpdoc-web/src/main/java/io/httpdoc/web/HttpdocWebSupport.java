package io.httpdoc.web;

import io.httpdoc.core.Container;
import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.core.serialization.Serializer;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Httpdoc web 容器支持
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:26
 **/
public abstract class HttpdocWebSupport {
    private String charset = "UTF-8";
    private String contentType = null;
    private Translator translator = new HttpdocMergedTranslator();
    private Provider provider = new SystemProvider();
    private Interpreter interpreter = new SourceInterpreter();
    private Converter converter = new StandardConverter();
    private Serializer serializer = new HttpdocSuffixSerializer();

    public void init(HttpdocWebConfig config) throws ServletException {
        try {
            String charset = config.getInitParameter("charset");
            if (charset != null && charset.trim().length() > 0) {
                this.charset = charset;
            }
            String contentType = config.getInitParameter("contentType");
            if (contentType != null && contentType.trim().length() > 0) {
                this.contentType = contentType;
            }
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
            String converter = config.getInitParameter("converter");
            if (converter != null && converter.trim().length() > 0) {
                this.converter = Class.forName(interpreter).asSubclass(Converter.class).newInstance();
            }
            String serializer = config.getInitParameter("serializer");
            if (serializer != null && serializer.trim().length() > 0) {
                this.serializer = Class.forName(serializer).asSubclass(Serializer.class).newInstance();
            }
            String src = config.getInitParameter("src");
            if (src != null && src.trim().length() > 0) {
                System.setProperty("httpdoc.src.path", config.getServletContext().getRealPath(src));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void handle(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        try {
            HttpdocThreadLocal.bind((HttpServletRequest) request, (HttpServletResponse) response);
            Container container = new HttpdocWebContainer(request.getServletContext());
            Translation translation = new Translation(container, provider, interpreter);
            Document document = translator.translate(translation);
            response.setCharacterEncoding(charset);
            response.setContentType(contentType != null ? contentType : serializer.getType() + "; charset=" + charset);
            Map<String, Object> doc = converter.convert(document);
            serializer.serialize(doc, response.getOutputStream());
        } catch (DocumentTranslationException e) {
            throw new ServletException(e);
        } finally {
            HttpdocThreadLocal.clear();
        }
    }

    public void destroy() {
        System.getProperties().remove("httpdoc.src.path");
    }

}
