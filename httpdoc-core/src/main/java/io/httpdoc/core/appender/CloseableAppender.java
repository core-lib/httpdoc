package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 可关闭的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 13:40
 **/
public abstract class CloseableAppender<T extends CloseableAppender<T>> implements Appender<T> {
    protected volatile boolean closed;

    protected void validate() throws IOException {
        if (closed) throw new IOException("appender closed");
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
