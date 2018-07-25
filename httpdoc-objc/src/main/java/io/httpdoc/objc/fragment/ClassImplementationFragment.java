package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjCProtocol;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 实现代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:30
 **/
public class ClassImplementationFragment implements Fragment {
    private String name;
    private Set<ObjCProtocol> protocols = new LinkedHashSet<>();
    private Set<PropertyFragment> propertyFragments = new LinkedHashSet<>();
    private Set<SelectorFragment> selectorFragments = new LinkedHashSet<>();

    @Override
    public Set<String> imports() {
        return null;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

    public String getName() {
        return name;
    }

    public ClassImplementationFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ObjCProtocol> getProtocols() {
        return protocols;
    }

    public ClassImplementationFragment setProtocols(Set<ObjCProtocol> protocols) {
        this.protocols = protocols;
        return this;
    }

    public Set<PropertyFragment> getPropertyFragments() {
        return propertyFragments;
    }

    public ClassImplementationFragment setPropertyFragments(Set<PropertyFragment> propertyFragments) {
        this.propertyFragments = propertyFragments;
        return this;
    }

    public Set<SelectorFragment> getSelectorFragments() {
        return selectorFragments;
    }

    public ClassImplementationFragment setSelectorFragments(Set<SelectorFragment> selectorFragments) {
        this.selectorFragments = selectorFragments;
        return this;
    }
}
