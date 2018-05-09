package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 包裹的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 15:05
 **/
public class WrappedLineAppender extends FilterLineAppender<WrappedLineAppender> implements LineAppender<WrappedLineAppender> {
    private final String prefix;
    private final String suffix;

    public WrappedLineAppender(LineAppender<?> appender, String prefix, String suffix) {
        super(appender);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    protected void append(String line) throws IOException {
        appender.append(prefix);
        appender.append(line);
        appender.append(suffix);
        appender.enter();
    }

}
