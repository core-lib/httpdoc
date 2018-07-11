package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ObjCClass extends HDClass implements ObjC {
    private static final List<String> PRIMARIES = Arrays.asList("bool", "short", "char", "int", "float", "long", "double");
    private final HDClass clazz;

    public ObjCClass(HDClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        String name = clazz.getName();
        switch (name) {
            case "boolean":
                return ObjCReferenceType.ASSIGN;
            case "byte":
                return ObjCReferenceType.ASSIGN;
            case "short":
                return ObjCReferenceType.ASSIGN;
            case "char":
                return ObjCReferenceType.ASSIGN;
            case "int":
                return ObjCReferenceType.ASSIGN;
            case "float":
                return ObjCReferenceType.ASSIGN;
            case "long":
                return ObjCReferenceType.ASSIGN;
            case "double":
                return ObjCReferenceType.ASSIGN;

            case "java.lang.Boolean":
                return ObjCReferenceType.STRONG;
            case "java.lang.Byte":
                return ObjCReferenceType.STRONG;
            case "java.lang.Short":
                return ObjCReferenceType.STRONG;
            case "java.lang.Character":
                return ObjCReferenceType.STRONG;
            case "java.lang.Integer":
                return ObjCReferenceType.STRONG;
            case "java.lang.Float":
                return ObjCReferenceType.STRONG;
            case "java.lang.Long":
                return ObjCReferenceType.STRONG;
            case "java.lang.Double":
                return ObjCReferenceType.STRONG;

            case "void":
                return ObjCReferenceType.ASSIGN;

            case "java.lang.String":
                return ObjCReferenceType.COPY;
            case "java.math.BigDecimal":
                return ObjCReferenceType.STRONG;
            case "java.util.Date":
                return ObjCReferenceType.STRONG;
            case "java.io.File":
                return ObjCReferenceType.STRONG;
            case "java.lang.Object":
                return ObjCReferenceType.STRONG;
            case "java.util.List":
                return ObjCReferenceType.COPY;
            case "java.util.Map":
                return ObjCReferenceType.COPY;

            case "io.httpdoc.objective.c.ID":
                return ObjCReferenceType.STRONG;
        }

        switch (clazz.getCategory()) {
            case CLASS:
                return ObjCReferenceType.STRONG;
            case ARRAY:
                return ObjCReferenceType.COPY;
            default:
                return ObjCReferenceType.ASSIGN;
        }
    }

    @Override
    public Set<String> imports() {
        return getComponentType() != null
                ? getComponentType().imports()
                : getEnclosingType() != null
                ? getEnclosingType().imports()
                : PRIMARIES.contains(getName())
                ? Collections.<String>emptySet()
                : Collections.singleton(getName());
    }

    @Override
    public CharSequence getAbbrName() {
        HDType componentType = getComponentType();
        HDClass enclosingType = getEnclosingType();
        return componentType != null
                ? "NSArray<" + (PRIMARIES.contains(String.valueOf(componentType.getAbbrName())) ? "NSNumber *" : componentType.getAbbrName()) + "> *"
                : enclosingType != null
                ? enclosingType.getAbbrName() + "." + getName()
                : getName().substring(getName().lastIndexOf('.') + 1);
    }

    @Override
    public CharSequence getTypeName() {
        HDType componentType = getComponentType();
        HDClass enclosingType = getEnclosingType();
        return componentType != null
                ? "NSArray<" + (PRIMARIES.contains(String.valueOf(componentType.getAbbrName())) ? "NSNumber *" : componentType.getAbbrName()) + "> *"
                : enclosingType != null
                ? enclosingType.getAbbrName() + "." + getName()
                : getName();
    }

    @Override
    public Category getCategory() {
        return clazz.getCategory();
    }

    @Override
    public String getName() {
        String name = clazz.getName();
        switch (name) {
            case "boolean":
                return "bool";
            case "byte":
                return "int";
            case "short":
                return "short";
            case "char":
                return "char";
            case "int":
                return "int";
            case "float":
                return "float";
            case "long":
                return "long";
            case "double":
                return "double";

            case "java.lang.Boolean":
                return "NSNumber *";
            case "java.lang.Byte":
                return "NSNumber *";
            case "java.lang.Short":
                return "NSNumber *";
            case "java.lang.Character":
                return "NSNumber";
            case "java.lang.Integer":
                return "NSNumber *";
            case "java.lang.Float":
                return "NSNumber *";
            case "java.lang.Long":
                return "NSNumber *";
            case "java.lang.Double":
                return "NSNumber *";

            case "void":
                return "void";

            case "java.lang.String":
                return "NSString *";
            case "java.math.BigDecimal":
                return "NSNumber *";
            case "java.util.Date":
                return "NSDate *";
            case "java.io.File":
                return "NSDate *";
            case "java.lang.Object":
                return "NSObject *";
            case "java.util.List":
                return "NSArray";
            case "java.util.Map":
                return "NSDictionary";

            case "io.httpdoc.objective.c.ID":
                return "id";
        }

        switch (clazz.getCategory()) {
            case CLASS:
                return name + " *";
            default:
                return name;
        }
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
