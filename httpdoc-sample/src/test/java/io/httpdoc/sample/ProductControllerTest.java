package io.httpdoc.sample;

import io.httpdoc.gen.ProductController;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 18:35
 **/
public class ProductControllerTest {

    public static void main(String... args) throws IOException {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/txt"),
                new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")
        );
        MultipartBody.Part file = MultipartBody.Part.createFormData(
                "files",
                "filename.ext",
                requestBody
        );
        Map<String, RequestBody> map = new LinkedHashMap<>();
        map.put("files", requestBody);
        map.put("filesff", requestBody);
        Response<String> execute = ProductController.INSTANCE.fForCall(new String[]{"a", "b"}, map).execute();
        System.out.println(execute);
    }

}
