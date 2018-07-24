package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.appender.TitleCasedAppender;
import io.httpdoc.core.fragment.CommentFragment;
import io.httpdoc.core.fragment.ConstructorFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.type.HDType;
import io.httpdoc.objective.c.Instancetype;
import io.httpdoc.objective.c.type.ObjCType;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Objective-C 构造器代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 9:51
 **/
public class ObjCConstructorFragment extends ConstructorFragment {

    public ObjCConstructorFragment() {
        this.name = "init";
        this.resultFragment = new ObjCResultFragment(ObjCType.valueOf("", HDType.valueOf(Instancetype.class)));
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
        TitleCasedAppender tca = new TitleCasedAppender(appender);
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            if (i == 0) appender.append("With");
            else appender.enter().append("    ");
            ParameterFragment fragment = parameterFragments.get(i);
            fragment.joinTo(tca, preference);
        }
        tca.close();
        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";").enter();
    }

    public ObjCConstructorFragment signature() {
        ObjCConstructorFragment signature = new ObjCConstructorFragment();
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
