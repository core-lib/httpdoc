package io.httpdoc.jestful.client;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.fragment.ResultFragment;
import io.httpdoc.core.fragment.annotation.HDAnnotation;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.OperationGenerateContext;
import io.httpdoc.core.generation.ParameterGenerateContext;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Retrofit Completable Future 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 17:40
 **/
public class JestfulJava8Generator extends JestfulAbstractGenerator {

    public JestfulJava8Generator() {
        super("", "ForJava8");
    }

    public JestfulJava8Generator(Modeler<ClassFragment> modeler) {
        super(modeler);
    }

    public JestfulJava8Generator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulJava8Generator(Modeler<ClassFragment> modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Operation operation = context.getOperation();
        Class<?> clazz;
        try {
            clazz = Class.forName("java.util.concurrent.CompletableFuture");
        } catch (ClassNotFoundException e) {
            throw new HttpdocRuntimeException("this generator can only used in jdk 8 or greater versions");
        }
        MethodFragment method = new MethodFragment(0);
        method.setComment(operation.getDescription());
        Collection<HDAnnotation> annotations = annotate(operation);
        method.getAnnotations().addAll(annotations);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null
                ? result.getType().isVoid()
                ? null
                : result.getType().isPrimitive()
                ? result.getType().toWrapper().toType(pkg, pkgForced, supplier)
                : result.getType().toType(pkg, pkgForced, supplier)
                : null;
        HDParameterizedType returnType = new HDParameterizedType(HDType.valueOf(clazz), null, type != null ? type : HDType.valueOf(Entity.class));
        String comment = result != null ? result.getDescription() : null;
        method.setResultFragment(new ResultFragment(returnType, comment));
        method.setName(name(operation.getName()));
        Generation generation = context.getGeneration();
        Controller controller = context.getController();
        List<Parameter> parameters = operation.getParameters() != null ? operation.getParameters() : Collections.<Parameter>emptyList();
        Collection<ParameterFragment> fragments = generate(new ParameterGenerateContext(generation, controller, operation, parameters));
        method.getParameterFragments().addAll(fragments);
        return Collections.singleton(method);
    }

}
