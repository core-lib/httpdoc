package io.httpdoc.objc;

import io.httpdoc.core.Document;
import io.httpdoc.core.export.BundleExporter;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.jackson.deserialization.JsonDeserializer;

import java.io.IOException;
import java.net.URL;

/**
 * ObjC 导出器
 */
public class ObjCExporter extends BundleExporter implements Exporter {

    @Override
    public String platform() {
        return "objective-c";
    }

    @Override
    public String framework() {
        return "RSNetworking";
    }

    @Override
    public void export(String docURL, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        copy("httpdoc-sdk/objc-sdk", folder);

        ObjCRSNetworkingGenerator generator = new ObjCRSNetworkingGenerator();

        Document document = Document.from(new URL(docURL), new JsonDeserializer());
        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new ObjCSupplier());

        generator.generate(generation);
    }


}
