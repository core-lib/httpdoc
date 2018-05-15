package io.httpdoc.core.reflection;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 通配符类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 9:47
 **/
public class WildcardTypeImpl implements WildcardType {
    private final Type upperBound;
    private final Type lowerBound;

    public WildcardTypeImpl(Type upperBound, Type lowerBound) {
        if (upperBound == null && lowerBound == null) throw new IllegalArgumentException("either upper bound or lower bound is not null");
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override
    public Type[] getUpperBounds() {
        return upperBound != null ? new Type[]{upperBound} : new Type[0];
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBound != null ? new Type[]{lowerBound} : new Type[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WildcardTypeImpl that = (WildcardTypeImpl) o;

        boolean upper = (upperBound != null ? upperBound.equals(that.upperBound) : that.upperBound == null);
        boolean lower = (lowerBound != null ? lowerBound.equals(that.lowerBound) : that.lowerBound == null);
        return upper && lower;
    }

    @Override
    public int hashCode() {
        int result = upperBound != null ? upperBound.hashCode() : 0;
        result = 31 * result + (lowerBound != null ? lowerBound.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (lowerBound != null) builder.append("? super ").append(lowerBound);
        else if (upperBound != null) builder.append("? extends ").append(upperBound);
        else builder.append("?");
        return builder.toString();
    }

}
