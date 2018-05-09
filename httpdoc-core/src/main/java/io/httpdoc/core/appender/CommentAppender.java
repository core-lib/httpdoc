package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 注释拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 14:47
 **/
public class CommentAppender extends LineAppenderWrapper<CommentAppender> implements LineAppender<CommentAppender> {
    private final LineAppender<?> appender;

    public CommentAppender(LineAppender<?> appender) throws IOException {
        super(new WrappedLineAppender(appender, " * ", ""));
        this.appender = appender.append("/**").enter();
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        super.close();
        this.appender.append(" */");
    }

}
