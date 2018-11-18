package io.httpdoc.retrofit;

import io.detector.Resource;
import io.detector.SimpleDetector;
import io.httpdoc.core.Document;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.kit.IOKit;
import io.httpdoc.jackson.deserialization.JsonDeserializer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Retrofit 导出器
 */
public class RetrofitExporter implements Exporter {

    @Override
    public String platform() {
        return "java";
    }

    @Override
    public String framework() {
        return "retrofit";
    }

    @Override
    public void export(String docURL, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        String bundle = "httpdoc-sdk/retrofit-sdk";
        Collection<Resource> resources = SimpleDetector.Builder.scan(bundle)
                .by(this.getClass().getClassLoader())
                .includeJar()
                .recursively()
                .build()
                .detect();
        for (Resource resource : resources) {
            try {
                URL url = resource.getUrl();
                String path = URLDecoder.decode(url.getPath(), Charset.defaultCharset().name());
                int index = path.lastIndexOf(bundle);
                String uri = path.substring(index + bundle.length());
                File file = new File(folder, uri);
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new IOException("could not make directory: " + file.getParentFile());
                }
                IOKit.transfer(resource.getInputStream(), file);
            } finally {
                IOKit.close(resource);
            }
        }

        RetrofitMergedGenerator generator = new RetrofitMergedGenerator();

        Document document = Document.from(new URL(docURL), new JsonDeserializer());
        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new RetrofitSupplier());

        generator.generate(generation);
    }

}
