package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;

import java.io.IOException;

/**
 * 注解碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:41
 **/
public class AnnotationFragment implements Fragment {

    @Override
    public <T extends Appender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }
}
