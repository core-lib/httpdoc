package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.JavaClass;
import io.httpdoc.core.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 父类碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 10:03
 **/
public class SuperclassFragment implements Fragment {
    private JavaType type;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

}
