package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 可回车的
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 14:19
 **/
public interface Enterable<T extends Enterable<T>> {

    /**
     * 回车
     *
     * @return {@code this}
     * @throws IOException IO异常
     */
    T enter() throws IOException;

}
