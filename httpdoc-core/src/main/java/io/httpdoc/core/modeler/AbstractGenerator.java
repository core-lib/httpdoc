package io.httpdoc.core.modeler;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.strategy.Claxx;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.strategy.Task;
import io.httpdoc.core.supplier.Supplier;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 抽象的生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 12:36
 **/
public abstract class AbstractGenerator implements Generator {
    private final Modeler modeler;

    protected AbstractGenerator(Modeler modeler) {
        this.modeler = modeler;
    }

    @Override
    public void generate(Generation generation) throws IOException {
        Document document = generation.getDocument();
        if (document == null) return;
        Map<String, Schema> schemas = document.getSchemas();
        Set<Controller> controllers = document.getControllers();
        String directory = generation.getDirectory();
        String pkg = generation.getPkg();
        boolean pkgForced = generation.isPkgForced();
        Supplier supplier = generation.getSupplier();
        Strategy strategy = generation.getStrategy();
        Collection<Claxx> classes = new LinkedHashSet<>();
        if (schemas != null) for (Schema schema : schemas.values()) classes.add(generate(document, pkg, pkgForced, supplier, schema));
        if (controllers != null) for (Controller controller : controllers) classes.add(generate(document, pkg, pkgForced, supplier, controller));
        Task task = new Task(directory, classes);
        strategy.execute(task);
    }

    protected Claxx generate(Document document, String pkg, boolean pkgForced, Supplier supplier, Schema schema) {
        Archetype archetype = new Archetype(document, pkg, pkgForced, supplier, schema);
        return modeler.design(archetype);
    }

    protected abstract Claxx generate(Document document, String pkg, boolean pkgForced, Supplier supplier, Controller controller) throws IOException;

}
