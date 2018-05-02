package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 拼接器包装器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 14:51
 **/
public abstract class AppenderWrapper<T extends AppenderWrapper<T>> extends CloseableAppender<T> implements Appender<T> {
    protected final Appender<?> appender;

    protected AppenderWrapper(Appender<?> appender) {
        this.appender = appender;
    }

    @Override
    public T append(CharSequence text) throws IOException {
        appender.append(text);
        return (T) this;
    }

    @Override
    public T append(CharSequence text, int start, int end) throws IOException {
        appender.append(text, start, end);
        return (T) this;
    }

    @Override
    public T append(char c) throws IOException {
        validate();
        appender.append(c);
        return (T) this;
    }

    @Override
    public void close() throws IOException {
        appender.close();
    }

}
