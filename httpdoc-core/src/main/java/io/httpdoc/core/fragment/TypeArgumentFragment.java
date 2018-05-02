package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 类型实参碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 10:04
 **/
public class TypeArgumentFragment implements Fragment {
    private String name;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

}
