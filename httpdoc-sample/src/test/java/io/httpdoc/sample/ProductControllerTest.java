package io.httpdoc.sample;

import io.httpdoc.gen.ProductController;
import org.junit.Test;

import java.io.File;
import java.util.*;

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

    @Test
    public void testC() {
        ProductController.INSTANCE.c(Arrays.asList(
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"),
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")
        ));
    }

    @Test
    public void testD() {
        ProductController.INSTANCE.d(new LinkedHashSet<>(Arrays.asList(
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"),
                new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql")
        )));
    }

    @Test
    public void testE() {
        ProductController.INSTANCE.e(new LinkedHashSet<>(Arrays.asList(
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"),
                new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql")
        )));
    }

    @Test
    public void testF() {
        Map<String, File> map = new HashMap<>();
        map.put("files", new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"));
        map.put("files", new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"));
        ProductController.INSTANCE.f(new String[]{"sdfsdf", "sdf"}, map);
    }

    @Test
    public void testG() {
        Map<String, File[]> map = new HashMap<>();
        map.put("files", new File[]{new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")});
        map.put("sql", new File[]{new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql")});
        ProductController.INSTANCE.g(new String[]{"sdfsdf", "sdf"}, map);
    }

    @Test
    public void testH() {
        Map<String, List<File>> map = new HashMap<>();
        map.put("files", Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")));
        map.put("sql", Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql")));
        ProductController.INSTANCE.h(new String[]{"sdfsdf", "sdf"}, map);
    }

    @Test
    public void testI() {
        Map<String, Set<File>> map = new HashMap<>();
        map.put("files", new HashSet<>(Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"))));
        map.put("sql", new HashSet<>(Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"))));
        ProductController.INSTANCE.i(new String[]{"sdfsdf", "sdf"}, map);
    }

    @Test
    public void testJ() {
        Map<String, Collection<File>> map = new HashMap<>();
        map.put("files", Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt"), new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")));
        map.put("sql", Arrays.asList(new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql"), new File("C:\\Users\\Administrator\\Desktop\\new_mf_pro.sql")));
        ProductController.INSTANCE.j(new String[]{"sdfsdf", "sdf"}, map);
    }



}
