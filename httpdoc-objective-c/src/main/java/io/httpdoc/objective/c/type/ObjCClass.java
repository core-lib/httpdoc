package io.httpdoc.objective.c.type;

import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;
import io.httpdoc.objective.c.ObjC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ObjCClass extends HDClass implements ObjC {
    public static final List<String> PRIMARIES = Arrays.asList("id", "bool", "short", "char", "int", "float", "long", "double");
    private final String prefix;
    private final HDClass clazz;

    public ObjCClass(String prefix, HDClass clazz) {
        this.prefix = prefix;
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

            case "io.httpdoc.objective.c.Id":
                return ObjCReferenceType.STRONG;
            case "java.lang.Error":
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

    public String getIntactName() {
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
                return "NSNumber";
            case "java.lang.Byte":
                return "NSNumber";
            case "java.lang.Short":
                return "NSNumber";
            case "java.lang.Character":
                return "NSNumber";
            case "java.lang.Integer":
                return "NSNumber";
            case "java.lang.Float":
                return "NSNumber";
            case "java.lang.Long":
                return "NSNumber";
            case "java.lang.Double":
                return "NSNumber";

            case "void":
                return "void";

            case "java.lang.String":
                return "NSString";
            case "java.math.BigDecimal":
                return "NSNumber";
            case "java.util.Date":
                return "NSDate";
            case "java.io.File":
                return "NSData";
            case "java.lang.Object":
                return "NSObject";
            case "java.util.List":
                return "NSArray";
            case "java.util.Map":
                return "NSDictionary";

            case "io.httpdoc.objective.c.Id":
                return "id";
            case "java.lang.Error":
                return "NSError";
        }
        String[] names = name.split("\\.");
        int index = names.length - 1;
        names[index] = prefix + names[index];
        return StringKit.join('.', names);
    }

    public String getSimpleName() {
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
                return "NSNumber";
            case "java.lang.Byte":
                return "NSNumber";
            case "java.lang.Short":
                return "NSNumber";
            case "java.lang.Character":
                return "NSNumber";
            case "java.lang.Integer":
                return "NSNumber";
            case "java.lang.Float":
                return "NSNumber";
            case "java.lang.Long":
                return "NSNumber";
            case "java.lang.Double":
                return "NSNumber";

            case "void":
                return "void";

            case "java.lang.String":
                return "NSString";
            case "java.math.BigDecimal":
                return "NSNumber";
            case "java.util.Date":
                return "NSDate";
            case "java.io.File":
                return "NSData";
            case "java.lang.Object":
                return "NSObject";
            case "java.util.List":
                return "NSArray";
            case "java.util.Map":
                return "NSDictionary";

            case "io.httpdoc.objective.c.Id":
                return "id";
            case "java.lang.Error":
                return "NSError";
        }
        String[] names = name.split("\\.");
        int index = names.length - 1;
        return prefix + names[index];
    }

    @Override
    public Set<String> imports() {
        String name = clazz.getName();
        switch (name) {
            case "boolean":
                return Collections.emptySet();
            case "byte":
                return Collections.emptySet();
            case "short":
                return Collections.emptySet();
            case "char":
                return Collections.emptySet();
            case "int":
                return Collections.emptySet();
            case "float":
                return Collections.emptySet();
            case "long":
                return Collections.emptySet();
            case "double":
                return Collections.emptySet();

            case "java.lang.Boolean":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Byte":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Short":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Character":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Integer":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Float":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Long":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Double":
                return Collections.singleton(ObjC.FOUNDATION);

            case "void":
                return Collections.emptySet();

            case "java.lang.String":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.math.BigDecimal":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.util.Date":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.io.File":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.lang.Object":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.util.List":
                return Collections.singleton(ObjC.FOUNDATION);
            case "java.util.Map":
                return Collections.singleton(ObjC.FOUNDATION);

            case "io.httpdoc.objective.c.Id":
                return Collections.emptySet();
            case "java.lang.Error":
                return Collections.singleton(ObjC.FOUNDATION);
        }

        switch (clazz.getCategory()) {
            case INTERFACE:// 接口类型
                return Collections.singleton(getSimpleName());
            case CLASS: // 实现类型
                return Collections.singleton(getSimpleName());
            case ENUM: // 枚举类型
                return Collections.singleton("\"" + getSimpleName() + ".h\"");
            case ARRAY:// 数组类型
                return getComponentType().imports();
            default:// 不支持类型
                throw new IllegalStateException();
        }
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
                return "NSNumber *";
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
                return "NSData *";
            case "java.lang.Object":
                return "NSObject *";
            case "java.util.List":
                return "NSArray";
            case "java.util.Map":
                return "NSDictionary";

            case "io.httpdoc.objective.c.Id":
                return "id";
            case "java.lang.Error":
                return "NSError *";
        }

        switch (clazz.getCategory()) {
            case INTERFACE:// 接口类型
                return prefix + (name.substring(name.lastIndexOf('.') + 1)) + " *";
            case CLASS: // 实现类型
                return prefix + (name.substring(name.lastIndexOf('.') + 1)) + " *";
            case ENUM: // 枚举类型
                return prefix + (name.substring(name.lastIndexOf('.') + 1));
            default:// 不支持类型
                throw new IllegalStateException();
        }
    }

    @Override
    public HDType getComponentType() {
        HDType componentType = clazz.getComponentType();
        if (componentType == null) return null;
        return ObjCType.valueOf(prefix, componentType);
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
        for (int i = 0; i < typeParameters.length; i++) array[i] = new ObjCTypeVariable(prefix, typeParameters[i]);
        return array;
    }

}
