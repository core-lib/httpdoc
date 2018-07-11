package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.*;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDTypeVariable;

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
        StringBuilder builder = new StringBuilder(comment != null ? comment : "").append('\n');
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            ParameterFragment fragment = parameterFragments.get(i);
            String name = fragment.getName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param ").append(name).append(" ").append(comment);
        }
        for (int i = 0; typeVariableFragments != null && i < typeVariableFragments.size(); i++) {
            TypeVariableFragment fragment = typeVariableFragments.get(i);
            HDTypeVariable typeVariable = fragment.getTypeVariable();
            String name = typeVariable.getName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param <").append(name).append("> ").append(comment);
        }
        if (resultFragment != null) {
            String comment = resultFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@return ").append(" ").append(comment);
        }
        for (int i = 0; exceptionFragments != null && i < exceptionFragments.size(); i++) {
            ExceptionFragment fragment = exceptionFragments.get(i);
            HDClass type = fragment.getType();
            CharSequence name = type.getAbbrName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@throws ").append(name).append(" ").append(comment);
        }
        CommentFragment commentFragment = new CommentFragment(builder.toString());
        commentFragment.joinTo(appender, preference);

        if (Modifier.isStatic(modifier)) appender.append("+ ");
        else appender.append("- ");

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        if (name != null) appender.append(name);
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            if (i == 0) appender.append("_");
            else appender.append(" ");
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
}
