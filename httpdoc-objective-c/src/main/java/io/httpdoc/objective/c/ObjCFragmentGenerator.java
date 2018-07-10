package io.httpdoc.objective.c;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.generation.*;
import io.httpdoc.core.modeler.Archetype;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.strategy.Task;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.objective.c.fragment.ObjCClassFragment;
import io.httpdoc.objective.c.fragment.ObjCMethodFragment;
import io.httpdoc.objective.c.fragment.ObjCParameterFragment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class ObjCFragmentGenerator implements Generator {
    private final Modeler<ObjCClassFragment> modeler;

    protected ObjCFragmentGenerator(Modeler<ObjCClassFragment> modeler) {
        this.modeler = modeler;
    }

    @Override
    public void generate(Generation generation) throws IOException {
        Document document = generation.getDocument();
        if (document == null) return;
        Map<String, Schema> schemas = document.getSchemas() != null ? document.getSchemas() : Collections.<String, Schema>emptyMap();
        Set<Controller> controllers = document.getControllers() != null ? document.getControllers() : Collections.<Controller>emptySet();
        String directory = generation.getDirectory();
        Strategy strategy = generation.getStrategy();
        Collection<ClassFragment> fragments = new LinkedHashSet<>();
        for (Schema schema : schemas.values()) fragments.addAll(generate(new SchemaGenerateContext(generation, schema)));
        for (Controller controller : controllers) fragments.addAll(generate(new ControllerGenerateContext(generation, controller)));
        Collection<Claxx> classes = new LinkedHashSet<>();
        for (ClassFragment fragment : fragments) {
            HDClass clazz = fragment.getClazz();
            String className = clazz.getName();
            String extension = clazz.getCategory() == HDClass.Category.CLASS ? ".m" : ".h";
            String classPath = File.separator + className.replace(".", File.separator) + extension;
            Claxx claxx = new Claxx(classPath, fragment, Preference.DEFAULT);
            classes.add(claxx);
        }
        Task task = new Task(directory, classes);
        strategy.execute(task);
    }

    protected Collection<ObjCClassFragment> generate(SchemaGenerateContext context) {
        Document document = context.getDocument();
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Schema schema = context.getSchema();
        Archetype archetype = new Archetype(document, pkg, pkgForced, supplier, schema);
        return modeler.design(archetype);
    }

    protected abstract Collection<ObjCClassFragment> generate(ControllerGenerateContext context);

    protected abstract Collection<ObjCMethodFragment> generate(OperationGenerateContext context);

    protected abstract Collection<ObjCParameterFragment> generate(ParameterGenerateContext context);

}
