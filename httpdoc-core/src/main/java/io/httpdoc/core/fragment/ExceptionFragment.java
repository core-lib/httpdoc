package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 异常碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 17:45
 **/
public class ExceptionFragment implements Fragment {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

}
