package io.httpdoc.core.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class<?> rawType;
    private final Class<?> ownerType;
    private final Type[] actualTypeArguments;

    public ParameterizedTypeImpl(Class<?> rawType, Type... actualTypeArguments) {
        this(rawType, null, actualTypeArguments);
    }

    public ParameterizedTypeImpl(Class<?> rawType, Class<?> ownerType, Type... actualTypeArguments) {
        if (rawType == null) throw new NullPointerException("raw type can not be null");
        if (actualTypeArguments == null || actualTypeArguments.length == 0) throw new IllegalArgumentException("actual type arguments is null or empty");
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    public Class<?> getOwnerType() {
        return ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;

        if (!rawType.equals(that.rawType)) return false;
        if (ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(actualTypeArguments, that.actualTypeArguments);
    }

    @Override
    public int hashCode() {
        int result = rawType.hashCode();
        result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(actualTypeArguments);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (ownerType != null) sb.append(ownerType).append(".");
        sb.append(rawType);
        sb.append("<");
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(actualTypeArguments[0]);
        }
        sb.append(">");
        return sb.toString();
    }
}
