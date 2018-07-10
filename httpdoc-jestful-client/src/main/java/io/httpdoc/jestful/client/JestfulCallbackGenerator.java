package io.httpdoc.jestful.client;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;
import io.httpdoc.core.Parameter;
import io.httpdoc.core.Result;
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
import org.qfox.jestful.client.scheduler.Callback;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Jestful Client 回调生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:32
 **/
public class JestfulCallbackGenerator extends JestfulAbstractGenerator {

    public JestfulCallbackGenerator() {
        super("", "");
    }

    public JestfulCallbackGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulCallbackGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulCallbackGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Operation operation = context.getOperation();
        MethodFragment method = new MethodFragment(0);
        method.setComment(operation.getDescription());
        Collection<HDAnnotation> annotations = annotate(operation);
        method.getAnnotations().addAll(annotations);
        Result result = operation.getResult();
        method.setResultFragment(new ResultFragment(HDType.valueOf(void.class), result != null ? result.getDescription() : null));
        method.setName(name(operation.getName()));
        Generation generation = context.getGeneration();
        Controller controller = context.getController();
        List<Parameter> parameters = operation.getParameters() != null ? operation.getParameters() : Collections.<Parameter>emptyList();
        Collection<ParameterFragment> fragments = generate(new ParameterGenerateContext(generation, controller, operation, parameters));
        method.getParameterFragments().addAll(fragments);

        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, supplier) : null;
        ParameterFragment callback = new ParameterFragment();
        callback.setType(new HDParameterizedType(HDType.valueOf(Callback.class), null, type != null ? type : HDType.valueOf(Entity.class)));
        callback.setName("callback");
        method.getParameterFragments().add(callback);

        return Collections.singleton(method);
    }

}
