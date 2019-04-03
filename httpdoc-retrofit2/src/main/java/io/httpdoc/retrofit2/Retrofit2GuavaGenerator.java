package io.httpdoc.retrofit2;

import com.google.common.util.concurrent.ListenableFuture;
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
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Retrofit Guava 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 17:45
 **/
public class Retrofit2GuavaGenerator extends Retrofit2AbstractGenerator {
    public Retrofit2GuavaGenerator() {
        super("", "ForGuava");
    }

    public Retrofit2GuavaGenerator(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public Retrofit2GuavaGenerator(Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(converterFactories);
    }

    public Retrofit2GuavaGenerator(String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(prefix, suffix, converterFactories);
    }

    public Retrofit2GuavaGenerator(Modeler<ClassFragment> modeler) {
        super(modeler);
    }

    public Retrofit2GuavaGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix) {
        super(modeler, prefix, suffix);
    }

    public Retrofit2GuavaGenerator(Modeler<ClassFragment> modeler, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler, converterFactories);
    }

    public Retrofit2GuavaGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
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
        HDType type = result != null && result.getType() != null
                ? result.getType().isVoid()
                ? null
                : result.getType().isPrimitive()
                ? result.getType().toWrapper().toType(pkg, pkgForced, supplier)
                : result.getType().toType(pkg, pkgForced, supplier)
                : null;
        HDParameterizedType returnType = new HDParameterizedType(HDType.valueOf(ListenableFuture.class), null, type != null ? type : HDType.valueOf(ResponseBody.class));
        String comment = result != null ? result.getDescription() : null;
        method.setResultFragment(new ResultFragment(returnType, comment));
        method.setName(name(operation.getName()));
        List<Parameter> parameters = operation.getParameters() != null ? operation.getParameters() : Collections.<Parameter>emptyList();
        Collection<ParameterFragment> fragments = generate(new ParameterGenerateContext(generation, controller, operation, parameters));
        method.getParameterFragments().addAll(fragments);
        return Collections.singleton(method);
    }

    @Override
    protected Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories() {
        return Collections.<Class<? extends CallAdapter.Factory>>singleton(GuavaCallAdapterFactory.class);
    }
}
