package io.httpdoc.sample;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.retrofit.RetrofitCallGenerator;
import io.httpdoc.retrofit.RetrofitMergedGenerator;
import io.httpdoc.retrofit.RetrofitProvider;
import io.httpdoc.retrofit.RetrofitRxJavaGenerator;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-16 13:18
 **/
public class Generate {

    public static void main(String... args) throws IOException {
        Document document = Document.from(new URL("http://localhost:8080/httpdoc-sample/httpdoc.yaml"), new YamlDeserializer());
        Generation generation = new Generation();
        generation.setDocument(document);
        generation.setPkg("io.httpdoc.gen");
        generation.setDirectory(System.getProperty("user.dir") + "\\httpdoc-sample\\src\\main\\java\\io\\httpdoc\\gen");
        generation.setProvider(new RetrofitProvider());
        Generator generator = new RetrofitMergedGenerator()
                .include(RetrofitCallGenerator.class)
                .include(RetrofitRxJavaGenerator.class)
                .use(GsonConverterFactory.class);

        generator.generate(generation);
    }

}
