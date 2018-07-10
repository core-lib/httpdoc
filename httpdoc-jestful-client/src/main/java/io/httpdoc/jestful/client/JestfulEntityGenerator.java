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
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.client.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Jestful Client 实体 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:39
 **/
public class JestfulEntityGenerator extends JestfulAbstractGenerator {

    public JestfulEntityGenerator() {
        super("", "ForEntity");
    }

    public JestfulEntityGenerator(Modeler modeler) {
        super(modeler);
    }

    public JestfulEntityGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public JestfulEntityGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        Operation operation = context.getOperation();
        MethodFragment method = new MethodFragment(0);
        method.setComment(operation.getDescription());
        Collection<HDAnnotation> annotations = annotate(operation);
        method.getAnnotations().addAll(annotations);
        Result result = operation.getResult();
        method.setResultFragment(new ResultFragment(HDType.valueOf(Entity.class), result != null ? result.getDescription() : null));
        method.setName(name(operation.getName()));
        Generation generation = context.getGeneration();
        Controller controller = context.getController();
        List<Parameter> parameters = operation.getParameters() != null ? operation.getParameters() : Collections.<Parameter>emptyList();
        Collection<ParameterFragment> fragments = generate(new ParameterGenerateContext(generation, controller, operation, parameters));
        method.getParameterFragments().addAll(fragments);
        return Collections.singleton(method);
    }
}
