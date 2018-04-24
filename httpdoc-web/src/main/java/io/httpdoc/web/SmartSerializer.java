package io.httpdoc.web;

import io.httpdoc.core.Loader;
import io.httpdoc.core.serialization.Serializer;
import io.httpdoc.web.exception.UnknownSerializerException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 智能序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:07
 **/
public class SmartSerializer implements Serializer {
    private final Serializer serializer;

    SmartSerializer() {
        try {
            Set<URL> urls = Loader.load(this.getClass().getClassLoader());
            for (URL url : urls) {
                if (!url.getFile().endsWith("/serializer.properties")) continue;
                Properties properties = new Properties();
                properties.load(url.openStream());
                if (properties.isEmpty()) continue;
                String className = (String) properties.values().iterator().next();
                serializer = Class.forName(className).asSubclass(Serializer.class).newInstance();
                return;
            }
            throw new UnknownSerializerException("could not find any serializer");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return serializer.getName();
    }

    @Override
    public String getType() {
        return serializer.getType();
    }

    @Override
    public void serialize(Map<String, Object> doc, OutputStream out) throws IOException {
        serializer.serialize(doc, out);
    }

    @Override
    public void serialize(Map<String, Object> doc, Writer writer) throws IOException {
        serializer.serialize(doc, writer);
    }
}
