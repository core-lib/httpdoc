package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.util.LinkedHashSet;
import java.util.Set;

public class ObjCParameterizedType extends HDParameterizedType implements ObjC {
    private final String prefix;
    private final HDParameterizedType parameterizedType;

    public ObjCParameterizedType(String prefix, HDParameterizedType parameterizedType) {
        this.prefix = prefix;
        this.parameterizedType = parameterizedType;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        HDClass rawType = getRawType();
        String abbrName = rawType.getAbbrName().toString();
        if ("NSArray".equals(abbrName) || "NSDictionary".equals(abbrName)) return ObjCReferenceType.COPY;
        else return ObjCReferenceType.STRONG;
    }

    @Override
    public CharSequence getAbbrName() {
        HDClass rawType = getRawType();
        HDType[] actualTypeArguments = getActualTypeArguments();
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getAbbrName());
        for (int i = 0; actualTypeArguments != null && i < actualTypeArguments.length; i++) {
            if (i == 0) builder.append("<");
            else builder.append(", ");
            builder.append(actualTypeArguments[i] instanceof HDTypeVariable ? ((HDTypeVariable) actualTypeArguments[i]).getName() : actualTypeArguments[i].getAbbrName());
            if (i == actualTypeArguments.length - 1) builder.append(">");
        }
        builder.append(" *");
        return builder;
    }

    @Override
    public CharSequence getTypeName() {
        HDClass rawType = getRawType();
        HDType[] actualTypeArguments = getActualTypeArguments();
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getTypeName());
        for (int i = 0; actualTypeArguments != null && i < actualTypeArguments.length; i++) {
            if (i == 0) builder.append("<");
            else builder.append(", ");
            builder.append(actualTypeArguments[i] instanceof HDTypeVariable ? ((HDTypeVariable) actualTypeArguments[i]).getName() : actualTypeArguments[i].getTypeName());
            if (i == actualTypeArguments.length - 1) builder.append(">");
        }
        builder.append(" *");
        return builder;
    }

    @Override
    public Set<String> imports() {
        HDClass rawType = getRawType();
        HDType ownerType = getOwnerType();
        HDType[] actualTypeArguments = getActualTypeArguments();
        Set<String> imports = new LinkedHashSet<>();
        if (rawType != null) imports.addAll(rawType.imports());
        if (ownerType != null) imports.addAll(ownerType.imports());
        for (int i = 0; actualTypeArguments != null && i < actualTypeArguments.length; i++) imports.addAll(actualTypeArguments[i].imports());
        return imports;
    }

    @Override
    public HDClass getRawType() {
        HDClass rawType = parameterizedType.getRawType();
        if (rawType == null) return null;
        return new ObjCClass(prefix, rawType);
    }

    @Override
    public HDType getOwnerType() {
        HDType ownerType = parameterizedType.getOwnerType();
        if (ownerType == null) return null;
        return ObjCType.valueOf(prefix, ownerType);
    }

    @Override
    public HDType[] getActualTypeArguments() {
        HDType[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments == null) return null;
        HDType[] array = new HDType[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; i++) array[i] = ObjCType.valueOf(prefix, actualTypeArguments[i]);
        return array;
    }

}
