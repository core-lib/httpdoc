package io.httpdoc.core.fragment;

import java.io.IOException;

/**
 * 缩进的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:32
 **/
public class IndentedAppender extends AbstractAppender<IndentedAppender> implements Appender<IndentedAppender> {
    private final Appender<?> appender;
    private final String space;
    private volatile String buffer = null;
    private volatile boolean first = true;
    private volatile boolean enter = false;

    public IndentedAppender(int indent, Appender<?> appender) {
        this.appender = appender;
        String space = "";
        for (int i = 0; i < indent; i++) space = space.concat(" ");
        this.space = space;
    }

    @Override
    public IndentedAppender append(CharSequence text) throws IOException {
        buffer = buffer == null ? text.toString() : buffer + text;
        int start = 0;
        int end = buffer.indexOf("\n", start);
        while (end >= 0) {
            if (first || enter) appender.append(space);
            String line = buffer.substring(start, end).trim();
            appender.append(line).enter();
            start = end + 1;
            end = buffer.indexOf("\n", start);
            first = false;
            enter = true;
        }
        buffer = buffer.substring(start);
        return this;
    }

    @Override
    public void flush() throws IOException {
        if (buffer == null) return;
        super.flush();
        if (first || enter) appender.append(space);
        appender.append(buffer);
        buffer = "";
        first = false;
        enter = false;
        appender.flush();
    }
}
