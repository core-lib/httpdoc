package io.httpdoc.retrofit2;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Retrofit 导出器
 */
public class Retrofit2Exporter extends BundleExporter implements Exporter, Lifecycle {
    private Retrofit2Modeler modeler = new Retrofit2SimpleModeler();
    private Collection<Retrofit2AbstractGenerator> generators = Arrays.asList(
            new Retrofit2CallGenerator(),
            new Retrofit2ObservableGenerator()
    );

    @Override
    public String platform() {
        return "Android";
    }

    @Override
    public String framework() {
        return "Retrofit2";
    }

    @Override
    public void export(Document document, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        copy("httpdoc-sdk/retrofit2-sdk", folder);

        Retrofit2MergedGenerator generator = new Retrofit2MergedGenerator(modeler, generators);

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new Retrofit2Supplier());

        generator.generate(generation);
    }

    @Override
    public void initial(Config config) throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();

        {
            String value = config.getInitParameter("retrofit2.exporter.modeler");
            if (!StringKit.isBlank(value)) {
                Map<String, Retrofit2Modeler> map = LoadKit.load(classLoader, Retrofit2Modeler.class);
                Retrofit2Modeler modeler = map.get(value.trim());
                if (modeler == null) throw new HttpdocException("unrecognized modeler named: " + value + " currently supports " + map.keySet());
                this.modeler = modeler;
            }
        }

        {
            String value = config.getInitParameter("retrofit2.exporter.generators");
            if (!StringKit.isBlank(value)) {
                Map<String, Retrofit2AbstractGenerator> map = LoadKit.load(classLoader, Retrofit2AbstractGenerator.class);
                Collection<Retrofit2AbstractGenerator> generators = new ArrayList<>();
                String[] names = value.split("[,\\s\t\r\n]+");
                for (String name : names) {
                    if (StringKit.isBlank(name)) continue;
                    Retrofit2AbstractGenerator generator = map.get(name);
                    if (generator == null) throw new HttpdocException("unrecognized generator named: " + name + " currently supports " + map.keySet());
                    generators.add(generator);
                }
                this.generators = generators;
            }
        }
    }

    @Override
    public void destroy() {

    }
}
