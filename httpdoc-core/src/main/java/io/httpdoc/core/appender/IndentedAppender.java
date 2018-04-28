package io.httpdoc.core.appender;

/**
 * 缩进的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:32
 **/
public class IndentedAppender extends FilterAppender<IndentedAppender> implements Appender<IndentedAppender> {

    public IndentedAppender(int indent, Appender<?> appender) {
        super(new PrefixAppender(space(indent), appender));
    }

    private static String space(int indent) {
        String space = "";
        for (int i = 0; i < indent; i++) space = space.concat(" ");
        return space;
    }

}
