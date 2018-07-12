package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.CommentFragment;
import io.httpdoc.core.fragment.ConstantFragment;
import io.httpdoc.objective.c.type.ObjCClass;

import java.io.IOException;
import java.util.Set;

public class ObjCConstantFragment extends ConstantFragment {
    protected final ObjCClass clazz;

    public ObjCConstantFragment(ObjCClass clazz) {
        this.clazz = clazz;
    }

    public ObjCConstantFragment(String name, ObjCClass clazz) {
        super(name);
        this.clazz = clazz;
    }

    public ObjCConstantFragment(CommentFragment commentFragment, String name, ObjCClass clazz) {
        super(commentFragment, name);
        this.clazz = clazz;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.append(clazz.getSimpleName()).append(name);
    }

    @Override
    public Set<String> imports() {
        return super.imports();
    }
}
