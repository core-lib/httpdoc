package io.httpdoc.retrofit;

import io.httpdoc.core.Document;
import io.httpdoc.core.export.BundleExporter;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.generation.Generation;

import java.io.IOException;

/**
 * Retrofit 导出器
 */
public class RetrofitExporter extends BundleExporter implements Exporter {

    @Override
    public String platform() {
        return "Android";
    }

    @Override
    public String framework() {
        return "Retrofit";
    }

    @Override
    public void export(Document document, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        copy("httpdoc-sdk/retrofit-sdk", folder);

        RetrofitMergedGenerator generator = new RetrofitMergedGenerator();

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new RetrofitSupplier());

        generator.generate(generation);
    }

}
