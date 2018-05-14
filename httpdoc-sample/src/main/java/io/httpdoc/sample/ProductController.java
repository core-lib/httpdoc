package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.jestful.*;
import org.qfox.jestful.core.http.*;
import org.qfox.jestful.server.formatting.Multipart;
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
        Generator generator = new JestfulClientMergedGenerator()
                .include(JestfulClientFutureGenerator.class)
                .include(JestfulClientMessageGenerator.class)
                .include(JestfulClientObservableGenerator.class)
                .include(JestfulClientLambdaGenerator.class);

        generator.generate(generation);

//        io.httpdoc.gen.ProductController.INSTANCE.list(1, 20);
//        Translator translator = new JestfulServerTranslator();
//        String path = translator.normalize("/products/{1:\\d+}/size-{2:\\d+}");
//        System.out.println(path);
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

    @POST("/pictures")
    public String upload(@Body("file") Multipart file) {
        return null;
    }

}
