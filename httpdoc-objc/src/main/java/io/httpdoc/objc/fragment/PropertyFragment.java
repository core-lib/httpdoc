package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 属性代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:40
 **/
public class PropertyFragment implements Fragment {
    private CommentFragment commentFragment;
    private ObjCType type;
    private String name;

    public PropertyFragment() {
    }

    public PropertyFragment(ObjCType type, String name) {
        this.type = type;
        this.name = name;
    }

    public PropertyFragment(String comment, ObjCType type, String name) {
        this.commentFragment = comment != null ? new CommentFragment(comment) : null;
        this.type = type;
        this.name = name;
    }

    public PropertyFragment(CommentFragment commentFragment, ObjCType type, String name) {
        this.commentFragment = commentFragment;
        this.type = type;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        if (commentFragment != null) imports.addAll(commentFragment.imports());
        imports.addAll(type.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.append("@property (nonatomic, ").append(type.getReferenceType()).append(") ");
        appender.append(type.getName()).append(type.isPrimitive() ? " " : " *");
        appender.append(name).append(";");
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public PropertyFragment setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public ObjCType getType() {
        return type;
    }

    public PropertyFragment setType(ObjCType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public PropertyFragment setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyFragment that = (PropertyFragment) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
