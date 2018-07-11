package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.util.Set;

public class ObjCParameterFragment extends ParameterFragment {

    public ObjCParameterFragment() {
    }

    public ObjCParameterFragment(int modifier) {
        super(modifier);
    }

    public ObjCParameterFragment(HDType type, String name) {
        super(type, name);
    }

    public ObjCParameterFragment(int modifier, HDType type, String name) {
        super(modifier, type, name);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append(name).append(":").append("(").append(type).append(")").append(name);
    }

    @Override
    public Set<String> imports() {
        return super.imports();
    }
}
