package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.EnterMergedAppender;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjC;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 枚举定义代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:36
 **/
public class EnumDefinitionFragment implements Fragment {
    private NoteFragment noteFragment;
    private String name;
    private Set<EnumConstantFragment> constantFragments = new LinkedHashSet<>();

    public EnumDefinitionFragment() {
    }

    public EnumDefinitionFragment(String name) {
        this.name = name;
    }

    public EnumDefinitionFragment(String note, String name) {
        this.noteFragment = note != null ? new NoteFragment(note) : null;
        this.name = name;
    }

    public EnumDefinitionFragment(NoteFragment noteFragment, String name) {
        this.noteFragment = noteFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        for (EnumConstantFragment constant : constantFragments) imports.addAll(constant.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (noteFragment != null) noteFragment.joinTo(appender, preference);

        for (String include : imports()) appender.append(include).enter();

        appender.append("typedef NS_ENUM(NSInteger, ").append(name).append("){").enter();

        EnterMergedAppender indented = new EnterMergedAppender(new IndentAppender(appender, preference.getIndent()), 2);
        Iterator<EnumConstantFragment> iterator = constantFragments.iterator();
        while (iterator.hasNext()) {
            EnumConstantFragment fragment = iterator.next();
            fragment.joinTo(indented, preference);
            if (iterator.hasNext()) indented.append(",");
            indented.enter();
        }
        indented.close();

        appender.append("};");
    }

    public EnumDefinitionFragment addConstantFragment(String name) {
        return addConstantFragment(new EnumConstantFragment(name));
    }

    public EnumDefinitionFragment addConstantFragment(String name, Integer value) {
        return addConstantFragment(new EnumConstantFragment(name, value));
    }

    public EnumDefinitionFragment addConstantFragment(String note, String name) {
        return addConstantFragment(new EnumConstantFragment(note, name));
    }

    public EnumDefinitionFragment addConstantFragment(String note, String name, Integer value) {
        return addConstantFragment(new EnumConstantFragment(note, name, value));
    }

    public EnumDefinitionFragment addConstantFragment(NoteFragment noteFragment, String name, Integer value) {
        return addConstantFragment(new EnumConstantFragment(noteFragment, name, value));
    }

    public EnumDefinitionFragment addConstantFragment(EnumConstantFragment constantFragment) {
        constantFragments.add(constantFragment);
        return this;
    }

    public NoteFragment getNoteFragment() {
        return noteFragment;
    }

    public EnumDefinitionFragment setNoteFragment(NoteFragment noteFragment) {
        this.noteFragment = noteFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public EnumDefinitionFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<EnumConstantFragment> getConstantFragments() {
        return constantFragments;
    }

    public EnumDefinitionFragment setConstantFragments(Set<EnumConstantFragment> constantFragments) {
        this.constantFragments = constantFragments;
        return this;
    }

    /**
     * 枚举常量代码碎片
     *
     * @author 杨昌沛 646742615@qq.com
     * @date 2018-07-24 17:47
     **/
    public static class EnumConstantFragment implements Fragment, ObjC {
        private NoteFragment noteFragment;
        private String name;
        private Integer value;

        public EnumConstantFragment() {
        }

        public EnumConstantFragment(String name) {
            this(name, (Integer) null);
        }

        public EnumConstantFragment(String name, Integer value) {
            this((String) null, name, value);
        }

        public EnumConstantFragment(String note, String name) {
            this(note, name, null);
        }

        public EnumConstantFragment(String note, String name, Integer value) {
            this(note != null ? new NoteFragment(note) : null, name, value);
        }

        public EnumConstantFragment(NoteFragment noteFragment, String name, Integer value) {
            this.noteFragment = noteFragment;
            this.name = name;
            this.value = value;
        }

        @Override
        public Set<String> imports() {
            return Collections.singleton(FOUNDATION);
        }

        @Override
        public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
            if (noteFragment != null) noteFragment.joinTo(appender, preference);
            appender.append(name);
            if (value != null) appender.append(" = ").append(String.valueOf(value));
        }

        public NoteFragment getNoteFragment() {
            return noteFragment;
        }

        public EnumConstantFragment setNoteFragment(NoteFragment noteFragment) {
            this.noteFragment = noteFragment;
            return this;
        }

        public String getName() {
            return name;
        }

        public EnumConstantFragment setName(String name) {
            this.name = name;
            return this;
        }

        public Integer getValue() {
            return value;
        }

        public EnumConstantFragment setValue(Integer value) {
            this.value = value;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EnumConstantFragment that = (EnumConstantFragment) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

}
