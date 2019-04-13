package io.httpdoc.core.export;

import io.httpdoc.core.kit.IOKit;
import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.loadkit.Uris;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;

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
        Enumeration<Resource> resources = Loaders.ant(this.getClass().getClassLoader())
                .load(bundle + "/**");
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            try (InputStream in = resource.getInputStream()) {
                URL url = resource.getUrl();
                String path = Uris.decode(url.getPath(), Charset.defaultCharset().name());
                int index = path.lastIndexOf(bundle);
                String uri = path.substring(index + bundle.length());
                File file = new File(folder, uri);
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new IOException("could not make directory: " + file.getParentFile());
                }
                IOKit.transfer(in, file);
            }
        }
    }

}
