package io.httpdoc.core.appender;

/**
 * 缩进的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:32
 **/
public class IndentAppender extends LineAppenderWrapper<IndentAppender> implements LineAppender<IndentAppender> {

    public IndentAppender(LineAppender<?> appender, int indent) {
        super(wrap(appender, indent));
    }

    private static LineAppender<?> wrap(LineAppender<?> appender, int indent) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < indent; i++) space.append(" ");
        return new WrappedLineAppender(appender, space.toString(), "");
    }

}
