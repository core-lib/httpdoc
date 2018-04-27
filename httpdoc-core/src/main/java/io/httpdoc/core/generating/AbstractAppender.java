package io.httpdoc.core.generating;

import java.io.IOException;

/**
 * 抽象的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:46
 **/
public abstract class AbstractAppender<T extends AbstractAppender<T>> implements Appender<T> {

    @Override
    public T appendln() throws IOException {
        return append("\n");
    }

    @Override
    public T appendln(CharSequence text) throws IOException {
        return append(text.toString()).appendln();
    }

}
