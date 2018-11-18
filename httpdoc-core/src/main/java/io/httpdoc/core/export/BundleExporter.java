package io.httpdoc.core.export;

import io.detector.Resource;
import io.detector.SimpleDetector;
import io.httpdoc.core.kit.IOKit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 工程模板导出器
 */
public abstract class BundleExporter implements Exporter {

    /**
     * 将工程模板拷贝到指定目录
     *
     * @param bundle 工程模板
     * @param folder 导出目录
     * @throws IOException I/O异常
     */
    protected void copy(String bundle, String folder) throws IOException {
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
    }

}
