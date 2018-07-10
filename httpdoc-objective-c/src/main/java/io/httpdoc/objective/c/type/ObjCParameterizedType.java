package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;

import java.util.Set;

public class ObjCParameterizedType extends HDParameterizedType {
    private final HDParameterizedType parameterizedType;

    public ObjCParameterizedType(HDParameterizedType parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    @Override
    public CharSequence getAbbrName() {
        return parameterizedType.getAbbrName();
    }

    @Override
    public CharSequence getTypeName() {
        return parameterizedType.getTypeName();
    }

    @Override
    public Set<String> imports() {
        return parameterizedType.imports();
    }

    @Override
    public HDClass getRawType() {
        HDClass rawType = parameterizedType.getRawType();
        if (rawType == null) return null;
        return new ObjCClass(rawType);
    }

    @Override
    public HDType getOwnerType() {
        HDType ownerType = parameterizedType.getOwnerType();
        if (ownerType == null) return null;
        return ObjCType.valueOf(ownerType);
    }

    @Override
    public HDType[] getActualTypeArguments() {
        HDType[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments == null) return null;
        HDType[] array = new HDType[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; i++) array[i] = ObjCType.valueOf(actualTypeArguments[i]);
        return array;
    }

    @Override
    public int length() {
        return parameterizedType.length();
    }

    @Override
    public char charAt(int index) {
        return parameterizedType.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return parameterizedType.subSequence(start, end);
    }

}
