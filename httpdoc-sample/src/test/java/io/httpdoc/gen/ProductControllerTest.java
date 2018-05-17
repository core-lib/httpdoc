package io.httpdoc.gen;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Test;
import retrofit2.Response;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    @Test
    public void testList() throws Exception {
        ProductListResult result = ProductController.INSTANCE.listForCall(1, 20, ProductStatus.A).execute().body();
        System.out.println(result);
    }

    @Test
    public void testCreate() throws Exception {
        Product product = new Product();
        product.setName("iPhone X");
        product.setPrice(new BigDecimal(8888));
        product.setStatus(ProductStatus.B);
        ProductCreateResult result = ProductController.INSTANCE.createForCall(product).execute().body();
        System.out.println(result);
    }

    @Test
    public void testUpdate() throws Exception {
        Product product = new Product();
        product.setName("iPhone X");
        product.setPrice(new BigDecimal(8888));
        product.setStatus(ProductStatus.B);
        RequestBody picture = RequestBody.create(MediaType.parse("application/jpeg"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"));
        Response<ProductUpdateResult> response = ProductController.INSTANCE.updateForCall(1L, "Name", product, new RequestBody[]{picture, picture}, null).execute();
        ProductUpdateResult result = response.body();
        System.out.println(result);
    }

}
