package io.httpdoc.core.decode;

import io.httpdoc.core.Document;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.deserialization.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * 组合的文档解码器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:27
 **/
public class CompositeDecoder implements Decoder {
    private final Converter converter;
    private final Deserializer deserializer;

    public CompositeDecoder(Converter converter, Deserializer deserializer) {
        this.converter = converter;
        this.deserializer = deserializer;
    }

    @Override
    public Document decode(InputStream in) throws IOException {
        Map<String, Object> doc = deserializer.deserialize(in);
        return converter.convert(doc);
    }

    @Override
    public Document decode(Reader reader) throws IOException {
        Map<String, Object> doc = deserializer.deserialize(reader);
        return converter.convert(doc);
    }
}
