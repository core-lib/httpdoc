package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDWildcardType;
import io.httpdoc.objective.c.ObjC;

import java.util.Set;

public class ObjCWildcardType extends HDWildcardType implements ObjC {
    private final String prefix;
    private final HDWildcardType wildcardType;

    public ObjCWildcardType(String prefix, HDWildcardType wildcardType) {
        this.prefix = prefix;
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
        return ObjCType.valueOf(prefix, upperBound);
    }

    @Override
    public HDType getLowerBound() {
        HDType lowerBound = wildcardType.getLowerBound();
        if (lowerBound == null) return null;
        return ObjCType.valueOf(prefix, lowerBound);
    }

}
