package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.type.ObjCClass;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 协议代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:39
 **/
public class ObjCProtocolFragment implements Fragment {
    private ObjCCommentFragment commentFragment;
    private String name;
    private Set<ObjCClass> protocols = new LinkedHashSet<>();
    private Set<ObjCSelectorFragment> selectorFragments = new LinkedHashSet<>();

    public ObjCProtocolFragment() {
    }

    public ObjCProtocolFragment(String name) {
        this.name = name;
    }

    public ObjCProtocolFragment(String comment, String name) {
        this.commentFragment = comment != null ? new ObjCCommentFragment(comment) : null;
        this.name = name;
    }

    public ObjCProtocolFragment(ObjCCommentFragment commentFragment, String name) {
        this.commentFragment = commentFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        for (ObjCClass protocol : protocols) imports.addAll(protocol.imports());
        for (ObjCSelectorFragment selectorFragment : selectorFragments) imports.addAll(selectorFragment.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        appender.enter();
        for (String include : imports()) appender.append(include).enter();
        appender.enter();

        appender.append("@protocol").append(" ").append(name);

        int index = 0;
        for (ObjCClass protocol : protocols) {
            if (index++ == 0) appender.append("<");
            else appender.append(", ");
            appender.append(protocol.getName());
            if (index == protocols.size()) appender.append(">");
        }

        appender.enter();
        for (ObjCSelectorFragment selectorFragment : selectorFragments) {
            appender.enter();
            selectorFragment.joinTo(appender, preference);
            appender.enter();
        }

        appender.enter().append("@end");
    }

    public ObjCProtocolFragment addProtocol(ObjCClass protocol) {
        protocols.add(protocol);
        return this;
    }

    public ObjCProtocolFragment addSelectorFragment(ObjCResultFragment resultFragment, String name) {
        return addSelectorFragment(new ObjCSelectorFragment(resultFragment, name));
    }

    public ObjCProtocolFragment addSelectorFragment(ObjCResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new ObjCSelectorFragment(resultFragment, name, comment));
    }

    public ObjCProtocolFragment addSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name) {
        return addSelectorFragment(new ObjCSelectorFragment(instantial, resultFragment, name));
    }

    public ObjCProtocolFragment addSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new ObjCSelectorFragment(instantial, resultFragment, name, comment));
    }

    public ObjCProtocolFragment addSelectorFragment(ObjCSelectorFragment selectorFragment) {
        selectorFragments.add(selectorFragment);
        return this;
    }

    public ObjCCommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ObjCProtocolFragment setCommentFragment(ObjCCommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCProtocolFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ObjCClass> getProtocols() {
        return protocols;
    }

    public ObjCProtocolFragment setProtocols(Set<ObjCClass> protocols) {
        this.protocols = protocols;
        return this;
    }

    public Set<ObjCSelectorFragment> getSelectorFragments() {
        return selectorFragments;
    }

    public ObjCProtocolFragment setSelectorFragments(Set<ObjCSelectorFragment> selectorFragments) {
        this.selectorFragments = selectorFragments;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCProtocolFragment that = (ObjCProtocolFragment) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
