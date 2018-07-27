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
            boolean simple = entry.getValue().isPrimitive() || entry.getValue().isTypedef();
            builder.append(type).append(simple ? "" : " *").append(" ").append(name);
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isTypedef() {
        return true;
    }

    @Override
    public String getReferenceType() {
        return STRONG;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        for (ObjCType type : parameters.values()) imports.addAll(type.imports());
        return imports;
    }

    public Map<String, ObjCType> getParameters() {
        return parameters;
    }


}
