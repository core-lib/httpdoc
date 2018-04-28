package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;

import java.io.IOException;

/**
 * 静待代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:38
 **/
public class StaticFragment extends BlockFragment {

    @Override
    public <T extends Appender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("static");
        super.joinTo(appender, preference);
    }
}
