package io.httpdoc.core;

import io.httpdoc.core.kit.IOKit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yangchangpei on 17/3/28.
 */
public abstract class Config {
    private static ConcurrentMap<String, Properties> cache = new ConcurrentHashMap<>();

    protected Config() {
        Class<?> clazz = this.getClass();
        Properties properties = load(clazz);

        for (String name : properties.stringPropertyNames()) {
            Class<?> c = clazz;
            while (c != Object.class) {
                try {
                    Field field = c.getDeclaredField(name);
                    if (Modifier.isFinal(field.getModifiers())) break;
                    if (field.getType() != String.class) break;
                    field.setAccessible(true);
                    field.set(this, properties.getProperty(name));
                    break;
                } catch (NoSuchFieldException e) {
                    c = c.getSuperclass();
                } catch (IllegalAccessException e) {
                    break;
                }
            }
        }
    }

    public static Properties load(Class<?> clazz) throws IllegalArgumentException {
        String path = "/" + clazz.getName().replace('.', '/') + ".properties";
        Properties properties = cache.get(path);
        if (properties != null) return properties;
        InputStream in = null;
        try {
            URL resource = clazz.getResource(path);
            if (resource == null) throw new FileNotFoundException(path);
            properties = new Properties();
            properties.load(in = resource.openStream());
            Properties old = cache.putIfAbsent(path, properties);
            return old != null ? old : properties;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            IOKit.close(in);
        }
    }

}
