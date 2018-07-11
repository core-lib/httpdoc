package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.CommentFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Set;

public class ObjCMethodFragment extends MethodFragment {

    public ObjCMethodFragment() {
    }

    public ObjCMethodFragment(int modifier) {
        super(modifier);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        String comment = comment();
        CommentFragment commentFragment = new CommentFragment(comment);
        commentFragment.joinTo(appender, preference);

        if (Modifier.isStatic(modifier)) appender.append("+ ");
        else appender.append("- ");

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        if (name != null) appender.append(name);
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            if (i == 0) appender.append("_");
            else appender.enter().append("    ");
            ParameterFragment fragment = parameterFragments.get(i);
            fragment.joinTo(appender, preference);
        }
        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";").enter();
    }

    @Override
    public Set<String> imports() {
        return super.imports();
    }

    public ObjCMethodFragment signature() {
        ObjCMethodFragment signature = new ObjCMethodFragment(modifier);
        signature.setAnnotations(annotations);
        signature.setTypeVariableFragments(typeVariableFragments);
        signature.setResultFragment(resultFragment);
        signature.setName(name);
        signature.setParameterFragments(parameterFragments);
        signature.setExceptionFragments(exceptionFragments);
        signature.setComment(comment);
        return signature;
    }

}
