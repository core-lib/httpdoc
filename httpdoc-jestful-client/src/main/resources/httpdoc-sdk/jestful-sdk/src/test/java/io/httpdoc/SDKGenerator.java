package io.httpdoc;

import io.httpdoc.core.Document;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.jackson.deserialization.JsonDeserializer;
import io.httpdoc.jestful.client.*;
import org.junit.Test;

import java.net.URL;

public class SDKGenerator {

    @Test
    public void generate() throws Exception {
        JestfulMergedGenerator generator = new JestfulMergedGenerator();
//        generator.include(JestfulStandardGenerator.class);
//        generator.include(JestfulCallbackGenerator.class);
//        generator.include(JestfulEntityGenerator.class);
//        generator.include(JestfulFutureGenerator.class);
//        generator.include(JestfulGuavaGenerator.class);
//        generator.include(JestfulHeaderGenerator.class);
//        generator.include(JestfulJava8Generator.class);
//        generator.include(JestfulLambdaGenerator.class);
//        generator.include(JestfulMessageGenerator.class);
//        generator.include(JestfulRxJavaGenerator.class);

        Document document = Document.from(new URL("http://localhost:8080/httpdoc.json"), new JsonDeserializer());
        Generation generation = new Generation(document);
        generation.setDirectory(System.getProperty("user.dir") + "/src/main/java"); // set generate directory
        generation.setSupplier(new JestfulSupplier());                              // set type supplier
//        generation.setPkg();                                                      // set default package
//        generation.setPkgForced();                                                // set forced package
//        generation.setStrategy();                                                 // set generate strategy

        generator.generate(generation);
    }

}
