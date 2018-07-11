package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.FieldFragment;
import io.httpdoc.objective.c.type.ObjC;

import java.io.IOException;

/**
 * Objective-C 字段代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-11 14:46
 **/
public class ObjCFieldFragment extends FieldFragment {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.append("@property (nonatomic, ").append(((ObjC) type).getReferenceType().name().toLowerCase()).append(") ");
        appender.append(type).append(" ").append(name);
        if (assignmentFragment != null) assignmentFragment.joinTo(appender, preference);
        appender.append(";");
    }

}
