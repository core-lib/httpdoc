package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.*;

public abstract class ObjCType {

    public static HDType valueOf(HDType type) {
        if (type == null) return null;
        else if (type instanceof HDClass) return new ObjCClass((HDClass) type);
        else if (type instanceof HDParameterizedType) return new ObjCParameterizedType((HDParameterizedType) type);
        else if (type instanceof HDTypeVariable) return new ObjCTypeVariable((HDTypeVariable) type);
        else if (type instanceof HDGenericArrayType) return new ObjCGenericArrayType((HDGenericArrayType) type);
        else if (type instanceof HDWildcardType) return new ObjCWildcardType((HDWildcardType) type);
        else throw new IllegalArgumentException("unsupported type " + type);
    }


}
