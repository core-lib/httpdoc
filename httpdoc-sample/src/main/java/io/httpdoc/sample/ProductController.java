package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.deserialization.Deserializer;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.jestful.JestfulClientGenerator;
import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.POST;
import org.qfox.jestful.core.http.PUT;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:18
 **/
@HTTP("/products")
@Controller
public class ProductController {

    public static void main(String... args) throws IOException {
        Deserializer deserializer = new YamlDeserializer();
        Converter converter = new StandardConverter();
        Map<String, Object> doc = deserializer.deserialize(new FileInputStream("D:\\用户目录\\下载\\httpdoc (11).yaml"));
        Document document = converter.convert(doc);
        Generation generation = new Generation();
        generation.setDocument(document);
        generation.setPkg("io.httpdoc.gen");
        generation.setDirectory("D:\\用户目录\\下载");
        generation.setProvider(new SystemProvider());
        Generator generator = new JestfulClientGenerator();
        generator.generate(generation);
    }

    /**
     * 创建产品
     *
     * @param name  产品名称
     * @param price 产品价格
     * @return 创建后的产品对象
     */
    @POST(value = "/", produces = "application/json", consumes = "applicaiton/json")
    public Product create(@Body("name") String name, @Body("price") BigDecimal price) {
        return null;
    }

    /**
     * @param t
     * @param <T>
     * @return
     */
    @PUT(value = "/", produces = "application/json", consumes = {"application/json", "application/xml"})
    public <T> String update(T t) {
        return null;
    }

}
