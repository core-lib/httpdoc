package io.httpdoc.core.translation;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractContainer implements Container {

    @Override
    public <T> Map<String, T> get(Class<T> type) {
        Map<String, T> map = new LinkedHashMap<>();
        Enumeration<String> names = names();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object bean = get(name);
            if (type.isInstance(bean)) map.put(name, type.cast(bean));
        }
        return map;
    }

}
