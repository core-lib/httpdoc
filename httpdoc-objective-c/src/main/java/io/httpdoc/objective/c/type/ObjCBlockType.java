package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Objective-C Block 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-11 10:41
 **/
public class ObjCBlockType extends HDType {
    private Map<String, HDType> parameters = new LinkedHashMap<>();

    protected ObjCBlockType() {
    }

    public ObjCBlockType(Map<String, HDType> parameters) {
        this.parameters = parameters;
    }

    @Override
    public CharSequence getAbbrName() {
        StringBuilder builder = new StringBuilder("void (^)");
        builder.append("(");
        int count = 0;
        for (Map.Entry<String, HDType> entry : parameters.entrySet()) {
            if (count++ > 0) builder.append(", ");
            CharSequence abbrName = entry.getValue().getAbbrName();
            builder.append(abbrName).append(" ").append(entry.getKey());
        }
        builder.append(")");
        return builder;
    }

    @Override
    public CharSequence getTypeName() {
        StringBuilder builder = new StringBuilder("void (^)");
        builder.append("(");
        int count = 0;
        for (Map.Entry<String, HDType> entry : parameters.entrySet()) {
            if (count++ > 0) builder.append(", ");
            CharSequence typeName = entry.getValue().getTypeName();
            builder.append(typeName).append(" ").append(entry.getKey());
        }
        builder.append(")");
        return builder;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        for (HDType type : parameters.values()) imports.addAll(type.imports());
        return imports;
    }

    public Map<String, HDType> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, HDType> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCBlockType that = (ObjCBlockType) o;

        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }

}
