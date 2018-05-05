package io.httpdoc.core.type;

import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 11:10
 **/
public abstract class HDType implements CharSequence, Importable {
    private static final ConcurrentMap<Type, HDType> CACHE = new ConcurrentHashMap<>();

    public static HDType valueOf(Type type) {
        if (type == null) return null;
        else if (CACHE.containsKey(type)) return CACHE.get(type);
        else if (type instanceof Class<?>) return valueOf((Class<?>) type);
        else if (type instanceof ParameterizedType) return valueOf((ParameterizedType) type);
        else if (type instanceof GenericArrayType) return valueOf((GenericArrayType) type);
        else if (type instanceof TypeVariable<?>) return valueOf((TypeVariable<?>) type);
        else if (type instanceof WildcardType) return valueOf((WildcardType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }

    public static HDClass valueOf(Class<?> clazz) {
        if (CACHE.containsKey(clazz)) return (HDClass) CACHE.get(clazz);
        HDClass javaClass = new HDClass(clazz);
        HDType HDType = CACHE.putIfAbsent(clazz, javaClass);
        if (HDType == null) {
            TypeVariable<?>[] variables = clazz.getTypeParameters();
            HDTypeVariable[] typeParameters = new HDTypeVariable[variables != null ? variables.length : 0];
            for (int i = 0; variables != null && i < variables.length; i++) typeParameters[i] = HDType.valueOf(variables[i]);
            javaClass.setTypeParameters(typeParameters);
        }
        return HDType != null ? (HDClass) HDType : javaClass;
    }

    public static HDParameterizedType valueOf(ParameterizedType type) {
        if (CACHE.containsKey(type)) return (HDParameterizedType) CACHE.get(type);
        HDParameterizedType javaParameterizedType = new HDParameterizedType();
        HDType HDType = CACHE.putIfAbsent(type, javaParameterizedType);
        if (HDType == null) {
            HDType rawType = valueOf(type.getRawType());
            javaParameterizedType.setRawType(rawType);
            HDType ownerType = valueOf(type.getOwnerType());
            javaParameterizedType.setOwnerType(ownerType);
            HDType[] actualTypeArguments = new HDType[type.getActualTypeArguments() != null ? type.getActualTypeArguments().length : 0];
            for (int i = 0; i < actualTypeArguments.length; i++) actualTypeArguments[i] = valueOf(type.getActualTypeArguments()[i]);
            javaParameterizedType.setActualTypeArguments(actualTypeArguments);
        }
        return HDType != null ? (HDParameterizedType) HDType : javaParameterizedType;
    }

    public static HDGenericArrayType valueOf(GenericArrayType type) {
        if (CACHE.containsKey(type)) return (HDGenericArrayType) CACHE.get(type);
        HDGenericArrayType javaGenericArrayType = new HDGenericArrayType();
        HDType HDType = CACHE.putIfAbsent(type, javaGenericArrayType);
        if (HDType == null) {
            HDType genericComponentType = valueOf(type.getGenericComponentType());
            javaGenericArrayType.setGenericComponentType(genericComponentType);
        }
        return HDType != null ? (HDGenericArrayType) HDType : javaGenericArrayType;
    }

    public static HDTypeVariable valueOf(TypeVariable<?> variable) {
        if (CACHE.containsKey(variable)) return (HDTypeVariable) CACHE.get(variable);
        HDTypeVariable javaTypeVariable = new HDTypeVariable(variable.getName());
        HDType HDType = CACHE.putIfAbsent(variable, javaTypeVariable);
        if (HDType == null) {
            Type[] bounds = variable.getBounds();
            if (bounds != null && bounds.length > 0) javaTypeVariable.setBound(valueOf(bounds[0]));
        }
        return HDType != null ? (HDTypeVariable) HDType : javaTypeVariable;
    }

    public static HDWildcardType valueOf(WildcardType type) {
        if (CACHE.containsKey(type)) return (HDWildcardType) CACHE.get(type);
        HDWildcardType javaWildcardType = new HDWildcardType();
        HDType HDType = CACHE.putIfAbsent(type, javaWildcardType);
        if (HDType == null) {
            Type[] upperBounds = type.getUpperBounds();
            if (upperBounds != null && upperBounds.length > 0) javaWildcardType.setUpperBound(valueOf(upperBounds[0]));
            Type[] lowerBounds = type.getLowerBounds();
            if (lowerBounds != null && lowerBounds.length > 0) javaWildcardType.setLowerBound(valueOf(lowerBounds[0]));
        }
        return HDType != null ? (HDWildcardType) HDType : javaWildcardType;
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
