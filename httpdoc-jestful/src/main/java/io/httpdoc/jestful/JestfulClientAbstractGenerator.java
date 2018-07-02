package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.appender.FileAppender;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.CommentFragment;
import io.httpdoc.core.fragment.MethodFragment;
import io.httpdoc.core.fragment.ParameterFragment;
import io.httpdoc.core.fragment.annotation.HDAnnotation;
import io.httpdoc.core.fragment.annotation.HDAnnotationConstant;
import io.httpdoc.core.generation.Generation;
import io.httpdoc.core.generation.Generator;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.modeler.ModelGenerator;
import io.httpdoc.core.modeler.Modeler;
import io.httpdoc.core.modeler.SimpleModeler;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDClass;
import org.qfox.jestful.core.http.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static io.httpdoc.core.Parameter.*;

/**
 * Jestful Client 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 15:59
 **/
public abstract class JestfulClientAbstractGenerator extends ModelGenerator implements Generator {
    protected final String prefix;
    protected final String suffix;

    protected JestfulClientAbstractGenerator() {
        this("", "");
    }

    protected JestfulClientAbstractGenerator(Modeler modeler) {
        this(modeler, "", "");
    }

    protected JestfulClientAbstractGenerator(String prefix, String suffix) {
        this(new SimpleModeler(), prefix, suffix);
    }

    protected JestfulClientAbstractGenerator(Modeler modeler, String prefix, String suffix) {
        super(modeler);
        if (prefix == null || suffix == null) throw new NullPointerException();
        this.prefix = prefix.trim();
        this.suffix = suffix.trim();
    }

    @Override
    public void generate(Generation generation) throws IOException {
        super.generate(generation);
        Document document = generation.getDocument();
        String directory = generation.getDirectory();
        String pkg = generation.getPkg();
        boolean pkgForced = generation.isPkgForced();
        Provider provider = generation.getProvider();
        Set<Controller> controllers = document.getControllers();

        generate(directory, pkg, pkgForced, provider, controllers);
    }

    protected void generate(String directory, String pkgGenerated, boolean pkgForced, Provider provider, Set<Controller> controllers) throws IOException {
        String comment = "Generated By Httpdoc";
        for (Controller controller : controllers) {
            String name = controller.getName();
            ClassFragment interfase = new ClassFragment();
            String pkgTranslated = controller.getPkg();
            String pkg = pkgForced || pkgTranslated == null ? pkgGenerated : pkgTranslated;
            interfase.setPkg(pkg);
            interfase.setCommentFragment(new CommentFragment(controller.getDescription() != null ? controller.getDescription() + "\n" + comment : comment));
            interfase.setClazz(new HDClass(HDClass.Category.INTERFACE, pkg + "." + name));

            HDAnnotation http = new HDAnnotation(HTTP.class);
            http.getProperties().put("value", HDAnnotationConstant.valuesOf(controller.getPath() != null ? controller.getPath() : ""));
            interfase.getAnnotations().add(http);

            List<Operation> operations = controller.getOperations();
            if (operations != null) generate(pkgGenerated, pkgForced, provider, interfase, operations);

            String className = ((pkg == null || pkg.isEmpty() ? "" : pkg + ".") + name);
            String path = directory + File.separator + className.replace(".", File.separator) + ".java";
            FileAppender appender = new FileAppender(path);
            interfase.joinTo(appender, Preference.DEFAULT);
            appender.close();
        }
    }

    protected void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, List<Operation> operations) {
        for (Operation operation : operations) generate(pkg, pkgForced, provider, interfase, operation);
    }

    protected abstract void generate(String pkg, boolean pkgForced, Provider provider, ClassFragment interfase, Operation operation);

    protected String name(String name) {
        if (prefix.isEmpty()) return name + suffix;
        else return prefix + name.substring(0, 1).toUpperCase() + name.substring(1) + suffix;
    }

    protected void describe(Operation operation, MethodFragment method, List<Parameter> parameters, Result result) {
        StringBuilder description = new StringBuilder();
        description.append(operation.getDescription() != null ? operation.getDescription() : "");
        description.append('\n');
        for (int i = 0; parameters != null && i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            if (parameter.getDescription() == null) continue;
            ParameterFragment fragment = method.getParameterFragments().get(i);
            description.append('\n').append("@param ").append(fragment.getName()).append(" ").append(parameter.getDescription());
        }
        if (result != null && result.getDescription() != null) {
            description.append('\n').append("@return ").append(result.getDescription());
        }
        method.setCommentFragment(new CommentFragment(description.toString()));
    }

    protected void generate(String pkg, boolean pkgForced, Provider provider, MethodFragment method, List<Parameter> parameters) {
        for (int i = 0; parameters != null && i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            ParameterFragment parameter = new ParameterFragment();
            String name = StringKit.isBlank(param.getName()) ? param.getType().toName() : param.getName();
            loop:
            while (true) {
                for (ParameterFragment fragment : method.getParameterFragments()) {
                    if (name.equals(fragment.getName())) {
                        name = String.format("_%s", name);
                        continue loop;
                    }
                }
                break;
            }
            parameter.setName(name);
            annotate(param, parameter);
            parameter.setType(param.getType().toType(pkg, pkgForced, provider));
            method.getParameterFragments().add(parameter);
        }
    }

    protected void annotate(Parameter parameter, ParameterFragment fragment) {
        String name = parameter.getName();
        switch (parameter.getScope()) {
            case HTTP_PARAM_SCOPE_HEADER: {
                HDAnnotation header = new HDAnnotation(Header.class);
                if (name != null) header.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(header);
                break;
            }
            case HTTP_PARAM_SCOPE_PATH: {
                HDAnnotation path = new HDAnnotation(Path.class);
                if (name != null) path.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(path);
                break;
            }
            case HTTP_PARAM_SCOPE_QUERY: {
                HDAnnotation query = new HDAnnotation(Query.class);
                if (name != null) query.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(query);
                break;
            }
            case HTTP_PARAM_SCOPE_BODY: {
                HDAnnotation body = new HDAnnotation(Body.class);
                if (name != null) body.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(body);
                break;
            }
            case HTTP_PARAM_SCOPE_COOKIE: {
                HDAnnotation cookie = new HDAnnotation(Cookie.class);
                if (name != null) cookie.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(cookie);
                break;
            }
            case HTTP_PARAM_SCOPE_FIELD: {
                HDAnnotation cookie = new HDAnnotation(Query.class);
                if (name != null) cookie.getProperties().put("value", HDAnnotationConstant.valuesOf(name.isEmpty() ? "*" : name));
                fragment.getAnnotations().add(cookie);
                break;
            }
        }
    }

    protected void annotate(Operation operation, MethodFragment fragment) {
        switch (operation.getMethod()) {
            case "HEAD": {
                HDAnnotation get = new HDAnnotation(HEAD.class);
                if (operation.getPath() != null) get.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) get.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                fragment.getAnnotations().add(get);
                break;
            }
            case "OPTIONS": {
                HDAnnotation get = new HDAnnotation(OPTIONS.class);
                if (operation.getPath() != null) get.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) get.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                fragment.getAnnotations().add(get);
                break;
            }
            case "GET": {
                HDAnnotation get = new HDAnnotation(GET.class);
                if (operation.getPath() != null) get.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) get.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                fragment.getAnnotations().add(get);
                break;
            }
            case "POST": {
                HDAnnotation post = new HDAnnotation(POST.class);
                if (operation.getPath() != null) post.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) post.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                if (operation.getConsumes() != null) post.getProperties().put("consumes", HDAnnotationConstant.valuesOf(operation.getConsumes().toArray(new Object[0])));
                fragment.getAnnotations().add(post);
                break;
            }
            case "PUT": {
                HDAnnotation put = new HDAnnotation(PUT.class);
                if (operation.getPath() != null) put.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) put.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                if (operation.getConsumes() != null) put.getProperties().put("consumes", HDAnnotationConstant.valuesOf(operation.getConsumes().toArray(new Object[0])));
                fragment.getAnnotations().add(put);
                break;
            }
            case "DELETE": {
                HDAnnotation get = new HDAnnotation(DELETE.class);
                if (operation.getPath() != null) get.getProperties().put("value", HDAnnotationConstant.valuesOf(operation.getPath()));
                if (operation.getProduces() != null) get.getProperties().put("produces", HDAnnotationConstant.valuesOf(operation.getProduces().toArray(new Object[0])));
                fragment.getAnnotations().add(get);
                break;
            }
        }
    }

}
