package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 抽象的按行拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 13:38
 **/
public abstract class AbstractLineAppender<T extends AbstractLineAppender<T>> extends EnterableAppender<T> implements LineAppender<T> {
    private volatile StringBuilder buffer = null;

    /**
     * 拼接一行
     *
     * @param line 一行
     * @throws IOException IO异常
     */
    protected abstract void append(String line) throws IOException;

    @Override
    public T append(char c) throws IOException {
        validate();
        if (buffer == null) buffer = new StringBuilder();
        switch (c) {
            case '\r':
                break;
            case '\n':
                String line = buffer.toString();
                append(line);
                buffer.setLength(0);
                break;
            default:
                buffer.append(c);
                break;
        }
        return (T) this;
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        super.close();
        if (buffer == null || buffer.length() == 0) return;
        String line = buffer.toString();
        append(line);
        buffer.setLength(0);
    }

}
