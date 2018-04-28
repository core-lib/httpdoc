package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 前缀拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 15:05
 **/
public class PrefixAppender extends FilterAppender<PrefixAppender> implements Appender<PrefixAppender> {
    private final String prefix;
    private volatile StringBuilder buffer = null;
    private volatile boolean first = true;
    private volatile boolean enter = false;

    public PrefixAppender(String prefix, Appender<?> appender) {
        super(appender);
        this.prefix = prefix;
    }

    @Override
    public PrefixAppender append(char c) throws IOException {
        if (buffer == null) buffer = new StringBuilder();
        switch (c) {
            case '\r':
                return this;
            case '\n':
                if (first || enter) appender.append(prefix);
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
        if (first || enter) appender.append(prefix);
        appender.append(buffer);
        buffer.setLength(0);
        first = false;
        enter = false;
        appender.flush();
    }

}
