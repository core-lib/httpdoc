package io.httpdoc.objc;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.objc.fragment.ObjCParameterFragment;

import java.io.IOException;
import java.util.Set;

/**
 * 下划线Selector命名策略
 *
 * @author Payne 646742615@qq.com
 * 2019/3/29 16:17
 */
public class ObjCSELUnderlineNamingStrategy implements ObjCSELNamingStrategy {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference, String name, Set<ObjCParameterFragment> parameterFragments) throws IOException {
        appender.append(name);
        int index = 0;
        for (ObjCParameterFragment parameterFragment : parameterFragments) {
            if (index++ == 0) appender.append("_");
            else appender.enter().append("    ");
            parameterFragment.joinTo(appender, preference);
        }
    }
}