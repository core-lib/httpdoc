package io.httpdoc.retrofit;

import io.httpdoc.core.*;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.CommentFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.fragment.annotation.HDAnnotation;
import io.httpdoc.core.fragment.annotation.HDAnnotationConstant;
import io.httpdoc.core.generation.*;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.http.*;

import java.util.*;

import static io.httpdoc.core.Parameter.*;

/**
 * Retrofit Client 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 15:59
 **/
public abstract class RetrofitAbstractGenerator extends FragmentGenerator implements Generator {
    protected final static Collection<String> SCOPES = Arrays.asList(
            Parameter.HTTP_PARAM_SCOPE_HEADER,
            Parameter.HTTP_PARAM_SCOPE_PATH,
            Parameter.HTTP_PARAM_SCOPE_QUERY,
            Parameter.HTTP_PARAM_SCOPE_BODY,
            Parameter.HTTP_PARAM_SCOPE_FIELD
    );
    protected final String prefix;
    protected final String suffix;
    protected final Set<Class<? extends Converter.Factory>> converterFactories = new LinkedHashSet<>();

    protected RetrofitAbstractGenerator() {
        this("", "");
    }

    protected RetrofitAbstractGenerator(String prefix, String suffix) {
        this(prefix, suffix, Collections.<Class<? extends Converter.Factory>>emptyList());
    }

    protected RetrofitAbstractGenerator(Collection<Class<? extends Converter.Factory>> converterFactories) {
        this("", "", converterFactories);
    }

    protected RetrofitAbstractGenerator(String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        this(new SimpleModeler(), prefix, suffix, converterFactories);
    }

    protected RetrofitAbstractGenerator(Modeler<ClassFragment> modeler) {
        this(modeler, "", "");
    }

    protected RetrofitAbstractGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix) {
        this(modeler, prefix, suffix, Collections.<Class<? extends Converter.Factory>>emptyList());
    }

    protected RetrofitAbstractGenerator(Modeler<ClassFragment> modeler, Collection<Class<? extends Converter.Factory>> converterFactories) {
        this(modeler, "", "", converterFactories);
    }

    protected RetrofitAbstractGenerator(Modeler<ClassFragment> modeler, String prefix, String suffix, Collection<Class<? extends Converter.Factory>> converterFactories) {
        super(modeler);
        if (prefix == null || suffix == null || converterFactories == null) throw new NullPointerException();
        this.prefix = prefix.trim();
        this.suffix = suffix.trim();
        this.converterFactories.addAll(converterFactories);
    }

    protected Collection<ClassFragment> generate(ControllerGenerateContext context) {
        Generation generation = context.getGeneration();
        String pkgGenerated = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Controller controller = context.getController();
        String comment = "Generated By Httpdoc";
        String name = controller.getName();
        ClassFragment interfase = new ClassFragment();
        String pkgTranslated = controller.getPkg();
        String pkg = pkgForced || pkgTranslated == null ? pkgGenerated : pkgTranslated;
        interfase.setPkg(pkg);
        interfase.setCommentFragment(new CommentFragment(controller.getDescription() != null ? controller.getDescription() + "\n" + comment : comment));
        interfase.setClazz(new HDClass(HDClass.Category.INTERFACE, pkg + "." + name));

        List<Operation> operations = controller.getOperations() != null ? controller.getOperations() : Collections.<Operation>emptyList();
        for (Operation operation : operations) interfase.getMethodFragments().addAll(generate(new OperationGenerateContext(generation, controller, operation)));

        return Collections.singleton(interfase);
    }

    protected abstract Collection<MethodFragment> generate(OperationGenerateContext context);

    protected abstract Set<Class<? extends CallAdapter.Factory>> getCallAdapterFactories();

    protected String name(String name) {
        if (prefix.isEmpty()) return name + suffix;
        else return prefix + name.substring(0, 1).toUpperCase() + name.substring(1) + suffix;
    }

    protected Collection<ParameterFragment> generate(ParameterGenerateContext context) {
        String pkg = context.getPkg();
        boolean pkgForced = context.isPkgForced();
        Supplier supplier = context.getSupplier();

        List<Parameter> parameters = context.getParameters();
        Collection<ParameterFragment> fragments = new LinkedHashSet<>();

        Operation operation = context.getOperation();
        boolean multipart = operation.isMultipart();

        for (int i = 0; parameters != null && i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            ParameterFragment parameter = new ParameterFragment();
            // 只处理能解析的
            String scope = param.getScope();
            if (!SCOPES.contains(scope)) continue;

            String name = StringKit.isBlank(param.getName()) ? param.getType().toName() : param.getName();
            // 去掉特殊字符
            name = name.replaceAll("[^0-9a-zA-Z_$]", "_");
            loop:
            while (true) {
                for (ParameterFragment fragment : fragments) {
                    if (name.equals(fragment.getName())) {
                        name = String.format("_%s", name);
                        continue loop;
                    }
                }
                break;
            }
            parameter.setName(name);
            Collection<HDAnnotation> annotations = annotate(param, multipart);
            parameter.getAnnotations().addAll(annotations);
            HDType type = param.getType().toType(pkg, pkgForced, supplier);
            parameter.setType(type);
            fragments.add(parameter);
        }
        return fragments;
    }

    protected Collection<HDAnnotation> annotate(Parameter parameter, boolean multipart) {
        String name = parameter.getName();
        switch (parameter.getScope()) {
            case HTTP_PARAM_SCOPE_HEADER: {
                HDAnnotation header = new HDAnnotation(Header.class);
                if (name != null) header.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                return Collections.singleton(header);
            }
            case HTTP_PARAM_SCOPE_PATH: {
                HDAnnotation path = new HDAnnotation(Path.class);
                if (name != null) path.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                return Collections.singleton(path);
            }
            case HTTP_PARAM_SCOPE_QUERY: {
                HDAnnotation query = new HDAnnotation(Query.class);
                if (name != null) query.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                return Collections.singleton(query);
            }
            case HTTP_PARAM_SCOPE_FIELD: {
                HDAnnotation query = new HDAnnotation(Query.class);
                if (name != null) query.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                return Collections.singleton(query);
            }
            case HTTP_PARAM_SCOPE_BODY: {
                if (parameter.getType().isPart()) {
                    if (parameter.getType().getCategory() == Category.DICTIONARY) {
                        HDAnnotation map = new HDAnnotation(PartMap.class);
                        return Collections.singleton(map);
                    } else {
                        HDAnnotation part = new HDAnnotation(Part.class);
                        if (name != null) part.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                        return Collections.singleton(part);
                    }
                } else {
                    if (multipart) {
                        HDAnnotation part = new HDAnnotation(Part.class);
                        if (name != null) part.getProperties().put("value", HDAnnotationConstant.valuesOf(name));
                        return Collections.singleton(part);
                    } else {
                        HDAnnotation body = new HDAnnotation(Body.class);
                        return Collections.singleton(body);
                    }
                }
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    protected Collection<HDAnnotation> annotate(Document document, Controller controller, Operation operation) {
        Collection<HDAnnotation> annotations = new LinkedHashSet<>();
        boolean multipart = operation.isMultipart();
        if (multipart) {
            HDAnnotation annotation = new HDAnnotation(Multipart.class);
            annotations.add(annotation);
        }

        StringBuilder builder = new StringBuilder();
        List<String> segments = Arrays.asList(document.getContext(), controller.getPath(), operation.getPath());
        for (String segment : segments) {
            if (segment == null) continue;
            builder.append("/").append(segment);
        }
        String path = builder.toString().replaceAll("/+", "/");

        switch (operation.getMethod()) {
            case "HEAD": {
                HDAnnotation head = new HDAnnotation(HEAD.class);
                head.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(head);
                break;
            }
            case "OPTIONS": {
                HDAnnotation options = new HDAnnotation(OPTIONS.class);
                options.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(options);
                break;
            }
            case "GET": {
                HDAnnotation get = new HDAnnotation(GET.class);
                get.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(get);
                break;
            }
            case "POST": {
                HDAnnotation post = new HDAnnotation(POST.class);
                post.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(post);
                break;
            }
            case "PUT": {
                HDAnnotation put = new HDAnnotation(PUT.class);
                put.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(put);
                break;
            }
            case "DELETE": {
                HDAnnotation delete = new HDAnnotation(DELETE.class);
                delete.getProperties().put("value", HDAnnotationConstant.valuesOf(path));
                annotations.add(delete);
                break;
            }
            default: {
                break;
            }
        }

        return annotations;
    }

}
