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
 * 返回值代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 13:12
 **/
public class ResultFragment implements Fragment {
    private ObjCType type;
    private String comment;

    public ResultFragment() {
    }

    public ResultFragment(ObjCType type) {
        this.type = type;
    }

    public ResultFragment(ObjCType type, String comment) {
        this.type = type;
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
        if (type != null) appender.append("(").append(type.getName()).append(type.isPrimitive() || type.isTypedef() ? "" : " *").append(")");
        else appender.append("(void)");
    }

    public ObjCType getType() {
        return type;
    }

    public ResultFragment setType(ObjCType type) {
        this.type = type;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ResultFragment setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
