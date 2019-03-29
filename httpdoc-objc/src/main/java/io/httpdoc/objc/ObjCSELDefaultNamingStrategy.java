package io.httpdoc.objc;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.appender.TitleCasedAppender;
import io.httpdoc.objc.fragment.ObjCParameterFragment;

import java.io.IOException;
import java.util.Set;

/**
 * 默认的Selector命名策略
 *
 * @author Payne 646742615@qq.com
 * 2019/3/29 15:45
 */
public class ObjCSELDefaultNamingStrategy implements ObjCSELNamingStrategy {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference, String name, Set<ObjCParameterFragment> parameterFragments) throws IOException {
        appender.append(name);
        TitleCasedAppender tca = new TitleCasedAppender(appender);
        int index = 0;
        for (ObjCParameterFragment parameterFragment : parameterFragments) {
            if (index++ == 0) appender.append("With");
            else appender.enter().append("    ");
            parameterFragment.joinTo(tca, preference);
        }
        tca.close();
    }
}
