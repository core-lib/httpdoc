package io.httpdoc.core;

import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 16:26
 **/
public class Boy extends Person {
    private Girl girlfriend;

    public static void main(String... args) throws Exception {
        YAMLMapper mapper = new YAMLMapper();
//        Schema schema = Schema.valueOf(Boy.class);
//
//        mapper.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
//
//        Map<String, Schema> schemas = new HashMap<>();
//        for (Schema s : schema.getDependencies()) schemas.put(s.getName(), s);
//        Document document = new Document();
//        document.setSchemas(schemas);
//
//        StringWriter sw = new StringWriter();
//        mapper.writeValue(sw, document);
//        String httpdoc = sw.toString();
//        System.out.println(httpdoc);
        Document doc = mapper.readValue(new URL("file:\\D:\\gitpot\\httpdoc\\httpdoc-core\\src\\test\\resources\\swagger.yaml"), Document.class);
        System.out.println(doc);
    }

    public Girl getGirlfriend() {
        return girlfriend;
    }

    public void setGirlfriend(Girl girlfriend) {
        this.girlfriend = girlfriend;
    }
}
