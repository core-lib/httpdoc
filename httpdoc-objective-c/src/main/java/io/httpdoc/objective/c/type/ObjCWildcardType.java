package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDWildcardType;

import java.util.Set;

public class ObjCWildcardType extends HDWildcardType implements ObjC {
    private final HDWildcardType wildcardType;

    public ObjCWildcardType(HDWildcardType wildcardType) {
        this.wildcardType = wildcardType;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        return ObjCReferenceType.STRONG;
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

}
