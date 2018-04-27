package io.httpdoc.core.generating;

import java.io.IOException;

/**
 * 生成
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:26
 **/
public interface Generating {

    <T extends Appender<T>> void generate(T appender) throws IOException;

}
