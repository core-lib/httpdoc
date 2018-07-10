package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.FieldFragment;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Objective-C字段代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 19:52
 **/
public class ObjCFieldFragment extends FieldFragment {

    public ObjCFieldFragment() {
    }

    public ObjCFieldFragment(int modifier) {
        super(modifier);
    }

    public ObjCFieldFragment(HDType type, String name) {
        super(type, name);
    }

    public ObjCFieldFragment(HDType type, String name, CharSequence assignment) {
        super(type, name, assignment);
    }

    public ObjCFieldFragment(int modifier, HDType type, String name) {
        super(modifier, type, name);
    }

    public ObjCFieldFragment(int modifier, HDType type, String name, CharSequence assignment) {
        super(modifier, type, name, assignment);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        if (Modifier.isStatic(modifier)) appender.append("static ");
        else appender.append("@property ");
        appender.append(type).append(" ").append(name);
        if (assignmentFragment != null) assignmentFragment.joinTo(appender, preference);
        appender.append(";");
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        if (commentFragment != null) imports.addAll(commentFragment.imports());
        if (type != null) imports.addAll(type.imports());
        if (assignmentFragment != null) imports.addAll(assignmentFragment.imports());
        return imports;
    }

}
