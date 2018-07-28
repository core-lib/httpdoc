package io.httpdoc.objc.type;

import java.util.Set;
import java.util.TreeSet;

/**
 * ObjC 参数化类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:04
 **/
public class ObjCGenericType extends ObjCType {
    private ObjCClass rawType;
    private ObjCType[] actualTypeArguments;

    ObjCGenericType() {
    }

    public ObjCGenericType(ObjCClass rawType, ObjCType... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getName());
        builder.append("<");
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (i > 0) builder.append(", ");
            ObjCType argument = actualTypeArguments[i];
            builder.append(argument.getName());
            if (!argument.isPrimitive() && !argument.isTypedef()) builder.append(" *");
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public Kind getKind() {
        return Kind.GENERIC;
    }

    @Override
    public Reference getReference() {
        return rawType.getReference();
    }

    @Override
    public String getLocation() {
        return rawType.getLocation();
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public Set<ObjCClass> dependencies() {
        Set<ObjCClass> dependencies = new TreeSet<>();
        dependencies.addAll(rawType.dependencies());
        for (ObjCType objCType : actualTypeArguments) dependencies.addAll(objCType.dependencies());
        return dependencies;
    }

    public ObjCClass getRawType() {
        return rawType;
    }

    ObjCGenericType setRawType(ObjCClass rawType) {
        this.rawType = rawType;
        return this;
    }

    public ObjCType[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    ObjCGenericType setActualTypeArguments(ObjCType[] actualTypeArguments) {
        this.actualTypeArguments = actualTypeArguments;
        return this;
    }

}
