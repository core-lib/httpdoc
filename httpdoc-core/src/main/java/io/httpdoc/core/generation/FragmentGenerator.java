package io.httpdoc.core.generation;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.modeler.Archetype;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.strategy.Task;
import io.httpdoc.core.supplier.Supplier;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 抽象的生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 12:36
 **/
public abstract class FragmentGenerator implements Generator {
    private final Modeler<ClassFragment> modeler;

    protected FragmentGenerator(Modeler<ClassFragment> modeler) {
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
            String className = fragment.getClazz().getName();
            String classPath = File.separator + className.replace(".", File.separator) + ".java";
            Claxx claxx = new Claxx(classPath, fragment, Preference.DEFAULT);
            classes.add(claxx);
        }
        Task task = new Task(directory, classes);
        strategy.execute(task);
    }

    protected Collection<ClassFragment> generate(SchemaGenerateContext context) {
        Document document = context.getDocument();
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Schema schema = context.getSchema();
        Archetype archetype = new Archetype(document, pkg, pkgForced, supplier, schema);
        return modeler.design(archetype);
    }

    protected abstract Collection<ClassFragment> generate(ControllerGenerateContext context);

    protected abstract Collection<MethodFragment> generate(OperationGenerateContext context);

    protected abstract Collection<ParameterFragment> generate(ParameterGenerateContext context);

}
