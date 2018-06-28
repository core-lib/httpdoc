package io.httpdoc.sample;

import org.qfox.jestful.core.http.*;
import org.qfox.jestful.server.annotation.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 产品管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:18
 **/
@HTTP("/products")
@Controller
public class ProductController {

    @Autowired
    private ProductAPI productAPI;

    /**
     * 分页获取产品列表
     *
     * @param p 页码
     * @param s 页面容量
     * @return 产品列表结果
     */
    @Transactional
    @GET("/{page}/{size}") // name=sdfsd&age=12
    public ProductListResult list(
            @Path("page") int p,
            @Path("size") int s,
            @Field ProductStatus status,
            @Field List<String> arr,
            @Field Product[] products,
            @Body("file1") Part file1,
            @Body("file2") Part file2,
            HttpServletRequest request
    ) {
        ProductListResult result = new ProductListResult();
        for (int i = (p - 1) * s; i < p * s; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setName("name-" + i);
            product.setPrice(new BigDecimal(i));
            product.setStatus(status);
            result.getProducts().add(product);
        }
        return result;
//        throw new RuntimeException();
    }

    @POST("/")
    public ProductCreateResult create(@Body Product product) {
        ProductCreateResult result = new ProductCreateResult();
        product.setId(new Random().nextLong());
        result.setProduct(product);
        return result;
    }

    @PUT("/{id:\\d+}")
    public ProductUpdateResult update(@Path("id") Long id, @Body("map") String map, @Body("product") Product product, @Body("picture") Part[] picture, MultipartRequest request) {
        ProductUpdateResult result = new ProductUpdateResult();
        product.setId(id);
        result.setProduct(product);
        return result;
    }

}
