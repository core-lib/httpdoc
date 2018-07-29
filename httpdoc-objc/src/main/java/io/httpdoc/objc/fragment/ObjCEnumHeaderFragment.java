package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjC;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 枚举接口代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:34
 **/
public class ObjCEnumHeaderFragment implements Fragment {
    private String pkg;
    private ObjCCommentFragment commentFragment;
    private String name;
    private Set<EnumExportFragment> exportFragments = new LinkedHashSet<>();

    public ObjCEnumHeaderFragment() {
    }

    public ObjCEnumHeaderFragment(String name) {
        this.name = name;
    }

    public ObjCEnumHeaderFragment(String comment, String name) {
        this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
        this.name = name;
    }

    public ObjCEnumHeaderFragment(ObjCCommentFragment commentFragment, String name) {
        this.commentFragment = commentFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        imports.add("#import " + ObjC.FOUNDATION);
        for (EnumExportFragment export : exportFragments) imports.addAll(export.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        for (String include : imports()) appender.append(include).enter();

        appender.enter().append("typedef NSString * ").append(name).append(" NS_STRING_ENUM;").enter();

        for (EnumExportFragment fragment : exportFragments) {
            appender.enter();
            fragment.setType(this.name);
            fragment.joinTo(appender, preference);
        }
    }

    public ObjCEnumHeaderFragment addExportFragment(String name) {
        return addExportFragment(new EnumExportFragment(this.name, name));
    }

    public ObjCEnumHeaderFragment addExportFragment(String comment, String name) {
        return addExportFragment(new EnumExportFragment(comment, this.name, name));
    }

    public ObjCEnumHeaderFragment addExportFragment(ObjCCommentFragment commentFragment, String name) {
        return addExportFragment(new EnumExportFragment(commentFragment, this.name, name));
    }

    private ObjCEnumHeaderFragment addExportFragment(EnumExportFragment exportFragment) {
        exportFragments.add(exportFragment);
        return this;
    }

    public String getPkg() {
        return pkg;
    }

    public ObjCEnumHeaderFragment setPkg(String pkg) {
        this.pkg = pkg;
        return this;
    }

    public ObjCCommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ObjCEnumHeaderFragment setCommentFragment(ObjCCommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCEnumHeaderFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<EnumExportFragment> getExportFragments() {
        return exportFragments;
    }

    public ObjCEnumHeaderFragment setExportFragments(Set<EnumExportFragment> exportFragments) {
        this.exportFragments = exportFragments;
        return this;
    }

    public static class EnumExportFragment implements Fragment {
        private ObjCCommentFragment commentFragment;
        private String type;
        private String name;

        public EnumExportFragment() {
        }

        public EnumExportFragment(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public EnumExportFragment(String comment, String type, String name) {
            this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
            this.type = type;
            this.name = name;
        }

        public EnumExportFragment(ObjCCommentFragment commentFragment, String type, String name) {
            this.commentFragment = commentFragment;
            this.type = type;
            this.name = name;
        }

        @Override
        public Set<String> imports() {
            return Collections.singleton("#import " + ObjC.FOUNDATION);
        }

        @Override
        public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
            if (commentFragment != null) commentFragment.joinTo(appender, preference);
            appender.append("FOUNDATION_EXPORT ").append(type).append(" const ").append(name).append(";");
        }

        public ObjCCommentFragment getCommentFragment() {
            return commentFragment;
        }

        public EnumExportFragment setCommentFragment(ObjCCommentFragment commentFragment) {
            this.commentFragment = commentFragment;
            return this;
        }

        public String getType() {
            return type;
        }

        public EnumExportFragment setType(String type) {
            this.type = type;
            return this;
        }

        public String getName() {
            return name;
        }

        public EnumExportFragment setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EnumExportFragment that = (EnumExportFragment) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

    }
}
