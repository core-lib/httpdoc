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
    public T append(CharSequence text, int start, int end) throws IOException {
        return append(text.subSequence(start, end));
    }

    @Override
    public T append(char c) throws IOException {
        return append(String.valueOf(c), 0, 1);
    }

    @Override
    public void flush() throws IOException {

    }

}
