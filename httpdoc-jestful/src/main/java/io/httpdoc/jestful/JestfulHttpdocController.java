package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.DefaultFormat;
import io.httpdoc.core.conversion.Format;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.core.serialization.Serializer;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.qfox.jestful.core.http.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Jestful Server 服务
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 16:23
 **/
@HTTP("/jestful")
public class JestfulHttpdocController {
    private Translator translator = new JestfulServerTranslator();
    private Provider provider = new SystemProvider();
    private Interpreter interpreter = new SourceInterpreter();
    private Converter converter = new StandardConverter();
    private Map<String, Serializer> serializers = new LinkedHashMap<>();
    private Format format = new DefaultFormat();

    {
        try {
            Set<URL> urls = Loader.load(JestfulHttpdocController.class.getClassLoader());
            for (URL url : urls) {
                if (url.getFile().endsWith("/serializer.properties")) {
                    Properties properties = new Properties();
                    properties.load(url.openStream());
                    if (properties.isEmpty()) continue;
                    Enumeration<Object> keys = properties.keys();
                    while (keys.hasMoreElements()) {
                        String name = (String) keys.nextElement();
                        String className = (String) properties.get(name);
                        Serializer serializer = Class.forName(className).asSubclass(Serializer.class).newInstance();
                        serializers.put(name, serializer);
                    }
                }
            }
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }


    @GET("/httpdoc")
    public void render(
            @Query("charset") String charset,
            @Query("contentType") String contentType,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, DocumentTranslationException {
        Container container = new JestfulWebContainer(request.getServletContext());
        Translation translation = new Translation(container, provider, interpreter);
        Document document = translator.translate(translation);
        response.setCharacterEncoding(charset);
        Serializer serializer = serializers.values().iterator().next();
        charset = charset != null && charset.trim().length() > 0 ? charset : "UTF-8";
        contentType = contentType != null && charset.trim().length() > 0 ? contentType : serializer.getType();
        response.setContentType(contentType + "; charset=" + charset);
        Map<String, Object> doc = converter.convert(document, format);
        serializer.serialize(doc, response.getOutputStream());
    }

    @GET("/httpdoc.{suffix:.*}")
    public void render(
            @Path("suffix") String suffix,
            @Query("charset") String charset,
            @Query("contentType") String contentType,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, DocumentTranslationException {
        Container container = new JestfulWebContainer(request.getServletContext());
        Translation translation = new Translation(container, provider, interpreter);
        Document document = translator.translate(translation);
        response.setCharacterEncoding(charset);
        Serializer serializer = serializers.get(suffix);
        if (serializer == null) throw new HttpdocRuntimeException("unknown serializer named " + suffix);
        charset = charset != null && charset.trim().length() > 0 ? charset : "UTF-8";
        contentType = contentType != null && charset.trim().length() > 0 ? contentType : serializer.getType();
        response.setContentType(contentType + "; charset=" + charset);
        Map<String, Object> doc = converter.convert(document, format);
        serializer.serialize(doc, response.getOutputStream());
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Map<String, Serializer> getSerializers() {
        return serializers;
    }

    public void setSerializers(Map<String, Serializer> serializers) {
        this.serializers = serializers;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
