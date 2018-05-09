package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 连续换行忽略拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-07 15:21
 **/
public class EnterMergedAppender extends FilterLineAppender<EnterMergedAppender> {
    private final int max;
    private int enter;

    public EnterMergedAppender(LineAppender<?> appender) {
        this(appender, 1);
    }

    public EnterMergedAppender(LineAppender<?> appender, int max) {
        super(appender);
        this.max = max;
    }

    @Override
    public EnterMergedAppender append(char c) throws IOException {
        if (c == '\n') {
            if (++enter > max) return this;
            else return super.append(c);
        } else {
            enter = 0;
            return super.append(c);
        }
    }
}
