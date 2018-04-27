package io.httpdoc.core.generating;

import java.io.IOException;

/**
 * 缩进的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:32
 **/
public class IndentedAppender extends AbstractAppender<IndentedAppender> implements Appender<IndentedAppender> {
    private final int indent;
    private final Appender<?> appender;
    private StringBuilder buffer;

    public IndentedAppender(int indent, Appender<?> appender) {
        this.indent = indent;
        this.appender = appender;
        this.buffer = new StringBuilder();
    }


    @Override
    public IndentedAppender append(CharSequence text) throws IOException {
        String string = text.toString();
        String space = "";
        for (int i = 0; i < indent; i++) space = space.concat(" ");
        buffer.append(text);
        int index = buffer.indexOf("\n");
        if (index < 0) return this;



        return this;
    }

    @Override
    public void flush() throws IOException {

    }

}
