package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.CommentAppender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 注释碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:41
 **/
public class CommentFragment implements Fragment {
    private String content;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (content == null) return;
        new CommentAppender(appender).append(content).close();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
