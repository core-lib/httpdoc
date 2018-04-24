package io.httpdoc.core.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * Xml反序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:29
 **/
public class XmlDeserializer implements Deserializer {
    private final XmlMapper mapper;

    public XmlDeserializer() {
        this(new XmlFactory());
    }

    public XmlDeserializer(XmlFactory factory) {
        this(new XmlMapper(factory));
    }

    public XmlDeserializer(XmlMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Map<String, Object> deserialize(InputStream in) throws IOException {
        return mapper.readValue(in, new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> deserialize(Reader reader) throws IOException {
        return mapper.readValue(reader, new TypeReference<Map<String, Object>>() {
        });
    }
}
