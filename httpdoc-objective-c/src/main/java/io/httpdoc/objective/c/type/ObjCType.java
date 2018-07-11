package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.*;

public abstract class ObjCType {

    public static HDType valueOf(String prefix, HDType type) {
        if (type == null) return null;
        else if (type instanceof HDClass) return new ObjCClass(prefix, (HDClass) type);
        else if (type instanceof HDParameterizedType) return new ObjCParameterizedType(prefix, (HDParameterizedType) type);
        else if (type instanceof HDTypeVariable) return new ObjCTypeVariable(prefix, (HDTypeVariable) type);
        else if (type instanceof HDGenericArrayType) return new ObjCGenericArrayType(prefix, (HDGenericArrayType) type);
        else if (type instanceof HDWildcardType) return new ObjCWildcardType(prefix, (HDWildcardType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }


}
