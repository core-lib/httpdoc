package io.httpdoc.sample;

import io.httpdoc.core.annotation.Name;
import io.httpdoc.core.annotation.Package;
import io.httpdoc.core.annotation.Tag;
import org.qfox.jestful.core.http.*;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 产品管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:18
 **/
@HTTP("/products")
@Controller
@Package("io.httpdoc.test")
@Name("ProductAPI")
@Tag(value = "product")
public class ProductController {

    /**
     * 分页获取产品列表
     *
     * @param p 页码
     * @param s 页面容量
     * @return 产品列表结果
     */
    @GET("/{page}/{size}")
    public ProductListResult list(
            @Path("page") int p,
            @Path("size") int s,
            @Matrix(value = "status", path = "size") ProductStatus status,
            @Header("sid") String sid
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
    }

    @DELETE(value = "/{id}", produces = {"application/json", "application/xml"})
    public void delete(
            @Path("id") String id,
            @Cookie("sid") String sid

    ) {

    }

    /**
     * 创建产品
     *
     * @return 产品创建结果
     */
    @POST(value = "/", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ProductCreateResult create(@Body("product") Product product) {
        ProductCreateResult result = new ProductCreateResult();
        product.setId(new Random().nextLong());
        result.setProduct(product);
        return result;
    }

    @PUT(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ProductUpdateResult update(
            @Path("id") Long id,
            @Matrix(value = "name", path = "id") String name,
            @Body("product") Product product
    ) {
        ProductUpdateResult result = new ProductUpdateResult();
        product.setId(id);
        result.setProduct(product);
        return result;
    }

}
