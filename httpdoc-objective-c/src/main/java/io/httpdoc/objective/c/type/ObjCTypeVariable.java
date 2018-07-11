package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.util.Set;

public class ObjCTypeVariable extends HDTypeVariable {
    private final HDTypeVariable typeVariable;

    public ObjCTypeVariable(HDTypeVariable typeVariable) {
        this.typeVariable = typeVariable;
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

    @Override
    public int length() {
        return typeVariable.length();
    }

    @Override
    public char charAt(int index) {
        return typeVariable.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return typeVariable.subSequence(start, end);
    }
}
