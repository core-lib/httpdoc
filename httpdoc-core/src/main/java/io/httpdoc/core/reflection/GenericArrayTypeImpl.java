package io.httpdoc.core.reflection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * 泛型数组类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 9:37
 **/
public class GenericArrayTypeImpl implements GenericArrayType {
    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        if (genericComponentType == null) throw new NullPointerException();
        this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericArrayTypeImpl that = (GenericArrayTypeImpl) o;

        return genericComponentType.equals(that.genericComponentType);
    }

    @Override
    public int hashCode() {
        return genericComponentType.hashCode();
    }

    @Override
    public String toString() {
        return genericComponentType.toString() + "[]";
    }
}
