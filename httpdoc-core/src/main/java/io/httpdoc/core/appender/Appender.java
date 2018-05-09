package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:27
 **/
public interface Appender<T extends Appender<T>> extends AutoCloseable {

    /**
     * 拼接
     *
     * @param text 文本
     * @return {@code this}
     * @throws IOException IO异常
     */
    T append(CharSequence text) throws IOException;

    /**
     * 拼接
     *
     * @param text  文本
     * @param start 起始下标
     * @param end   结束下标
     * @return {@code this}
     * @throws IOException IO异常
     */
    T append(CharSequence text, int start, int end) throws IOException;

    /**
     * 拼接
     *
     * @param c 字符
     * @return {@code this}
     * @throws IOException IO异常
     */
    T append(char c) throws IOException;

    /**
     * 关闭
     *
     * @throws IOException IO异常
     */
    void close() throws IOException;

}
