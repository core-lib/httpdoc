package io.httpdoc.core;

import io.httpdoc.core.decode.Decoder;
import io.httpdoc.core.decode.DefaultDecoder;
import io.httpdoc.core.encode.DefaultEncoder;
import io.httpdoc.core.encode.Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 16:26
 **/
public class Boy extends Person {
    private Girl girlfriend;

    public static void main(String... args) throws Exception {


        Schema schema = Schema.valueOf(Boy.class);

        Map<String, Schema> schemas = new HashMap<>();
        for (Schema s : schema.getDependencies()) schemas.put(s.getName(), s);
        Document document = new Document();
        document.setSchemas(schemas);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Encoder encoder = new DefaultEncoder();
        encoder.encode(document, baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Decoder decoder = new DefaultDecoder();
        Document doc = decoder.decode(bais);

    }

    public Girl getGirlfriend() {
        return girlfriend;
    }

    public void setGirlfriend(Girl girlfriend) {
        this.girlfriend = girlfriend;
    }
}
