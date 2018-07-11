package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.util.Set;

public class ObjCClass extends HDClass {
    private final HDClass clazz;

    public ObjCClass(HDClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public Set<String> imports() {
        return clazz.imports();
    }

    @Override
    public CharSequence getAbbrName() {
        return clazz.getAbbrName();
    }

    @Override
    public CharSequence getTypeName() {
        return clazz.getTypeName();
    }

    @Override
    public int length() {
        return clazz.length();
    }

    @Override
    public char charAt(int index) {
        return clazz.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return clazz.subSequence(start, end);
    }

    @Override
    public Category getCategory() {
        return clazz.getCategory();
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public HDType getComponentType() {
        HDType componentType = clazz.getComponentType();
        if (componentType == null) return null;
        return ObjCType.valueOf(componentType);
    }

    @Override
    public HDClass getEnclosingType() {
        HDClass enclosingType = clazz.getEnclosingType();
        if (enclosingType == null) return null;
        return new HDClass(enclosingType);
    }

    @Override
    public HDTypeVariable[] getTypeParameters() {
        HDTypeVariable[] typeParameters = clazz.getTypeParameters();
        if (typeParameters == null) return null;
        HDTypeVariable[] array = new HDTypeVariable[typeParameters.length];
        for (int i = 0; i < typeParameters.length; i++) array[i] = new ObjCTypeVariable(typeParameters[i]);
        return array;
    }

}
