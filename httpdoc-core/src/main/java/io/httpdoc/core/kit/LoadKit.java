package io.httpdoc.core.kit;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.loadkit.Loaders;
import io.loadkit.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置加载器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:55
 **/
public class LoadKit {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoadKit.class);

    public static Enumeration<Resource> load(ClassLoader classLoader) throws IOException {
        return Loaders.ant(classLoader)
                .load("httpdoc/*.properties");
    }

    public static <T> Map<String, T> load(ClassLoader classLoader, Class<T> type) {
        Map<String, T> map = new LinkedHashMap<>();
        try {
            Enumeration<Resource> resources = LoadKit.load(classLoader);
            while (resources.hasMoreElements()) {
                Resource resource = resources.nextElement();
                URL url = resource.getUrl();
                Properties properties = new Properties();
                properties.load(url.openStream());
                if (properties.isEmpty()) continue;
                Enumeration<Object> keys = properties.keys();
                while (keys.hasMoreElements()) {
                    String name = (String) keys.nextElement();
                    String value = (String) properties.get(name);
                    Class<? extends T> clazz = load(value, type);
                    if (clazz == null) continue;
                    try {
                        T bean = clazz.newInstance();
                        map.put(name, bean);
                    } catch (Exception e) {
                        LOGGER.warn("could not load " + type.getSimpleName() + " for [" + clazz + "]", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new HttpdocRuntimeException(e);
        }
        return map;
    }

    public static <T> Class<? extends T> load(String className, Class<T> superType) {
        try {
            Class<?> subType = Class.forName(className);
            if (superType.isAssignableFrom(subType)) return subType.asSubclass(superType);
            else return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
