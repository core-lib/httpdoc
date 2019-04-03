package io.httpdoc.retrofit;

import io.httpdoc.core.*;
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
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;
import retrofit.converter.Converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Retrofit Client 标准生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-14 13:39
 **/
public class RetrofitStandardGenerator extends RetrofitAbstractGenerator {

    public RetrofitStandardGenerator() {
        super("", "");
    }

    public RetrofitStandardGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public RetrofitStandardGenerator(Collection<Class<? extends Converter>> converterFactories) {
        super(converterFactories);
    }

    public RetrofitStandardGenerator(String prefix, String suffix, Collection<Class<? extends Converter>> converterFactories) {
        super(prefix, suffix, converterFactories);
    }

    public RetrofitStandardGenerator(Modeler<ClassFragment> modeler) {
        super(modeler);
    }

    public RetrofitStandardGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    public RetrofitStandardGenerator(Modeler<ClassFragment> modeler, Collection<Class<? extends Converter>> converterFactories) {
        super(modeler, converterFactories);
    }

    public RetrofitStandardGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix, Collection<Class<? extends Converter>> converterFactories) {
        super(modeler, prefix, suffix, converterFactories);
    }

    @Override
    protected Collection<MethodFragment> generate(OperationGenerateContext context) {
        Generation generation = context.getGeneration();
        Controller controller = context.getController();
        Document document = context.getDocument();
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();
        Operation operation = context.getOperation();
        MethodFragment method = new MethodFragment(0);
        method.setComment(operation.getDescription());
        Collection<HDAnnotation> annotations = annotate(document, controller, operation);
        method.getAnnotations().addAll(annotations);
        Result result = operation.getResult();
        HDType type = result != null && result.getType() != null ? result.getType().isVoid() ? null : result.getType().toType(pkg, pkgForced, supplier) : null;
        HDType returnType = type != null ? type : HDType.valueOf(ResponseBody.class);
        String comment = result != null ? result.getDescription() : null;
        method.setResultFragment(new ResultFragment(returnType, comment));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters() != null ? operation.getParameters() : Collections.<Parameter>emptyList();
        Collection<ParameterFragment> fragments = generate(new ParameterGenerateContext(generation, controller, operation, parameters));
        method.getParameterFragments().addAll(fragments);
        return Collections.singleton(method);
    }

}
