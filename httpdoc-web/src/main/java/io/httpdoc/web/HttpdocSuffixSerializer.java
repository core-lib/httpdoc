package io.httpdoc.web;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.kit.LoadKit;
import io.httpdoc.core.serialization.Serializer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.*;

/**
 * 智能序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:07
 **/
public class HttpdocSuffixSerializer implements Serializer {
    private final Map<String, Serializer> map = new LinkedHashMap<>();

    HttpdocSuffixSerializer() {
        try {
            Set<URL> urls = LoadKit.load(HttpdocSuffixSerializer.class.getClassLoader());
            for (URL url : urls) {
                if (!url.getFile().endsWith("/serializer.properties")) continue;
                Properties properties = new Properties();
                properties.load(url.openStream());
                if (properties.isEmpty()) continue;
                Enumeration<Object> keys = properties.keys();
                while (keys.hasMoreElements()) {
                    String name = (String) keys.nextElement();
                    String className = (String) properties.get(name);
                    Serializer serializer = Class.forName(className).asSubclass(Serializer.class).newInstance();
                    map.put(name, serializer);
                }
            }
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
    }

    private Serializer get() {
        HttpServletRequest request = HttpdocThreadLocal.getRequest();
        String uri = request.getRequestURI();
        int index = uri.indexOf(".");
        if (index < 0) return map.values().iterator().next();
        String suffix = uri.substring(index + 1);
        Serializer serializer = map.get(suffix);
        if (serializer == null) throw new HttpdocRuntimeException("unknown serializer named " + suffix);
        return serializer;
    }

    @Override
    public String getName() {
        return get().getName();
    }

    @Override
    public String getType() {
        return get().getType();
    }

    @Override
    public void serialize(Map<String, Object> doc, OutputStream out) throws IOException {
        get().serialize(doc, out);
    }

    @Override
    public void serialize(Map<String, Object> doc, Writer writer) throws IOException {
        get().serialize(doc, writer);
    }
}
