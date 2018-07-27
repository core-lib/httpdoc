package io.httpdoc.objc.type;

import io.httpdoc.core.Importable;
import io.httpdoc.objc.ObjCConstant;
import io.httpdoc.objc.foundation.Foundation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ObjC 类型父类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 13:55
 **/
public abstract class ObjCType implements Importable, ObjCConstant {
    private static final ConcurrentMap<Type, ObjCType> CACHE = new ConcurrentHashMap<>();

    public abstract String getName();

    public abstract boolean isPrimitive();

    public abstract String getReferenceType();

    public static ObjCType valueOf(Type type) {
        if (type == null) return null;
        else if (CACHE.containsKey(type)) return CACHE.get(type);
        else if (type instanceof Class<?>) return valueOf((Class<?>) type);
        else if (type instanceof ParameterizedType) return valueOf((ParameterizedType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }

    public static ObjCClass valueOf(Class<?> clazz) {
        if (CACHE.containsKey(clazz)) return (ObjCClass) CACHE.get(clazz);
        ObjCClass javaClass = new ObjCClass(clazz.asSubclass(Foundation.class));
        ObjCType objCType = CACHE.putIfAbsent(clazz, javaClass);
        return objCType != null ? (ObjCClass) objCType : javaClass;
    }

    public static ObjCParameterizedType valueOf(ParameterizedType type) {
        if (CACHE.containsKey(type)) return (ObjCParameterizedType) CACHE.get(type);
        ObjCParameterizedType javaParameterizedType = new ObjCParameterizedType();
        ObjCType ObjCType = CACHE.putIfAbsent(type, javaParameterizedType);
        if (ObjCType == null) {
            ObjCClass rawType = valueOf((Class<?>) type.getRawType());
            javaParameterizedType.setRawType(rawType);
            ObjCType[] actualTypeArguments = new ObjCType[type.getActualTypeArguments() != null ? type.getActualTypeArguments().length : 0];
            for (int i = 0; i < actualTypeArguments.length; i++) actualTypeArguments[i] = valueOf(type.getActualTypeArguments()[i]);
            javaParameterizedType.setActualTypeArguments(actualTypeArguments);
        }
        return ObjCType != null ? (ObjCParameterizedType) ObjCType : javaParameterizedType;
    }

}
