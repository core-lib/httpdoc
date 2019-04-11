package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.type.ObjCClass;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * 参数代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 13:51
 **/
public class ObjCParameterFragment implements Fragment {
    private String name;
    private ObjCType type;
    private String variable;
    private String comment;

    public ObjCParameterFragment() {
    }

    public ObjCParameterFragment(String name, ObjCType type) {
        this.name = name;
        this.type = type;
        this.variable = name;
    }

    public ObjCParameterFragment(String name, ObjCType type, String variable) {
        this.name = name;
        this.type = type;
        this.variable = variable;
    }

    public ObjCParameterFragment(String name, ObjCType type, String variable, String comment) {
        this.name = name;
        this.type = type;
        this.variable = variable;
        this.comment = comment;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        for (ObjCClass dependency : type.dependencies()) imports.addAll(dependency.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        boolean simple = type.isPrimitive() || type.isTypedef() || type.isBlock();
        appender.append(variable).append(":(").append(type.getName()).append(simple ? "" : " *").append(")").append(variable);
    }

    public String getName() {
        return name;
    }

    public ObjCParameterFragment setName(String name) {
        this.name = name;
        return this;
    }

    public ObjCType getType() {
        return type;
    }

    public ObjCParameterFragment setType(ObjCType type) {
        this.type = type;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public ObjCParameterFragment setVariable(String variable) {
        this.variable = variable;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ObjCParameterFragment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCParameterFragment that = (ObjCParameterFragment) o;

        return variable != null ? variable.equals(that.variable) : that.variable == null;
    }

    @Override
    public int hashCode() {
        return variable != null ? variable.hashCode() : 0;
    }

}
