package io.httpdoc.core.compilation;

import io.httpdoc.core.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * 文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:31
 **/
public interface Encoder {

    void encode(Document document, OutputStream out) throws IOException;

    void encode(Document document, Writer writer) throws IOException;

}
