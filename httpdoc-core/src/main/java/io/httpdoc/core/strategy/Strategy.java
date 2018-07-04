package io.httpdoc.core.strategy;

import java.io.IOException;

/**
 * 生成策略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 11:25
 **/
public interface Strategy {

    /**
     * 生成
     *
     * @param directory 文件夹
     * @param claxx     待生成类
     * @throws IOException IO 异常
     */
    void reply(String directory, Claxx claxx) throws IOException;

}
