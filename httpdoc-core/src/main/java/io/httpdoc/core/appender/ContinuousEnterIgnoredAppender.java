package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 连续换行忽略拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-07 15:21
 **/
public class ContinuousEnterIgnoredAppender extends FilterLineAppender<ContinuousEnterIgnoredAppender> {
    private final int continuous;
    private int enters;

    public ContinuousEnterIgnoredAppender(LineAppender<?> appender) {
        this(appender, 1);
    }

    public ContinuousEnterIgnoredAppender(LineAppender<?> appender, int continuous) {
        super(appender);
        this.continuous = continuous;
    }

    @Override
    public ContinuousEnterIgnoredAppender append(char c) throws IOException {
        if (c == '\n') {
            if (++enters > continuous) return this;
            else return super.append(c);
        } else {
            enters = 0;
            return super.append(c);
        }
    }
}
