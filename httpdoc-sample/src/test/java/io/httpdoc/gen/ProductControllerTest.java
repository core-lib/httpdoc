package io.httpdoc.gen;

import org.junit.Test;
import org.qfox.jestful.client.Message;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    @Test
    public void testList() throws Exception {
        Lock lock = new SimpleLock();
        Message message = ProductController.INSTANCE.listForMessage(1, 20, ProductStatus.A);
        System.out.println(message);
    }

//    @Test
//    public void testCreate() throws Exception {
//        Product product = new Product();
//        product.setName("iPhone X");
//        product.setPrice(new BigDecimal(8888));
//        product.setStatus(ProductStatus.B);
//        ProductCreateResult result = ProductController.INSTANCE.create(product).execute().body();
//        System.out.println(result);
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        Product product = new Product();
//        product.setName("iPhone X");
//        product.setPrice(new BigDecimal(8888));
//        product.setStatus(ProductStatus.B);
//        RequestBody picture = RequestBody.create(MediaType.parse("application/jpeg"), new File("E:\\DGSetup_1354E.exe"));
//        Response<ProductUpdateResult> response = ProductController.INSTANCE.update(1L, "Name", product, new RequestBody[]{picture, picture}, new LinkedHashMap<>()).execute();
//        ProductUpdateResult result = response.body();
//        System.out.println(result);
//    }

}
