package io.httpdoc.sample;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    public static void main(String... args) throws IOException {
        String text = "/product/name-{name:\\w+}/{price:\\d+}}";

        StringTokenizer tokenizer = new StringTokenizer(text, "{:}", true);
        while (tokenizer.hasMoreTokens()) {

        }

    }

}
