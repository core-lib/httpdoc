package io.httpdoc.core.provider;

import io.httpdoc.core.Category;
import io.httpdoc.core.Schema;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 基本资源模型提供者
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 13:45
 **/
public class DefaultProvider implements Provider {

    private final List<? extends Class<?>> wrappers = Arrays.asList(
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Float.class,
            Long.class,
            Double.class
    );

    @Override
    public boolean contains(Type type) {
        return acquire(type) != null;
    }

    @Override
    public Schema acquire(Type type) {
        if (!(type instanceof Class<?>)) return null;
        Class<?> clazz = (Class<?>) type;
        if (clazz.isPrimitive()) return build(clazz.getName());
        if (wrappers.contains(clazz)) return build(clazz.getSimpleName());
        if (CharSequence.class.isAssignableFrom(clazz)) return build("String");
        if (Number.class.isAssignableFrom(clazz)) return build("Number");
        if (Date.class.isAssignableFrom(clazz)) return build("Date");
        if (File.class.isAssignableFrom(clazz)) return build("File");
        if (Object.class == clazz) return build("Object");
        return null;
    }

    private Schema build(String name) {
        Schema schema = new Schema();
        schema.setCategory(Category.BASIC);
        schema.setName(name);
        return schema;
    }

}
