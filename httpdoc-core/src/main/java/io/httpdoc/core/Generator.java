package io.httpdoc.core;

import java.io.File;

/**
 * 代码生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 15:48
 **/
public interface Generator {

    void generation(Generation generation, File directory);

}
