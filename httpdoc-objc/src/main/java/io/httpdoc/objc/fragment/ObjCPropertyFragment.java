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
 * 属性代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:40
 **/
public class ObjCPropertyFragment implements Fragment {
    private ObjCCommentFragment commentFragment;
    private ObjCType type;
    private String name;

    public ObjCPropertyFragment() {
    }

    public ObjCPropertyFragment(ObjCType type, String name) {
        this.type = type;
        this.name = name;
    }

    public ObjCPropertyFragment(String comment, ObjCType type, String name) {
        this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
        this.type = type;
        this.name = name;
    }

    public ObjCPropertyFragment(ObjCCommentFragment commentFragment, ObjCType type, String name) {
        this.commentFragment = commentFragment;
        this.type = type;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        if (commentFragment != null) imports.addAll(commentFragment.imports());
        for (ObjCClass dependency : type.dependencies()) imports.addAll(dependency.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.append("@property (nonatomic, ").append(type.getReference().name().toLowerCase()).append(") ");
        appender.append(type.getName()).append(type.isPrimitive() || type.isTypedef() ? " " : " *");
        appender.append(name).append(";");
    }

    public ObjCCommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ObjCPropertyFragment setCommentFragment(ObjCCommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public ObjCType getType() {
        return type;
    }

    public ObjCPropertyFragment setType(ObjCType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCPropertyFragment setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCPropertyFragment that = (ObjCPropertyFragment) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
