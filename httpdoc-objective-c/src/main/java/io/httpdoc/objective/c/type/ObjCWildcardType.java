package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDWildcardType;

import java.util.Set;

public class ObjCWildcardType extends HDWildcardType {
    private final HDWildcardType wildcardType;

    public ObjCWildcardType(HDWildcardType wildcardType) {
        this.wildcardType = wildcardType;
    }

    @Override
    public CharSequence getAbbrName() {
        return wildcardType.getAbbrName();
    }

    @Override
    public CharSequence getTypeName() {
        return wildcardType.getTypeName();
    }

    @Override
    public Set<String> imports() {
        return wildcardType.imports();
    }

    @Override
    public HDType getUpperBound() {
        HDType upperBound = wildcardType.getUpperBound();
        if (upperBound == null) return null;
        return ObjCType.valueOf(upperBound);
    }

    @Override
    public HDType getLowerBound() {
        HDType lowerBound = wildcardType.getLowerBound();
        if (lowerBound == null) return null;
        return ObjCType.valueOf(lowerBound);
    }

    @Override
    public int length() {
        return wildcardType.length();
    }

    @Override
    public char charAt(int index) {
        return wildcardType.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return wildcardType.subSequence(start, end);
    }
}
