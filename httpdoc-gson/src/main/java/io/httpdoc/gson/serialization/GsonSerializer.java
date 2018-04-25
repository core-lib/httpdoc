package io.httpdoc.gson.serialization;

import com.google.gson.Gson;
import io.httpdoc.core.serialization.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * gson 文档序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:12
 **/
public class GsonSerializer implements Serializer {

    @Override
    public String getName() {
        return "gson";
    }

    @Override
    public String getType() {
        return "application/json";
    }

    @Override
    public void serialize(Map<String, Object> doc, OutputStream out) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(out);
            Gson gson = new Gson();
            gson.toJson(doc, writer);
        } finally {
            if (writer != null) writer.close();
        }
    }

    @Override
    public void serialize(Map<String, Object> doc, Writer writer) throws IOException {
        Gson gson = new Gson();
        gson.toJson(doc, writer);
    }
}
