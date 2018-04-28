package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 后缀拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 15:05
 **/
public class SuffixAppender extends FilterAppender<SuffixAppender> implements Appender<SuffixAppender> {
    private final String suffix;
    private volatile StringBuilder buffer = null;
    private volatile boolean first = true;
    private volatile boolean enter = false;

    public SuffixAppender(String suffix, Appender<?> appender) {
        super(appender);
        this.suffix = suffix;
    }

    @Override
    public SuffixAppender append(char c) throws IOException {
        if (buffer == null) buffer = new StringBuilder();
        switch (c) {
            case '\r':
                return this;
            case '\n':
                appender.append(buffer);
                if (first || enter) appender.append(suffix);
                appender.enter();
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
        appender.append(buffer);
        if (first || enter) appender.append(suffix);
        buffer.setLength(0);
        first = false;
        enter = false;
        appender.flush();
    }

}
