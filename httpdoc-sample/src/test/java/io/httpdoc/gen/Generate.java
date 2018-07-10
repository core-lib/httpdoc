package io.httpdoc.gen;

import io.httpdoc.core.Document;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.strategy.OverrideStrategy;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.jestful.server.JestfulClientCallbackGenerator;
import io.httpdoc.jestful.server.JestfulClientMergedGenerator;
import io.httpdoc.jestful.server.JestfulSupplier;

import java.io.IOException;
import java.net.URL;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-16 13:18
 **/
public class Generate {

    public static void main(String... args) throws IOException {
        Document document = Document.from(new URL("http://localhost:8080/httpdoc-sample/httpdoc.yaml"), new YamlDeserializer());
        Generation generation = new Generation(document);
        generation.setDirectory(System.getProperty("user.dir") + "\\httpdoc-sample\\src\\test\\java");
        generation.setPkg("io.httpdoc.gen");
        generation.setPkgForced(false);
        generation.setStrategy(new OverrideStrategy());
//        generation.setSupplier(new RetrofitSupplier());
//        Generator generator = new RetrofitMergedGenerator();
//                .include(RetrofitCallGenerator.class)
//                .include(RetrofitRxJavaGenerator.class)
//                .include(RetrofitJava8Generator.class)
//                .include(RetrofitGuavaGenerator.class);

        generation.setSupplier(new JestfulSupplier());
        Generator generator = new JestfulClientMergedGenerator()
                .exclude(JestfulClientCallbackGenerator.class);
//                .include(JestfulClientLambdaGenerator.class)
//                .include(JestfulClientFutureGenerator.class)
//                .include(JestfulClientGuavaGenerator.class)
//                .include(JestfulClientJava8Generator.class)
//                .include(JestfulClientMessageGenerator.class)
//                .include(JestfulClientEntityGenerator.class)
//                .include(JestfulClientHeaderGenerator.class)
//                .include(JestfulClientObservableGenerator.class);

        generator.generate(generation);
    }

}
