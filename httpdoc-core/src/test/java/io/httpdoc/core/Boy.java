package io.httpdoc.core;

import io.httpdoc.core.compilation.DefaultEncoder;
import io.httpdoc.core.compilation.Encoder;

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

        Encoder encoder = new DefaultEncoder();
        encoder.encode(document, System.out);

    }

    public Girl getGirlfriend() {
        return girlfriend;
    }

    public void setGirlfriend(Girl girlfriend) {
        this.girlfriend = girlfriend;
    }
}
