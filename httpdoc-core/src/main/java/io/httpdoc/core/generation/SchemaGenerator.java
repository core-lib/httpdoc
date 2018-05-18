package io.httpdoc.core.generation;

import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;
import io.httpdoc.core.kit.IOKit;
import io.httpdoc.core.modeler.Archetype;
import io.httpdoc.core.modeler.Model;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import io.httpdoc.core.provider.Provider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 抽象的生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 12:36
 **/
public abstract class SchemaGenerator implements Generator {
    private final Modeler modeler;

    protected SchemaGenerator() {
        this(new SimpleModeler());
    }

    protected SchemaGenerator(Modeler modeler) {
        this.modeler = modeler;
    }

    @Override
    public void generate(Generation generation) throws IOException {
        Document document = generation.getDocument();
        if (document == null) return;
        Map<String, Schema> schemas = document.getSchemas();
        if (schemas == null) return;
        String directory = generation.getDirectory();
        String pkg = generation.getPkg();
        Provider provider = generation.getProvider();
        for (Schema schema : schemas.values()) {
            OutputStream out = null;
            try {
                String name = schema.getName();
                out = new FileOutputStream(directory + "/" + name + ".java");
                Archetype archetype = new Archetype(pkg, provider, schema);
                Model model = modeler.design(archetype);
                model.buildTo(out);
            } finally {
                IOKit.close(out);
            }
        }
    }
}
