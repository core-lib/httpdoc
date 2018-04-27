package io.httpdoc.core.generating;

import java.io.IOException;

/**
 * 拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:27
 **/
public interface Appender<T extends Appender<T>> {

    T appendln() throws IOException;

    T appendln(CharSequence text) throws IOException;

    T append(CharSequence text) throws IOException;

    void flush() throws IOException;

}
