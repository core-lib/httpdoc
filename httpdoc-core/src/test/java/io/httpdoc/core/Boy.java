package io.httpdoc.core;

import io.httpdoc.core.decode.Decoder;
import io.httpdoc.core.decode.DefaultDecoder;
import io.httpdoc.core.encode.DefaultEncoder;
import io.httpdoc.core.encode.Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
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
        document.setHttpdoc("null");

        document.setSchemas(schemas);

        Controller controller = new Controller();
        controller.setName("ProductController");
        controller.setPath("/products");
        controller.setConsumes(Arrays.asList("application/json", "application/xml"));
        controller.setProduces(Collections.singletonList("application/json"));
        controller.setDescription("产品管理器");

        for (int i = 0 ; i < 3 ; i ++) {
            Operation operation = new Operation();
            operation.setName("create");
            operation.setPath("/");
            operation.setMethod("POST");
            operation.setConsumes(Arrays.asList("application/json", "application/xml"));
            operation.setProduces(Collections.singletonList("application/json"));
            operation.setDescription("创建产品");

            {
                Parameter parameter = new Parameter();
                parameter.setName("product");
                parameter.setScope("body");
                parameter.setType(Schema.valueOf(Product.class));
                parameter.setDescription("产品DTO");
                operation.getParameters().add(parameter);
            }
            {
                Parameter parameter = new Parameter();
                parameter.setName("product");
                parameter.setScope("body");
                parameter.setType(Schema.valueOf(Product.class));
                parameter.setDescription("产品DTO");
                operation.getParameters().add(parameter);
            }

            Result result = new Result();
            result.setType(Schema.valueOf(Long.class));
            operation.setResult(result);

            controller.getOperations().add(operation);
        }
        document.getControllers().add(controller);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Encoder encoder = new DefaultEncoder();
        encoder.encode(document, baos);

        System.out.println(baos.toString());

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Decoder decoder = new DefaultDecoder();
        Document doc = decoder.decode(bais);
        System.out.println(doc);
    }

    public Girl getGirlfriend() {
        return girlfriend;
    }

    public void setGirlfriend(Girl girlfriend) {
        this.girlfriend = girlfriend;
    }
}
