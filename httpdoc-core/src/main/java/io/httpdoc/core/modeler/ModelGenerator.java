package io.httpdoc.core.modeler;

import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.strategy.Claxx;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.supplier.Supplier;

import java.io.IOException;
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
        Supplier supplier = generation.getSupplier();
        Strategy strategy = generation.getStrategy();
        for (Schema schema : schemas.values()) {
            Archetype archetype = new Archetype(pkg, pkgForced, supplier, schema);
            Claxx model = modeler.design(archetype);
            strategy.reply(directory, model);
        }
    }
}
