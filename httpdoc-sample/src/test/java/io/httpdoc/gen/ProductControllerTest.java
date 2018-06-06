package io.httpdoc.gen;

import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.util.HashMap;
import java.util.Map;

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

        Lock lock = new SimpleLock();
        Map<String, String[]> map = new HashMap<>();
        map.put("a", new String[]{"a", "a"});
        map.put("b", new String[]{"b", "b"});
        ListenableFuture<ProductListResult> future = ProductController.INSTANCE.listForGuava(1, 20, ProductStatus.A, new String[]{"4", "5", "6"}, null, map);
        ProductListResult entity = future.get();
        System.out.println(entity);

        // status=A&arr[0]=1&arr[1]=2&arr[2]=3&a[0]=a&a[1]=a&b[0]=b&b[1]=b
        // status=A&arr[0][0]=1&arr[0][1]=2&arr[0][2]=3&arr[1][0]=4&arr[1][1]=5&arr[1][2]=6&a[0]=a&a[1]=a&b[0]=b&b[1]=b
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
