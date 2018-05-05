package io.httpdoc.core.type;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 11:10
 **/
public abstract class JavaType implements CharSequence, Importable {
    private static final ConcurrentMap<Type, JavaType> CACHE = new ConcurrentHashMap<>();

    public List<? extends Map<? super String, ? extends Serializable>> test() {

        return null;
    }

    public static JavaType valueOf(Type type) {
        if (type == null) return null;
        else if (CACHE.containsKey(type)) return CACHE.get(type);
        else if (type instanceof Class<?>) return valueOf((Class<?>) type);
        else if (type instanceof ParameterizedType) return valueOf((ParameterizedType) type);
        else if (type instanceof GenericArrayType) return valueOf((GenericArrayType) type);
        else if (type instanceof TypeVariable<?>) return valueOf((TypeVariable<?>) type);
        else if (type instanceof WildcardType) return valueOf((WildcardType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }

    public static JavaClass valueOf(Class<?> clazz) {
        if (CACHE.containsKey(clazz)) return (JavaClass) CACHE.get(clazz);
        JavaClass javaClass = new JavaClass(clazz);
        JavaType javaType = CACHE.putIfAbsent(clazz, javaClass);
        if (javaType == null) {
            TypeVariable<?>[] variables = clazz.getTypeParameters();
            JavaTypeVariable[] typeParameters = new JavaTypeVariable[variables != null ? variables.length : 0];
            for (int i = 0; variables != null && i < variables.length; i++) typeParameters[i] = JavaType.valueOf(variables[i]);
            javaClass.setTypeParameters(typeParameters);
        }
        return javaType != null ? (JavaClass) javaType : javaClass;
    }

    public static JavaParameterizedType valueOf(ParameterizedType type) {
        if (CACHE.containsKey(type)) return (JavaParameterizedType) CACHE.get(type);
        JavaParameterizedType javaParameterizedType = new JavaParameterizedType();
        JavaType javaType = CACHE.putIfAbsent(type, javaParameterizedType);
        if (javaType == null) {
            JavaType rawType = valueOf(type.getRawType());
            javaParameterizedType.setRawType(rawType);
            JavaType ownerType = valueOf(type.getOwnerType());
            javaParameterizedType.setOwnerType(ownerType);
            JavaType[] actualTypeArguments = new JavaType[type.getActualTypeArguments() != null ? type.getActualTypeArguments().length : 0];
            for (int i = 0; i < actualTypeArguments.length; i++) actualTypeArguments[i] = valueOf(type.getActualTypeArguments()[i]);
            javaParameterizedType.setActualTypeArguments(actualTypeArguments);
        }
        return javaType != null ? (JavaParameterizedType) javaType : javaParameterizedType;
    }

    public static JavaGenericArrayType valueOf(GenericArrayType type) {
        if (CACHE.containsKey(type)) return (JavaGenericArrayType) CACHE.get(type);
        JavaGenericArrayType javaGenericArrayType = new JavaGenericArrayType();
        JavaType javaType = CACHE.putIfAbsent(type, javaGenericArrayType);
        if (javaType == null) {
            JavaType genericComponentType = valueOf(type.getGenericComponentType());
            javaGenericArrayType.setGenericComponentType(genericComponentType);
        }
        return javaType != null ? (JavaGenericArrayType) javaType : javaGenericArrayType;
    }

    public static JavaTypeVariable valueOf(TypeVariable<?> variable) {
        if (CACHE.containsKey(variable)) return (JavaTypeVariable) CACHE.get(variable);
        JavaTypeVariable javaTypeVariable = new JavaTypeVariable(variable.getName());
        JavaType javaType = CACHE.putIfAbsent(variable, javaTypeVariable);
        if (javaType == null) {
            Type[] bounds = variable.getBounds();
            if (bounds != null && bounds.length > 0) javaTypeVariable.setBound(valueOf(bounds[0]));
        }
        return javaType != null ? (JavaTypeVariable) javaType : javaTypeVariable;
    }

    public static JavaWildcardType valueOf(WildcardType type) {
        if (CACHE.containsKey(type)) return (JavaWildcardType) CACHE.get(type);
        JavaWildcardType javaWildcardType = new JavaWildcardType();
        JavaType javaType = CACHE.putIfAbsent(type, javaWildcardType);
        if (javaType == null) {
            Type[] upperBounds = type.getUpperBounds();
            if (upperBounds != null && upperBounds.length > 0) javaWildcardType.setUpperBound(valueOf(upperBounds[0]));
            Type[] lowerBounds = type.getLowerBounds();
            if (lowerBounds != null && lowerBounds.length > 0) javaWildcardType.setLowerBound(valueOf(lowerBounds[0]));
        }
        return javaType != null ? (JavaWildcardType) javaType : javaWildcardType;
    }

    public abstract CharSequence getFormatName();

    @Override
    public int length() {
        return getFormatName().length();
    }

    @Override
    public char charAt(int index) {
        return getFormatName().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return getFormatName().subSequence(start, end);
    }

    @Override
    public String toString() {
        return getFormatName().toString();
    }

}
