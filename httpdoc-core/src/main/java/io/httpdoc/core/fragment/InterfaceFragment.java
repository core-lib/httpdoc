package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 10:07
 **/
public class InterfaceFragment implements Fragment {
    private String name;
    private List<TypeArgumentFragment> typeArgumentFragments = new ArrayList<>();

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

    public List<TypeArgumentFragment> getTypeArgumentFragments() {
        return typeArgumentFragments;
    }

    public void setTypeArgumentFragments(List<TypeArgumentFragment> typeArgumentFragments) {
        this.typeArgumentFragments = typeArgumentFragments;
    }
}
