package io.httpdoc.gen;

import io.httpdoc.core.Document;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.strategy.OverrideStrategy;
import io.httpdoc.jackson.deserialization.YamlDeserializer;
import io.httpdoc.objc.ObjCRSNetworkingGenerator;
import io.httpdoc.objc.ObjCSupplier;
import org.junit.Test;

import java.io.*;
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
        generation.setSupplier(new ObjCSupplier());
        Generator generator = new ObjCRSNetworkingGenerator("HD");
        generator.generate(generation);
    }

    @Test
    public void change() throws Exception {
        File directory = new File("");
        File[] files = directory.listFiles();
        for (File file : files) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            File newFile = new File(directory, "RS" + file.getName().substring(2));
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newFile));
            BufferedWriter bw = new BufferedWriter(osw);
            while ((line = br.readLine()) != null) {
                bw.write(line.replace("HD", "RS"));
            }
        }
    }

}
