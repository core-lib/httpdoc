package io.httpdoc.core.modeler;

import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.kit.IOKit;
import io.httpdoc.core.provider.Provider;

import java.io.File;
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
public abstract class ModelGenerator implements Generator {
    private final Modeler modeler;

    protected ModelGenerator(Modeler modeler) {
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
        boolean pkgForced = generation.isPkgForced();
        Provider provider = generation.getProvider();
        for (Schema schema : schemas.values()) {
            OutputStream out = null;
            try {
                String name = schema.getName();
                File file = new File(directory + "/" + name + ".java");
                File folder = file.getParentFile();
                if (!folder.exists() && !folder.mkdirs()) throw new IOException("could not create directory : " + folder);
                out = new FileOutputStream(file);
                Archetype archetype = new Archetype(pkg, pkgForced, provider, schema);
                Model model = modeler.design(archetype);
                model.buildTo(out);
            } finally {
                IOKit.close(out);
            }
        }
    }
}
