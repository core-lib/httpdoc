package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 实例代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:38
 **/
public class InstanceBlockFragment extends BlockFragment {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        super.joinTo(appender, preference);
    }

}
