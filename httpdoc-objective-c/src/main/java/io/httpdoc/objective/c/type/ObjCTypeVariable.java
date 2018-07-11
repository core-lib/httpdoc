package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.util.Set;

public class ObjCTypeVariable extends HDTypeVariable implements ObjC {
    private final HDTypeVariable typeVariable;

    public ObjCTypeVariable(HDTypeVariable typeVariable) {
        this.typeVariable = typeVariable;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        return ObjCReferenceType.STRONG;
    }

    @Override
    public CharSequence getAbbrName() {
        return typeVariable.getAbbrName();
    }

    @Override
    public CharSequence getTypeName() {
        return typeVariable.getTypeName();
    }

    @Override
    public Set<String> imports() {
        return typeVariable.imports();
    }

    @Override
    public String getName() {
        return typeVariable.getName();
    }

    @Override
    public HDType getBound() {
        HDType bound = typeVariable.getBound();
        if (bound == null) return null;
        return ObjCType.valueOf(bound);
    }

}
