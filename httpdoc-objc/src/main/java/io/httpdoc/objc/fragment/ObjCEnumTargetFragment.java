package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 枚举实现代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:35
 **/
public class ObjCEnumTargetFragment implements Fragment {
    private ObjCCommentFragment commentFragment;
    private String name;
    private Set<EnumAssignFragment> assignFragments = new LinkedHashSet<>();

    public ObjCEnumTargetFragment() {
    }

    public ObjCEnumTargetFragment(String name) {
        this.name = name;
    }

    public ObjCEnumTargetFragment(String comment, String name) {
        this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
        this.name = name;
    }

    public ObjCEnumTargetFragment(ObjCCommentFragment commentFragment, String name) {
        this.commentFragment = commentFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        imports.add("#import \"" + name + ".h\"");
        for (EnumAssignFragment assign : assignFragments) imports.addAll(assign.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        for (String include : imports()) appender.append(include).enter();

        for (EnumAssignFragment fragment : assignFragments) {
            appender.enter();
            fragment.setType(this.name);
            fragment.joinTo(appender, preference);
        }
    }

    public ObjCEnumTargetFragment addAssignFragment(String name, String value) {
        return addAssignFragment(new EnumAssignFragment(this.name, name, value));
    }

    public ObjCEnumTargetFragment addAssignFragment(String comment, String name, String value) {
        return addAssignFragment(new EnumAssignFragment(comment, this.name, name, value));
    }

    public ObjCEnumTargetFragment addAssignFragment(ObjCCommentFragment commentFragment, String name, String value) {
        return addAssignFragment(new EnumAssignFragment(commentFragment, this.name, name, value));
    }

    private ObjCEnumTargetFragment addAssignFragment(EnumAssignFragment assignFragment) {
        assignFragments.add(assignFragment);
        return this;
    }

    public ObjCCommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ObjCEnumTargetFragment setCommentFragment(ObjCCommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCEnumTargetFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<EnumAssignFragment> getAssignFragments() {
        return assignFragments;
    }

    public ObjCEnumTargetFragment setAssignFragments(Set<EnumAssignFragment> assignFragments) {
        this.assignFragments = assignFragments;
        return this;
    }

    public static class EnumAssignFragment implements Fragment {
        private ObjCCommentFragment commentFragment;
        private String type;
        private String name;
        private String value;

        public EnumAssignFragment() {
        }

        public EnumAssignFragment(String type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public EnumAssignFragment(String comment, String type, String name, String value) {
            this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public EnumAssignFragment(ObjCCommentFragment commentFragment, String type, String name, String value) {
            this.commentFragment = commentFragment;
            this.type = type;
            this.name = name;
            this.value = value;
        }

        @Override
        public Set<String> imports() {
            return Collections.singleton("#import \"" + type + ".h\"");
        }

        @Override
        public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
            if (commentFragment != null) commentFragment.joinTo(appender, preference);
            appender.append(type).append(" const ").append(name).append(" = ").append("@\"").append(value).append("\";");
        }

        public ObjCCommentFragment getCommentFragment() {
            return commentFragment;
        }

        public EnumAssignFragment setCommentFragment(ObjCCommentFragment commentFragment) {
            this.commentFragment = commentFragment;
            return this;
        }

        public String getType() {
            return type;
        }

        public EnumAssignFragment setType(String type) {
            this.type = type;
            return this;
        }

        public String getName() {
            return name;
        }

        public EnumAssignFragment setName(String name) {
            this.name = name;
            return this;
        }

        public String getValue() {
            return value;
        }

        public EnumAssignFragment setValue(String value) {
            this.value = value;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EnumAssignFragment that = (EnumAssignFragment) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
