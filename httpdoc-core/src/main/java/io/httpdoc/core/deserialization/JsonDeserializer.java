package io.httpdoc.core.deserialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * Object反序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:29
 **/
public class JsonDeserializer implements Deserializer {
    private final ObjectMapper mapper;

    public JsonDeserializer() {
        this(new JsonFactory());
    }

    public JsonDeserializer(JsonFactory factory) {
        this(new ObjectMapper(factory));
    }

    public JsonDeserializer(ObjectMapper mapper) {
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
