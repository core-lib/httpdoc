package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.ResultFragment;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ObjCResultFragment extends ResultFragment {

    public ObjCResultFragment() {
    }

    public ObjCResultFragment(HDType type) {
        super(type);
    }

    public ObjCResultFragment(HDType type, String comment) {
        super(type, comment);
    }

    @Override
    public Set<String> imports() {
        return type != null && !type.equals(VOID) ? type.imports() : Collections.<String>emptySet();
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (type != null) appender.append("(").append(type).append(")");
        else appender.append("(void)");
    }

}
