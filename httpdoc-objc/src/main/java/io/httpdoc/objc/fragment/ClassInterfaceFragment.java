package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.foundation.NSObject;
import io.httpdoc.objc.type.ObjCClass;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 接口代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:30
 **/
public class ClassInterfaceFragment implements Fragment {
    private CommentFragment commentFragment;
    private String name;
    private ObjCClass superclass = ObjCType.valueOf(NSObject.class);
    private Set<ObjCClass> protocols = new LinkedHashSet<>();
    private Set<PropertyFragment> propertyFragments = new LinkedHashSet<>();
    private Set<SelectorFragment> selectorFragments = new LinkedHashSet<>();

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        imports.add("#import " + superclass.getLocation());
        for (ObjCClass protocol : protocols) imports.add(protocol.getLocation());
        for (PropertyFragment propertyFragment : propertyFragments) imports.addAll(propertyFragment.imports());
        for (SelectorFragment selectorFragment : selectorFragments) imports.addAll(selectorFragment.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        appender.enter();
        for (String include : imports()) appender.append(include).enter();
        appender.enter();

        appender.append("@interface").append(" ").append(name);

        if (superclass != null) appender.append(" : ").append(superclass.getName());

        int index = 0;
        for (ObjCClass protocol : protocols) {
            if (index++ == 0) appender.append("<");
            else appender.append(", ");
            appender.append(protocol.getName());
            if (index == protocols.size()) appender.append(">");
        }

        appender.enter();
        for (PropertyFragment propertyFragment : propertyFragments) {
            appender.enter();
            propertyFragment.joinTo(appender, preference);
        }

        appender.enter();
        for (SelectorFragment selectorFragment : selectorFragments) {
            appender.enter();
            selectorFragment.joinTo(appender, preference);
            appender.enter();
        }

        appender.enter().append("@end");
    }

    public ClassInterfaceFragment addProtocol(ObjCClass protocol) {
        protocols.add(protocol);
        return this;
    }

    public ClassInterfaceFragment addPropertyFragment(ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(type, name));
    }

    public ClassInterfaceFragment addPropertyFragment(String comment, ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(comment, type, name));
    }

    public ClassInterfaceFragment addPropertyFragment(CommentFragment commentFragment, ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(commentFragment, type, name));
    }

    public ClassInterfaceFragment addPropertyFragment(PropertyFragment propertyFragment) {
        propertyFragments.add(propertyFragment);
        return this;
    }

    public ClassInterfaceFragment addSelectorFragment(ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name));
    }

    public ClassInterfaceFragment addSelectorFragment(ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name, comment));
    }

    public ClassInterfaceFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name));
    }

    public ClassInterfaceFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name, comment));
    }

    public ClassInterfaceFragment addSelectorFragment(SelectorFragment selectorFragment) {
        selectorFragments.add(selectorFragment);
        return this;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ClassInterfaceFragment setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
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

    public Set<ObjCClass> getProtocols() {
        return protocols;
    }

    public ClassInterfaceFragment setProtocols(Set<ObjCClass> protocols) {
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
