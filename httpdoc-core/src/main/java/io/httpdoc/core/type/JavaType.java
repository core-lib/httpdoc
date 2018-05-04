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

    public static void main(String... args) throws NoSuchMethodException {
        Method test = JavaType.class.getMethod("test");
        Type genericReturnType = test.getGenericReturnType();
        JavaType javaType = valueOf(genericReturnType);
        System.out.println(javaType);
    }

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
        JavaTypeVariable javaTypeVariable = new JavaTypeVariable(variable.getName());
        JavaType javaType = CACHE.putIfAbsent(variable, javaTypeVariable);
        if (javaType == null) {
            JavaType[] bounds = new JavaType[variable.getBounds() != null ? variable.getBounds().length : 0];
            for (int i = 0; i < bounds.length; i++) bounds[i] = valueOf(variable.getBounds()[i]);
            javaTypeVariable.setBounds(bounds);
        }
        return javaType != null ? (JavaTypeVariable) javaType : javaTypeVariable;
    }

    public static JavaWildcardType valueOf(WildcardType type) {
        if (CACHE.containsKey(type)) return (JavaWildcardType) CACHE.get(type);
        JavaWildcardType javaWildcardType = new JavaWildcardType();
        JavaType javaType = CACHE.putIfAbsent(type, javaWildcardType);
        if (javaType == null) {
            JavaType[] upperBounds = new JavaType[type.getUpperBounds() != null ? type.getUpperBounds().length : 0];
            for (int i = 0; i < upperBounds.length; i++) upperBounds[i] = valueOf(type.getUpperBounds()[i]);
            javaWildcardType.setUpperBounds(upperBounds);
            JavaType[] lowerBounds = new JavaType[type.getLowerBounds() != null ? type.getLowerBounds().length : 0];
            for (int i = 0; i < lowerBounds.length; i++) lowerBounds[i] = valueOf(type.getLowerBounds()[i]);
            javaWildcardType.setLowerBounds(lowerBounds);
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
