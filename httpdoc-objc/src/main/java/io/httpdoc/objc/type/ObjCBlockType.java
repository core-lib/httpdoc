package io.httpdoc.objc.type;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Objective-C Block 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-11 10:41
 **/
public class ObjCBlockType extends ObjCType {
    private final Map<String, ObjCType> parameters;

    public ObjCBlockType(Map<String, ObjCType> parameters) {
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder("void (^)");
        builder.append("(");
        int count = 0;
        for (Map.Entry<String, ObjCType> entry : parameters.entrySet()) {
            if (count++ > 0) builder.append(", ");
            String name = entry.getKey();
            String type = entry.getValue().getName();
            builder.append(type).append(entry.getValue().isPrimitive() || entry.getValue().isTypedef() || entry.getValue().isBlock() ? " " : " *").append(name);
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public Kind getKind() {
        return Kind.BLOCK;
    }

    @Override
    public Reference getReference() {
        return Reference.STRONG;
    }

    @Override
    public String getLocation() {
        return FOUNDATION;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public Set<ObjCClass> dependencies() {
        Set<ObjCClass> dependencies = new TreeSet<>();
        for (ObjCType objCType : parameters.values()) dependencies.addAll(objCType.dependencies());
        return dependencies;
    }

    public Map<String, ObjCType> getParameters() {
        return parameters;
    }

}
