package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.retrofit.RetrofitCallGenerator;
import io.httpdoc.retrofit.RetrofitMergedGenerator;
import io.httpdoc.retrofit.RetrofitObservableGenerator;
import org.qfox.jestful.core.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
//        Document document = Document.from(new URL("http://localhost:8080/httpdoc-sample/httpdoc.yaml"), new YamlDeserializer());
//        Generation generation = new Generation();
//        generation.setDocument(document);
//        generation.setPkg("io.httpdoc.gen");
//        generation.setDirectory("D:\\gitpot\\httpdoc\\httpdoc-sample\\src\\main\\java\\io\\httpdoc\\gen");
//        generation.setProvider(new SystemProvider());
//        Generator generator = new RetrofitMergedGenerator()
//                .include(RetrofitCallGenerator.class)
//                .include(RetrofitObservableGenerator.class);
//
//        generator.generate(generation);

        io.httpdoc.gen.ProductController.INSTANCE.a(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"));
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
    public String a(@Body("file") Part file) {
        return null;
    }

    @POST("/b")
    public String b(@Body("files") Part[] files) {
        return null;
    }

    @POST("/c")
    public String c(@Body("files") List<Part> files) {
        return null;
    }

    @POST("/d")
    public String d(@Body("files") Collection<Part> files) {
        return null;
    }

    @POST("/e")
    public String e(@Body("files") Set<Part> files) {
        return null;
    }

    @POST("/f")
    public String f(@Body("name") String[] names, MultipartRequest request) {
        return null;
    }

}
