package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDGenericArrayType;
import io.httpdoc.core.type.HDType;

import java.util.Set;

public class ObjCGenericArrayType extends HDGenericArrayType implements ObjC {
    private final HDGenericArrayType genericArrayType;

    public ObjCGenericArrayType(HDGenericArrayType genericArrayType) {
        this.genericArrayType = genericArrayType;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        return ObjCReferenceType.COPY;
    }

    @Override
    public CharSequence getAbbrName() {
        return genericArrayType.getAbbrName();
    }

    @Override
    public CharSequence getTypeName() {
        return genericArrayType.getTypeName();
    }

    @Override
    public Set<String> imports() {
        return genericArrayType.imports();
    }

    @Override
    public HDType getGenericComponentType() {
        HDType genericComponentType = genericArrayType.getGenericComponentType();
        if (genericComponentType == null) return null;
        return ObjCType.valueOf(genericComponentType);
    }

}
