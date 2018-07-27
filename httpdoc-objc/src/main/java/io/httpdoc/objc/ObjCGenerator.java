package io.httpdoc.objc;

import io.httpdoc.core.Claxx;
import io.httpdoc.core.Document;
import io.httpdoc.core.Preference;
import io.httpdoc.core.Schema;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.generation.SchemaGenerateContext;
import io.httpdoc.core.modeler.Archetype;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.strategy.Task;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.objc.core.ObjCDocument;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * ObjC 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:26
 **/
public class ObjCGenerator implements Generator {
    private final static String DEFAULT_PREFIX = "HD";
    private final String prefix;
    private final Modeler<ObjCFile> modeler;

    public ObjCGenerator() {
        this(DEFAULT_PREFIX);
    }

    public ObjCGenerator(String prefix) {
        this(prefix, new MJExtensionModeler(prefix));
    }

    public ObjCGenerator(Modeler<ObjCFile> modeler) {
        this(DEFAULT_PREFIX, modeler);
    }

    public ObjCGenerator(String prefix, Modeler<ObjCFile> modeler) {
        this.prefix = prefix;
        this.modeler = modeler;
    }

    @Override
    public void generate(Generation generation) throws IOException {
        Document document = generation.getDocument() != null ? new ObjCDocument(prefix, generation.getDocument()) : null;
        if (document == null) return;
        Map<String, Schema> schemas = document.getSchemas() != null ? document.getSchemas() : Collections.<String, Schema>emptyMap();
        String directory = generation.getDirectory();
        Strategy strategy = generation.getStrategy();
        Collection<ObjCFile> files = new LinkedHashSet<>();
        for (Schema schema : schemas.values()) files.addAll(generate(new SchemaGenerateContext(generation, schema)));
        Collection<Claxx> classes = new LinkedHashSet<>();
        for (ObjCFile file : files) {
            String pkg = file.getPkg();
            String name = file.getName();
            String extension = file.getType().extension;
            String className = pkg + "." + name;
            String classPath = File.separator + className.replace(".", File.separator) + extension;
            Claxx claxx = new Claxx(classPath, file, Preference.DEFAULT);
            classes.add(claxx);
        }
        Task task = new Task(directory, classes);
        strategy.execute(task);
    }

    protected Collection<ObjCFile> generate(SchemaGenerateContext context) {
        Document document = context.getDocument();
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Schema schema = context.getSchema();
        Archetype archetype = new Archetype(document, pkg, pkgForced, supplier, schema);
        return modeler.design(archetype);
    }

}
