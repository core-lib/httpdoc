package io.httpdoc.sample;

import org.qfox.jestful.core.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.Part;
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

    /**
     * 分页获取产品列表
     *
     * @param p 页码
     * @param s 页面容量
     * @return 产品列表结果
     */
    @GET("/{page}/{size}")
    public ProductListResult list(@Path("page") int p, @Path("size") int s) {
        return new ProductListResult();
    }

    @POST("/")
    public void create(@Body Product<Sample> product) {

    }

    @POST("/a")
    public String a(@Body("name") String name, @Body("sample") Sample sample, @Body("file") Part file) {
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
