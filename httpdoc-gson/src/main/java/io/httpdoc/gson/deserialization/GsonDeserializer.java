package io.httpdoc.gson.deserialization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.httpdoc.core.deserialization.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Gson 文档反序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:18
 **/
public class GsonDeserializer implements Deserializer {

    @Override
    public Map<String, Object> deserialize(InputStream in) throws IOException {
        Reader reader = null;
        try {
            reader = new InputStreamReader(in);
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
            }.getType());
        } finally {
            if (reader != null) reader.close();
        }
    }

    @Override
    public Map<String, Object> deserialize(Reader reader) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

}
