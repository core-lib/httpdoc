package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 类型形数碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 10:00
 **/
public class TypeParameterFragment implements Fragment {
    private String name;
    private SuperclassFragment superclassFragment;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

}
