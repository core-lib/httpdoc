package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.CommentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * 注释代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:48
 **/
public class CommentFragment implements Fragment {
    private String content;
    private Set<String> imports = new TreeSet<>();

    public CommentFragment() {
    }

    public CommentFragment(String content) {
        this.content = content;
    }

    public CommentFragment(String content, String... imports) {
        this.content = content;
        this.imports.addAll(Arrays.asList(imports));
    }

    public CommentFragment(String content, Set<String> imports) {
        this.content = content;
        this.imports.addAll(imports);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (content == null || content.trim().isEmpty()) return;
        new CommentAppender(appender).append(content).close();
        appender.enter();
    }

    @Override
    public Set<String> imports() {
        return imports != null ? imports : Collections.<String>emptySet();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
