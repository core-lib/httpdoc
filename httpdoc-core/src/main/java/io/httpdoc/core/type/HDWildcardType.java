package io.httpdoc.core.type;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 通配类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 15:20
 **/
public class HDWildcardType extends HDType {
    private HDType upperBound;
    private HDType lowerBound;

    HDWildcardType() {
    }

    public HDWildcardType(HDType upperBound, HDType lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override
    public CharSequence getAbbrName() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        if (lowerBound != null) builder.append(" super ").append(lowerBound.getAbbrName());
        else if (upperBound != null) builder.append(" extends ").append(upperBound.getAbbrName());
        return builder;
    }

    @Override
    public CharSequence getTypeName() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        if (lowerBound != null) builder.append(" super ").append(lowerBound.getTypeName());
        else if (upperBound != null) builder.append(" extends ").append(upperBound.getTypeName());
        return builder;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        if (upperBound != null) imports.addAll(upperBound.imports());
        if (lowerBound != null) imports.addAll(lowerBound.imports());
        return imports;
    }

    public HDType getUpperBound() {
        return upperBound;
    }

    void setUpperBound(HDType upperBound) {
        this.upperBound = upperBound;
    }

    public HDType getLowerBound() {
        return lowerBound;
    }

    void setLowerBound(HDType lowerBound) {
        this.lowerBound = lowerBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HDWildcardType that = (HDWildcardType) o;

        if (upperBound != null ? !upperBound.equals(that.upperBound) : that.upperBound != null) return false;
        return lowerBound != null ? lowerBound.equals(that.lowerBound) : that.lowerBound == null;
    }

    @Override
    public int hashCode() {
        int result = upperBound != null ? upperBound.hashCode() : 0;
        result = 31 * result + (lowerBound != null ? lowerBound.hashCode() : 0);
        return result;
    }
}
