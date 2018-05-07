package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 按行拼接器包装器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 14:53
 **/
public abstract class LineAppenderWrapper<T extends LineAppenderWrapper<T>> extends AppenderWrapper<T> implements LineAppender<T> {

    protected LineAppenderWrapper(LineAppender<?> appender) {
        super(appender);
    }

    @Override
    public T enter() throws IOException {
        validate();
        ((LineAppender<?>) appender).enter();
        return (T) this;
    }

}
