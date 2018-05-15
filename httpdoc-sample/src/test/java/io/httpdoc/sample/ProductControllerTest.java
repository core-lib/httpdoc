package io.httpdoc.sample;

import io.httpdoc.gen.ProductController;
import org.junit.Test;

import java.io.File;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    @Test
    public void testA() {
        ProductController.INSTANCE.a(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"));
    }

    @Test
    public void testB() {
        ProductController.INSTANCE.b(new File[]{
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"),
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")
        });
    }

}
