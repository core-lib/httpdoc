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
    private volatile StringBuilder buffer = null;
    private volatile boolean first = true;
    private volatile boolean enter = false;

    public IndentedAppender(int indent, Appender<?> appender) {
        this.appender = appender;
        String space = "";
        for (int i = 0; i < indent; i++) space = space.concat(" ");
        this.space = space;
    }

    @Override
    public IndentedAppender append(char c) throws IOException {
        if (buffer == null) buffer = new StringBuilder();
        switch (c) {
            case '\r':
                return this;
            case '\n':
                if (first || enter) appender.append(space);
                appender.append(buffer).enter();
                buffer.setLength(0);
                first = false;
                enter = true;
                return this;
            default:
                buffer.append(c);
                return this;
        }
    }

    @Override
    public void flush() throws IOException {
        if (buffer == null) return;
        super.flush();
        if (first || enter) appender.append(space);
        appender.append(buffer);
        buffer.setLength(0);
        first = false;
        enter = false;
        appender.flush();
    }
}
