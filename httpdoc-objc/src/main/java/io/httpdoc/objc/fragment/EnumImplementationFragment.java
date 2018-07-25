package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 枚举实现代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:35
 **/
public class EnumImplementationFragment implements Fragment {
    private NoteFragment noteFragment;
    private String name;
    private Set<EnumAssignFragment> assignFragments = new LinkedHashSet<>();

    public EnumImplementationFragment() {
    }

    public EnumImplementationFragment(String name) {
        this.name = name;
    }

    public EnumImplementationFragment(String note, String name) {
        this.noteFragment = note != null ? new NoteFragment(note) : null;
        this.name = name;
    }

    public EnumImplementationFragment(NoteFragment noteFragment, String name) {
        this.noteFragment = noteFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        for (EnumAssignFragment assign : assignFragments) imports.addAll(assign.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (noteFragment != null) noteFragment.joinTo(appender, preference);

        for (String include : imports()) appender.append(include).enter();

        for (EnumAssignFragment fragment : assignFragments) {
            appender.enter();
            fragment.setType(this.name);
            fragment.joinTo(appender, preference);
        }
    }

    public EnumImplementationFragment addAssignFragment(String name, String value) {
        return addAssignFragment(new EnumAssignFragment(this.name, name, value));
    }

    public EnumImplementationFragment addAssignFragment(String note, String name, String value) {
        return addAssignFragment(new EnumAssignFragment(note, this.name, name, value));
    }

    public EnumImplementationFragment addAssignFragment(NoteFragment noteFragment, String name, String value) {
        return addAssignFragment(new EnumAssignFragment(noteFragment, this.name, name, value));
    }

    private EnumImplementationFragment addAssignFragment(EnumAssignFragment assignFragment) {
        assignFragments.add(assignFragment);
        return this;
    }

    public NoteFragment getNoteFragment() {
        return noteFragment;
    }

    public EnumImplementationFragment setNoteFragment(NoteFragment noteFragment) {
        this.noteFragment = noteFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public EnumImplementationFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<EnumAssignFragment> getAssignFragments() {
        return assignFragments;
    }

    public EnumImplementationFragment setAssignFragments(Set<EnumAssignFragment> assignFragments) {
        this.assignFragments = assignFragments;
        return this;
    }

    public static class EnumAssignFragment implements Fragment {
        private NoteFragment noteFragment;
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

        public EnumAssignFragment(String note, String type, String name, String value) {
            this.noteFragment = note != null ? new NoteFragment(note) : null;
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public EnumAssignFragment(NoteFragment noteFragment, String type, String name, String value) {
            this.noteFragment = noteFragment;
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
            if (noteFragment != null) noteFragment.joinTo(appender, preference);
            appender.append(type).append(" const ").append(name).append(" = ").append("@\"").append(value).append("\";");
        }

        public NoteFragment getNoteFragment() {
            return noteFragment;
        }

        public EnumAssignFragment setNoteFragment(NoteFragment noteFragment) {
            this.noteFragment = noteFragment;
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
