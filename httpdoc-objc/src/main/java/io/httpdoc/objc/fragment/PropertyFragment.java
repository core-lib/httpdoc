package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.Set;

/**
 * 属性代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:40
 **/
public class PropertyFragment implements Fragment {
    private ObjCType type;


    @Override
    public Set<String> imports() {
        return null;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }
}
