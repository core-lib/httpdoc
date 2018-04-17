package io.httpdoc.core.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * YAML反序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:29
 **/
public class YamlDeserializer implements Deserializer {
    private final YAMLMapper mapper;

    public YamlDeserializer() {
        this(new YAMLMapper());
    }

    public YamlDeserializer(YAMLFactory factory) {
        this(new YAMLMapper(factory));
    }

    public YamlDeserializer(YAMLMapper mapper) {
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
