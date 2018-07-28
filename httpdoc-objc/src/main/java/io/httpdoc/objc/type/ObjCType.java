package io.httpdoc.objc.type;

import io.httpdoc.objc.ObjC;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ObjC 类型父类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 13:55
 **/
public abstract class ObjCType implements ObjC {
    private static final ConcurrentMap<Type, ObjCType> CACHE = new ConcurrentHashMap<>();

    public static ObjCGenericType valueOf(ParameterizedType type) {
        if (CACHE.containsKey(type)) return (ObjCGenericType) CACHE.get(type);
        ObjCGenericType javaParameterizedType = new ObjCGenericType();
        ObjCType ObjCType = CACHE.putIfAbsent(type, javaParameterizedType);
        if (ObjCType == null) {
            ObjCClass rawType = valueOf(((Class<?>) type.getRawType()).asSubclass(ObjC.class));
            javaParameterizedType.setRawType(rawType);
            ObjCType[] actualTypeArguments = new ObjCType[type.getActualTypeArguments() != null ? type.getActualTypeArguments().length : 0];
            for (int i = 0; i < actualTypeArguments.length; i++) actualTypeArguments[i] = valueOf(type.getActualTypeArguments()[i]);
            javaParameterizedType.setActualTypeArguments(actualTypeArguments);
        }
        return ObjCType != null ? (ObjCGenericType) ObjCType : javaParameterizedType;
    }

    public static ObjCType valueOf(Type type) {
        if (type == null) return null;
        else if (CACHE.containsKey(type)) return CACHE.get(type);
        else if (type instanceof Class<?>) return valueOf(((Class<?>) type).asSubclass(ObjC.class));
        else if (type instanceof ParameterizedType) return valueOf((ParameterizedType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }

    public static ObjCClass valueOf(Class<? extends ObjC> clazz) {
        if (CACHE.containsKey(clazz)) return (ObjCClass) CACHE.get(clazz);
        ObjCClass javaClass = new ObjCClass(clazz);
        ObjCType objCType = CACHE.putIfAbsent(clazz, javaClass);
        return objCType != null ? (ObjCClass) objCType : javaClass;
    }

    public abstract Set<ObjCClass> dependencies();

    public boolean isClass() {
        return getKind() == Kind.CLASS;
    }

    public boolean isProtocol() {
        return getKind() == Kind.PROTOCOL;
    }

    public boolean isPrimitive() {
        return getKind() == Kind.PRIMITIVE;
    }

    public boolean isTypedef() {
        return getKind() == Kind.TYPEDEF;
    }

    public boolean isBlock() {
        return getKind() == Kind.BLOCK;
    }

    public boolean isGeneric() {
        return getKind() == Kind.GENERIC;
    }

}
