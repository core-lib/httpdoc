package io.httpdoc.sample;

import io.httpdoc.gen.ProductController;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    public static void main(String... args) throws IOException {
        Response<String> execute = ProductController.INSTANCE.aForCall(new File("D:\\用户目录\\下载\\ProductStatus.java")).execute();
        System.out.println(execute);
    }

}
