package io.httpdoc.core.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 反射工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-23 13:28
 **/
public class ReflectionKit {

    public static Field getField(Class<?> clazz, String name) {
        if (clazz == null) throw new NullPointerException();
        if (name == null) throw new IllegalArgumentException("field name == null");
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    public static <T> T getFieldValue(Object obj, String name) {
        if (obj == null) throw new NullPointerException();
        try {
            if (obj instanceof Class<?>) {
                Field field = getField((Class<?>) obj, name);
                if (field == null) throw new IllegalArgumentException("no such field");
                else if (Modifier.isStatic(field.getModifiers())) return (T) field.get(null);
                else throw new IllegalArgumentException("access instance field in static mode");
            } else {
                Class<?> clazz = obj.getClass();
                Field field = getField(clazz, name);
                if (field == null) throw new IllegalArgumentException("no such field");
                else return (T) field.get(obj);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
