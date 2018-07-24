package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.EnterMergedAppender;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
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
    private Set<EnumConstantFragment> constantFragments;

    @Override
    public Set<String> imports() {
        return null;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (noteFragment != null) noteFragment.joinTo(appender, preference);
        appender.append("typedef NS_ENUM(NSInteger, ").append(name).append("){");
        EnterMergedAppender indented = new EnterMergedAppender(new IndentAppender(appender, preference.getIndent()), 2);
        // 枚举常量

        appender.append("};");
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

    public Set<EnumConstantFragment> getConstantFragments() {
        return constantFragments;
    }

    public void setConstantFragments(Set<EnumConstantFragment> constantFragments) {
        this.constantFragments = constantFragments;
    }

}
