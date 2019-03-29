package io.httpdoc.objc;

import io.httpdoc.core.Config;
import io.httpdoc.core.Document;
import io.httpdoc.core.Lifecycle;
import io.httpdoc.core.exception.HttpdocException;
import io.httpdoc.core.export.BundleExporter;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.kit.LoadKit;
import io.httpdoc.core.kit.StringKit;

import java.io.IOException;
import java.util.Map;

/**
 * ObjC 导出器
 */
public class ObjCExporter extends BundleExporter implements Exporter, Lifecycle {
    private String prefix = "HD";
    private ObjCModeler modeler = new ObjCMJExtensionModeler();

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

        ObjCRSNetworkingGenerator generator = new ObjCRSNetworkingGenerator(prefix, modeler);

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/SDK");
        generation.setSupplier(new ObjCSupplier());

        generator.generate(generation);
    }

    @Override
    public void initial(Config config) throws Exception {
        {
            String value = config.getInitParameter("objc.exporter.prefix");
            if (!StringKit.isBlank(value)) {
                prefix = value.trim();
            }
        }
        {
            String value = config.getInitParameter("objc.exporter.modeler");
            if (!StringKit.isBlank(value)) {
                ClassLoader classLoader = this.getClass().getClassLoader();
                Map<String, ObjCModeler> map = LoadKit.load(classLoader, ObjCModeler.class);
                ObjCModeler modeler = map.get(value.trim());
                if (modeler == null) throw new HttpdocException("unrecognized modeler named: " + value + " currently supports " + map.keySet());
                this.modeler = modeler;
            }
        }
    }

    @Override
    public void destroy() {

    }
}
