package io.httpdoc.core.encode;

import io.httpdoc.core.Document;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.DefaultFormat;
import io.httpdoc.core.conversion.Format;
import io.httpdoc.core.serialization.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * 组合的编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:03
 **/
public class CompositeEncoder implements Encoder {
    private final Converter converter;
    private final Serializer serializer;

    public CompositeEncoder(Converter converter, Serializer serializer) {
        this.converter = converter;
        this.serializer = serializer;
    }

    @Override
    public void encode(Document document, OutputStream out) throws IOException {
        Map<String, Object> doc = converter.convert(document);
        serializer.serialize(doc, out);
    }

    @Override
    public void encode(Document document, Writer writer) throws IOException {
        Map<String, Object> doc = converter.convert(document);
        serializer.serialize(doc, writer);
    }

    @Override
    public void encode(Document document, Format format, OutputStream out) throws IOException {
        Map<String, Object> doc = converter.convert(document, format);
        serializer.serialize(doc, out);
    }

    @Override
    public void encode(Document document, Format format, Writer writer) throws IOException {
        Map<String, Object> doc = converter.convert(document, format);
        serializer.serialize(doc, writer);
    }

}
