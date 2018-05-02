package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 缩进的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:32
 **/
public class IndentedAppender extends LineAppenderWrapper<IndentedAppender> implements LineAppender<IndentedAppender> {

    public IndentedAppender(LineAppender<?> appender, int indent) {
        super(wrap(appender, indent));
    }

    private static LineAppender<?> wrap(LineAppender<?> appender, int indent) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < indent; i++) space.append(" ");
        return new WrappedLineAppender(appender, space.toString(), "");
    }

    public static void main(String... args) throws IOException {
        Appender<?> appender = new IndentedAppender(new ConsoleAppender(), 4);
        appender.append("1\r\n2\n3\n4").close();
    }

}
