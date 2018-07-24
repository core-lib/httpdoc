package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
import java.util.Set;

/**
 * 枚举常量代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:47
 **/
public class EnumConstantFragment implements Fragment {
    private NoteFragment noteFragment;
    private String name;
    private Integer value;

    @Override
    public Set<String> imports() {
        return null;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

    public NoteFragment getNoteFragment() {
        return noteFragment;
    }

    public void setNoteFragment(NoteFragment noteFragment) {
        this.noteFragment = noteFragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
