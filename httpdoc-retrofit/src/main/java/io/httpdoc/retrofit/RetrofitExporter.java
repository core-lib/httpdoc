package io.httpdoc.retrofit;

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
public class RetrofitExporter extends BundleExporter implements Exporter, Lifecycle {
    private RetrofitModeler modeler = new RetrofitSimpleModeler();
    private Collection<RetrofitAbstractGenerator> generators = Arrays.asList(
            new RetrofitCallGenerator(),
            new RetrofitObservableGenerator()
    );

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

        RetrofitMergedGenerator generator = new RetrofitMergedGenerator(modeler, generators);

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new RetrofitSupplier());

        generator.generate(generation);
    }

    @Override
    public void initial(Config config) throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();

        {
            String value = config.getInitParameter("retrofit.exporter.modeler");
            if (!StringKit.isBlank(value)) {
                Map<String, RetrofitModeler> map = LoadKit.load(classLoader, RetrofitModeler.class);
                RetrofitModeler modeler = map.get(value.trim());
                if (modeler == null) throw new HttpdocException("unrecognized modeler named: " + value + " currently supports " + map.keySet());
                this.modeler = modeler;
            }
        }

        {
            String value = config.getInitParameter("retrofit.exporter.generators");
            if (!StringKit.isBlank(value)) {
                Map<String, RetrofitAbstractGenerator> map = LoadKit.load(classLoader, RetrofitAbstractGenerator.class);
                Collection<RetrofitAbstractGenerator> generators = new ArrayList<>();
                String[] names = value.split("[,\\s\t\r\n]+");
                for (String name : names) {
                    if (StringKit.isBlank(name)) continue;
                    RetrofitAbstractGenerator generator = map.get(name);
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
