package io.httpdoc.core.type;

import io.httpdoc.core.appender.Appender;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
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

    public static void main(String... args) {
        JavaClass javaClass = JavaType.valueOf(Appender.class);
        System.out.println(javaClass.getFormatName());
    }

    public static JavaType valueOf(Type type) {
        if (type == null) return null;
        else if (CACHE.containsKey(type)) return CACHE.get(type);
        else if (type instanceof Class<?>) return valueOf((Class<?>) type);
        else if (type instanceof ParameterizedType) return valueOf((ParameterizedType) type);
        else if (type instanceof GenericArrayType) return valueOf((GenericArrayType) type);
        else if (type instanceof TypeVariable<?>) return valueOf((TypeVariable<?>) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }

    public static JavaClass valueOf(Class<?> clazz) {
        if (CACHE.containsKey(clazz)) return (JavaClass) CACHE.get(clazz);
        JavaClass javaClass = new JavaClass(clazz);
        JavaType javaType = CACHE.putIfAbsent(clazz, javaClass);
        if (javaType == null) {
            TypeVariable<?>[] variables = clazz.getTypeParameters();
            JavaTypeVariable[] typeParameters = new JavaTypeVariable[variables.length];
            for (int i = 0; i < variables.length; i++) typeParameters[i] = JavaType.valueOf(variables[i]);
            javaClass.setTypeParameters(typeParameters);
        }
        return javaType != null ? (JavaClass) javaType : javaClass;
    }

    public static JavaParameterizedType valueOf(ParameterizedType type) {
        if (CACHE.containsKey(type)) return (JavaParameterizedType) CACHE.get(type);
        JavaType rawType = valueOf(type.getRawType());
        JavaType ownerType = valueOf(type.getOwnerType());
        JavaParameterizedType javaParameterizedType = new JavaParameterizedType(rawType, ownerType);
        JavaType javaType = CACHE.putIfAbsent(type, javaParameterizedType);
        if (javaType == null) {
            JavaType[] actualTypeArguments = new JavaType[type.getActualTypeArguments() != null ? type.getActualTypeArguments().length : 0];
            for (int i = 0; i < actualTypeArguments.length; i++) actualTypeArguments[i] = valueOf(type.getActualTypeArguments()[i]);
            javaParameterizedType.setActualTypeArguments(actualTypeArguments);
        }
        return javaType != null ? (JavaParameterizedType) javaType : javaParameterizedType;
    }

    public static JavaGenericArrayType valueOf(GenericArrayType type) {
        if (CACHE.containsKey(type)) return (JavaGenericArrayType) CACHE.get(type);
        JavaGenericArrayType javaGenericArrayType = new JavaGenericArrayType(valueOf(type.getGenericComponentType()));
        JavaType javaType = CACHE.putIfAbsent(type, javaGenericArrayType);
        return javaType != null ? (JavaGenericArrayType) javaType : javaGenericArrayType;
    }

    public static JavaTypeVariable valueOf(TypeVariable<?> variable) {
        if (CACHE.containsKey(variable)) return (JavaTypeVariable) CACHE.get(variable);
        JavaType[] bounds = new JavaType[variable.getBounds() != null ? variable.getBounds().length : 0];
        for (int i = 0; i < bounds.length; i++) bounds[i] = valueOf(variable.getBounds()[i]);
        JavaTypeVariable javaTypeVariable = new JavaTypeVariable(variable.getName(), bounds);
        JavaType javaType = CACHE.putIfAbsent(variable, javaTypeVariable);
        return javaType != null ? (JavaTypeVariable) javaType : javaTypeVariable;
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
