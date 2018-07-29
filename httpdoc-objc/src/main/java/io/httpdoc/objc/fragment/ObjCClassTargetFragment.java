package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.type.ObjCClass;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 实现代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:30
 **/
public class ObjCClassTargetFragment implements Fragment {
    private ObjCCommentFragment commentFragment;
    private String name;
    private Set<ObjCClass> protocols = new LinkedHashSet<>();
    private Set<ObjCPropertyFragment> propertyFragments = new LinkedHashSet<>();
    private Set<ObjCSelectorFragment> selectorFragments = new LinkedHashSet<>();

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        imports.add("#import \"" + name + ".h\"");
        for (ObjCClass protocol : protocols) imports.add(protocol.getLocation());
        for (ObjCPropertyFragment propertyFragment : propertyFragments) imports.addAll(propertyFragment.imports());
        for (ObjCSelectorFragment selectorFragment : selectorFragments) imports.addAll(selectorFragment.imports());
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
        for (ObjCClass protocol : protocols) {
            if (index++ == 0) appender.append("<");
            else appender.append(", ");
            appender.append(protocol.getName());
            if (index == protocols.size()) appender.append(">");
        }

        appender.enter();
        for (ObjCPropertyFragment propertyFragment : propertyFragments) {
            appender.enter();
            propertyFragment.joinTo(appender, preference);
        }
        appender.enter();

        appender.enter().append("@end").enter().enter();

        appender.append("@implementation").append(" ").append(name);

        appender.enter();
        for (ObjCSelectorFragment selectorFragment : selectorFragments) {
            appender.enter();
            selectorFragment.joinTo(appender, preference);
            appender.enter();
        }

        appender.enter().append("@end");
    }

    public ObjCClassTargetFragment addProtocol(ObjCClass protocol) {
        protocols.add(protocol);
        return this;
    }

    public ObjCClassTargetFragment addPropertyFragment(ObjCType type, String name) {
        return addPropertyFragment(new ObjCPropertyFragment(type, name));
    }

    public ObjCClassTargetFragment addPropertyFragment(String comment, ObjCType type, String name) {
        return addPropertyFragment(new ObjCPropertyFragment(comment, type, name));
    }

    public ObjCClassTargetFragment addPropertyFragment(ObjCCommentFragment commentFragment, ObjCType type, String name) {
        return addPropertyFragment(new ObjCPropertyFragment(commentFragment, type, name));
    }

    public ObjCClassTargetFragment addPropertyFragment(ObjCPropertyFragment propertyFragment) {
        propertyFragments.add(propertyFragment);
        return this;
    }

    public ObjCClassTargetFragment addSelectorFragment(ObjCResultFragment resultFragment, String name) {
        return addSelectorFragment(new ObjCSelectorFragment(resultFragment, name));
    }

    public ObjCClassTargetFragment addSelectorFragment(ObjCResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new ObjCSelectorFragment(resultFragment, name, comment));
    }

    public ObjCClassTargetFragment addSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name) {
        return addSelectorFragment(new ObjCSelectorFragment(instantial, resultFragment, name));
    }

    public ObjCClassTargetFragment addSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new ObjCSelectorFragment(instantial, resultFragment, name, comment));
    }

    public ObjCClassTargetFragment addSelectorFragment(ObjCSelectorFragment selectorFragment) {
        selectorFragments.add(selectorFragment);
        return this;
    }

    public ObjCCommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ObjCClassTargetFragment setCommentFragment(ObjCCommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCClassTargetFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ObjCClass> getProtocols() {
        return protocols;
    }

    public ObjCClassTargetFragment setProtocols(Set<ObjCClass> protocols) {
        this.protocols = protocols;
        return this;
    }

    public Set<ObjCPropertyFragment> getPropertyFragments() {
        return propertyFragments;
    }

    public ObjCClassTargetFragment setPropertyFragments(Set<ObjCPropertyFragment> propertyFragments) {
        this.propertyFragments = propertyFragments;
        return this;
    }

    public Set<ObjCSelectorFragment> getSelectorFragments() {
        return selectorFragments;
    }

    public ObjCClassTargetFragment setSelectorFragments(Set<ObjCSelectorFragment> selectorFragments) {
        this.selectorFragments = selectorFragments;
        return this;
    }

}
