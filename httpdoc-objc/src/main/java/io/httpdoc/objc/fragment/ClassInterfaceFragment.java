package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjCProtocol;
import io.httpdoc.objc.type.ObjCClass;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 接口代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:30
 **/
public class ClassInterfaceFragment implements Fragment {
    private String name;
    private ObjCClass superclass;
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

    public ClassInterfaceFragment setName(String name) {
        this.name = name;
        return this;
    }

    public ObjCClass getSuperclass() {
        return superclass;
    }

    public ClassInterfaceFragment setSuperclass(ObjCClass superclass) {
        this.superclass = superclass;
        return this;
    }

    public Set<ObjCProtocol> getProtocols() {
        return protocols;
    }

    public ClassInterfaceFragment setProtocols(Set<ObjCProtocol> protocols) {
        this.protocols = protocols;
        return this;
    }

    public Set<PropertyFragment> getPropertyFragments() {
        return propertyFragments;
    }

    public ClassInterfaceFragment setPropertyFragments(Set<PropertyFragment> propertyFragments) {
        this.propertyFragments = propertyFragments;
        return this;
    }

    public Set<SelectorFragment> getSelectorFragments() {
        return selectorFragments;
    }

    public ClassInterfaceFragment setSelectorFragments(Set<SelectorFragment> selectorFragments) {
        this.selectorFragments = selectorFragments;
        return this;
    }
}
