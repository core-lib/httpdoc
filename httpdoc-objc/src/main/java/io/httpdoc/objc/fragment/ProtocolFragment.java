package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjCProtocol;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 协议代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:39
 **/
public class ProtocolFragment implements Fragment {
    private CommentFragment commentFragment;
    private String name;
    private Set<ObjCProtocol> protocols = new LinkedHashSet<>();
    private Set<SelectorFragment> selectorFragments = new LinkedHashSet<>();

    public ProtocolFragment() {
    }

    public ProtocolFragment(String name) {
        this.name = name;
    }

    public ProtocolFragment(String comment, String name) {
        this.commentFragment = comment != null ? new CommentFragment(comment) : null;
        this.name = name;
    }

    public ProtocolFragment(CommentFragment commentFragment, String name) {
        this.commentFragment = commentFragment;
        this.name = name;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        for (ObjCProtocol protocol : protocols) imports.addAll(protocol.imports());
        for (SelectorFragment selectorFragment : selectorFragments) imports.addAll(selectorFragment.imports());
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
        for (ObjCProtocol protocol : protocols) {
            if (index++ == 0) appender.append("<");
            else appender.append(", ");
            appender.append(protocol.getName());
            if (index == protocols.size()) appender.append(">");
        }

        appender.enter();
        for (SelectorFragment selectorFragment : selectorFragments) {
            appender.enter();
            selectorFragment.joinTo(appender, preference);
            appender.enter();
        }

        appender.enter().append("@end");
    }

    public ProtocolFragment addProtocol(String name, String... imports) {
        protocols.add(new ObjCProtocol(name, imports));
        return this;
    }

    public ProtocolFragment addSelectorFragment(ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name));
    }

    public ProtocolFragment addSelectorFragment(ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(resultFragment, name, comment));
    }

    public ProtocolFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name));
    }

    public ProtocolFragment addSelectorFragment(boolean instantial, ResultFragment resultFragment, String name, String comment) {
        return addSelectorFragment(new SelectorFragment(instantial, resultFragment, name, comment));
    }

    public ProtocolFragment addSelectorFragment(SelectorFragment selectorFragment) {
        selectorFragments.add(selectorFragment);
        return this;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public ProtocolFragment setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProtocolFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ObjCProtocol> getProtocols() {
        return protocols;
    }

    public ProtocolFragment setProtocols(Set<ObjCProtocol> protocols) {
        this.protocols = protocols;
        return this;
    }

    public Set<SelectorFragment> getSelectorFragments() {
        return selectorFragments;
    }

    public ProtocolFragment setSelectorFragments(Set<SelectorFragment> selectorFragments) {
        this.selectorFragments = selectorFragments;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtocolFragment that = (ProtocolFragment) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
