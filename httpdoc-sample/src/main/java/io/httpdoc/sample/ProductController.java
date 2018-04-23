package io.httpdoc.sample;

import org.qfox.jestful.core.http.Body;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.POST;
import org.qfox.jestful.core.http.PUT;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:18
 **/
@HTTP("/products")
@Controller
public class ProductController {

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
