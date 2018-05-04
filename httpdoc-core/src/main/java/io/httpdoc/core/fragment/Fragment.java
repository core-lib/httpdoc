package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:26
 **/
public interface Fragment {

    <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException;

}
