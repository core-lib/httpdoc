package io.httpdoc.core.fragment;

import java.io.IOException;

/**
 * 生成
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:26
 **/
public interface Fragment {

    <T extends Appender<T>> void joinTo(T appender) throws IOException;

}
