package io.httpdoc.core.fragment;

import java.io.IOException;

/**
 * 抽象的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:46
 **/
public abstract class AbstractAppender<T extends AbstractAppender<T>> implements Appender<T> {

    @Override
    public T enter() throws IOException {
        return append("\n");
    }

    @Override
    public T append(CharSequence text) throws IOException {
        return append(text, 0, text.length());
    }

    @Override
    public T append(CharSequence text, int start, int end) throws IOException {
        for (int i = start; i < end; i++) append(text.charAt(i));
        return (T) this;
    }

    @Override
    public void flush() throws IOException {

    }

}
