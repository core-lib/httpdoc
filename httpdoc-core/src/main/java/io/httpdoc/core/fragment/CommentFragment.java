package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.CommentAppender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 注释碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:41
 **/
public class CommentFragment implements Fragment {
    private String content;
    private Set<String> imports = new LinkedHashSet<>();

    public CommentFragment() {
    }

    public CommentFragment(String content) {
        this.content = content;
    }

    public CommentFragment(String content, Set<String> imports) {
        this.content = content;
        this.imports = imports;
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
