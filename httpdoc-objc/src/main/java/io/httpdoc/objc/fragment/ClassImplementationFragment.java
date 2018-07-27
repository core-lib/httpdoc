package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjCProtocol;
import io.httpdoc.objc.type.ObjCType;

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
    private CommentFragment commentFragment;
    private String name;
    private Set<ObjCProtocol> protocols = new LinkedHashSet<>();
    private Set<PropertyFragment> propertyFragments = new LinkedHashSet<>();
    private Set<SelectorFragment> selectorFragments = new LinkedHashSet<>();

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        imports.add("#import \"" + name + ".h\"");
        for (ObjCProtocol protocol : protocols) imports.addAll(protocol.imports());
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

        appender.append("@interface").append(" ").append(name).append(" ()");

        int index = 0;
        for (ObjCProtocol protocol : protocols) {
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

        appender.enter().append("@end").enter().enter();

        appender.append("@implementation").append(" ").append(name);

        appender.enter();
        for (SelectorFragment selectorFragment : selectorFragments) {
            appender.enter();
            selectorFragment.joinTo(appender, preference);
            appender.enter();
        }

        appender.enter().append("@end");
    }

    public ClassImplementationFragment addProtocol(String name, String... imports) {
        protocols.add(new ObjCProtocol(name, imports));
        return this;
    }

    public ClassImplementationFragment addPropertyFragment(ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(type, name));
    }

    public ClassImplementationFragment addPropertyFragment(String comment, ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(comment, type, name));
    }

    public ClassImplementationFragment addPropertyFragment(CommentFragment commentFragment, ObjCType type, String name) {
        return addPropertyFragment(new PropertyFragment(commentFragment, type, name));
    }

    public ClassImplementationFragment addPropertyFragment(PropertyFragment propertyFragment) {
        propertyFragments.add(propertyFragment);
        return this;
    }

    public ClassImplementationFragment addSelectorFragment(ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name));
    }

    public ClassImplementationFragment addSelectorFragment(ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name, comment));
    }

    public ClassImplementationFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name));
    }

    public ClassImplementationFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name, comment));
    }

    public ClassImplementationFragment addSelectorFragment(SelectorFragment selectorFragment) {
        selectorFragments.add(selectorFragment);
        return this;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ClassImplementationFragment setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
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
