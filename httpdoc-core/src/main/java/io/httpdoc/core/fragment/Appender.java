package io.httpdoc.core.fragment;

import java.io.IOException;

/**
 * 拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 17:27
 **/
public interface Appender<T extends Appender<T>> {

    /**
     * 回车
     *
     * @return {@code this}
     * @throws IOException IO异常
     */
    T enter() throws IOException;

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
     * 冲刷
     *
     * @throws IOException IO异常
     */
    void flush() throws IOException;

}
