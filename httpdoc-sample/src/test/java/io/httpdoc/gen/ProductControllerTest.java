package io.httpdoc.gen;

import org.junit.Test;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    public String toName(String name) {
        int index = 0;
        for (int i = 0; i < name.length() && name.charAt(i) >= 'A' && name.charAt(i) <= 'Z'; i++) index++;
        return name.substring(0, index > 1 ? index - 1 : index).toLowerCase() + name.substring(index > 1 ? index - 1 : index);
    }

    @Test
    public void testList() throws Exception {
        System.out.println(toName("InputStream"));
//
//        Lock lock = new SimpleLock();
//        ListenableFuture<ProductListResult> future = ProductController.INSTANCE.listForGuava(1, 20, ProductStatus.A);
//        ProductListResult entity = future.get();
//        System.out.println(entity);
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
@Test
public void testUpdate() throws Exception {
//    Product product = new Product();
//    product.setName("iPhone X");
//    product.setPrice(new BigDecimal(8888));
//    product.setStatus(ProductStatus.B);
//    ProductUpdateResult productUpdateResult = ProductController.INSTANCE.updateForFuture(12L, "", product, new Part[]{new FilePart("D:\\用户目录\\下载\\JestfulHttpdocController.java")}, null).get();
}

}
