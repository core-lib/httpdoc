package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.jestful.*;
import org.qfox.jestful.core.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

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
        generation.setDirectory("C:\\Users\\Chang\\IdeaProjects\\httpdoc\\httpdoc-sample\\src\\main\\java\\io\\httpdoc\\gen");
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

    @POST("/a")
    public String a(@Body("file") MultipartFile file) {
        return null;
    }

    @POST("/b")
    public String b(@Body("files") MultipartFile[] files) {
        return null;
    }

    @POST("/c")
    public String c(@Body("files") List<MultipartFile> files) {
        return null;
    }

    @POST("/d")
    public String d(@Body("files") Map<String, MultipartFile> map) {
        return null;
    }

    @POST("/e")
    public String e(@Body("files") Map<String, MultipartFile[]> map) {
        return null;
    }

    @POST("/f")
    public String f(MultipartHttpServletRequest request) {
        return null;
    }

}
