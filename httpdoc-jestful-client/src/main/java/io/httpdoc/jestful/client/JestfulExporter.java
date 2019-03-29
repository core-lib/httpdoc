package io.httpdoc.jestful.client;

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
 * Jestful 导出器
 */
public class JestfulExporter extends BundleExporter implements Exporter, Lifecycle {
    private JestfulModeler modeler = new JestfulSimpleModeler();
    private Collection<JestfulAbstractGenerator> generators = Arrays.asList(
            new JestfulStandardGenerator(),
            new JestfulCallbackGenerator()
    );

    @Override
    public String platform() {
        return "Java";
    }

    @Override
    public String framework() {
        return "Jestful";
    }

    @Override
    public void export(Document document, String folder) throws IOException {
        // 将工程模板文件复制到该目录
        copy("httpdoc-sdk/jestful-sdk", folder);

        JestfulMergedGenerator generator = new JestfulMergedGenerator(modeler, generators);

        Generation generation = new Generation(document);
        generation.setDirectory(folder + "/src/main/java");
        generation.setSupplier(new JestfulSupplier());

        generator.generate(generation);
    }

    @Override
    public void initial(Config config) throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();

        {
            String value = config.getInitParameter("jestful.exporter.modeler");
            if (!StringKit.isBlank(value)) {
                Map<String, JestfulModeler> map = LoadKit.load(classLoader, JestfulModeler.class);
                JestfulModeler modeler = map.get(value.trim());
                if (modeler == null) throw new HttpdocException("unrecognized modeler named: " + value + " currently supports " + map.keySet());
                this.modeler = modeler;
            }
        }

        {
            String value = config.getInitParameter("jestful.exporter.generators");
            if (!StringKit.isBlank(value)) {
                Map<String, JestfulAbstractGenerator> map = LoadKit.load(classLoader, JestfulAbstractGenerator.class);
                Collection<JestfulAbstractGenerator> generators = new ArrayList<>();
                String[] names = value.split("[,\\s\t\r\n]+");
                for (String name : names) {
                    if (StringKit.isBlank(name)) continue;
                    JestfulAbstractGenerator generator = map.get(name);
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
