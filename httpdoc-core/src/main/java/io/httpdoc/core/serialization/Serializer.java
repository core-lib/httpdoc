package io.httpdoc.core.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * 文档序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 9:52
 **/
public interface Serializer {

    void serialize(Map<String, Object> doc, OutputStream out) throws IOException;

    void serialize(Map<String, Object> doc, Writer writer) throws IOException;

}
