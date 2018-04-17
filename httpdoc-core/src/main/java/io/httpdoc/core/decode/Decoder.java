package io.httpdoc.core.decode;

import io.httpdoc.core.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * 文档解码器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:25
 **/
public interface Decoder {

    Document decode(InputStream in) throws IOException;

    Document decode(Reader reader) throws IOException;

}
