package io.httpdoc.core.modeler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * 方案
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:00
 **/
public interface Model {

    /**
     * 建造
     *
     * @param out 输出流
     * @throws IOException IO异常
     */
    void buildTo(OutputStream out) throws IOException;

    /**
     * 建造
     *
     * @param writer 输出器
     * @throws IOException IO异常
     */
    void buildTo(Writer writer) throws IOException;

}
