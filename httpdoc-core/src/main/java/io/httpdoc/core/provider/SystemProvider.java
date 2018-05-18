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
public class SystemProvider implements Provider {

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

    @Override
    public boolean contains(Schema schema) {
        return acquire(schema) != null;
    }

    @Override
    public Type acquire(Schema schema) {
        if (schema == null || schema.getCategory() != Category.BASIC) return null;
        String name = schema.getName();
        switch (name) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "char":
                return char.class;
            case "int":
                return int.class;
            case "float":
                return float.class;
            case "long":
                return long.class;
            case "double":
                return double.class;

            case "void":
                return void.class;

            case "Boolean":
                return Boolean.class;
            case "Byte":
                return Byte.class;
            case "Short":
                return Short.class;
            case "Character":
                return Character.class;
            case "Integer":
                return Integer.class;
            case "Float":
                return Float.class;
            case "Long":
                return Long.class;
            case "Double":
                return Double.class;

            case "String":
                return String.class;
            case "Number":
                return Number.class;
            case "Date":
                return Date.class;
            case "File":
                return File.class;
            case "Object":
                return Object.class;
        }
        return null;
    }

    private Schema build(String name) {
        Schema schema = new Schema();
        schema.setCategory(Category.BASIC);
        schema.setName(name);
        return schema;
    }

}
