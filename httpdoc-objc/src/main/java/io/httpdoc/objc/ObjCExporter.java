package io.httpdoc.objc;

import io.httpdoc.core.Document;
import io.httpdoc.core.export.BundleExporter;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.generation.Generation;

import java.io.IOException;

/**
 * ObjC 导出器
 */
public class ObjCExporter extends BundleExporter implements Exporter {

    @Override
    public String platform() {
        return "iOS";
    }

    @Override
    public String framework() {
        return "RSNetworking";
    }

    @Override
    public void export(Document document, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        copy("httpdoc-sdk/objc-sdk", folder);

        ObjCRSNetworkingGenerator generator = new ObjCRSNetworkingGenerator();

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/sdk");
        generation.setSupplier(new ObjCSupplier());

        generator.generate(generation);
    }

}
