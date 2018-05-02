package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 注释拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 14:47
 **/
public class CommentedAppender extends LineAppenderWrapper<CommentedAppender> implements LineAppender<CommentedAppender> {

    public CommentedAppender(LineAppender<?> appender) throws IOException {
        super(new WrappedLineAppender(appender, " *", ""));
        appender.append("/**").enter();
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        this.enter().append("*/");
        super.close();
    }

    public static void main(String... args) throws IOException {
        Appender<?> appender = new CommentedAppender(new ConsoleAppender());
        appender.append("1\r\n2\n3\n4").close();
    }
}
