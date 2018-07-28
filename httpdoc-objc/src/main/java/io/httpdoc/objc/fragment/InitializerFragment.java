package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.objc.foundation.Cinstancetype;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;

public class InitializerFragment extends SelectorFragment {

    public InitializerFragment() {
        super(true, new ResultFragment(ObjCType.valueOf(Cinstancetype.class)), "init");
    }

    public InitializerFragment(String comment) {
        super(true, new ResultFragment(ObjCType.valueOf(Cinstancetype.class)), "init", comment);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        String comment = comment();
        CommentFragment commentFragment = new CommentFragment(comment);
        commentFragment.joinTo(appender, preference);
        if (instantial) appender.append("- ");
        else appender.append("+ ");

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        else appender.append("(void)");

        appender.append(name);
        int index = 0;
        for (ParameterFragment parameterFragment : parameterFragments) {
            if (index++ == 0) appender.append("With");
            else appender.enter().append("    ");
            parameterFragment.joinTo(appender, preference);
        }

        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";");
    }

}
