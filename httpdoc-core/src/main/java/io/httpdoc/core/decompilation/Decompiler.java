package io.httpdoc.core.decompilation;

import io.httpdoc.core.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * 文档反编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:35
 **/
public interface Decompiler {

    Document compile(InputStream in) throws IOException;

    Document compile(Reader reader) throws IOException;

}
