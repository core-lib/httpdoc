package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.jestful.JestfulClientGenerator;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

/**
 * 产品管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:18
 **/
@HTTP("/products")
@Controller
public class ProductController {

    public static void main(String... args) throws IOException {
        Document document = Document.from(new URL("http://localhost:8080/httpdoc-sample/httpdoc.yaml"), new YamlDeserializer());
        Generation generation = new Generation();
        generation.setDocument(document);
        generation.setPkg("io.httpdoc.gen");
        generation.setDirectory("D:\\gitpot\\httpdoc\\httpdoc-sample\\src\\main\\java\\io\\httpdoc\\gen");
        generation.setProvider(new SystemProvider());
        Generator generator = new JestfulClientGenerator();
        generator.generate(generation);
    }

    /**
     * 分页获取产品列表
     *
     * @param p 页码
     * @param s 页面容量
     * @return 产品列表结果
     */
    @GET("/{page}/{size}")
    public ProductListResult list(@Path("page") int p, @Path("size") int s) {
        return null;
    }

}
