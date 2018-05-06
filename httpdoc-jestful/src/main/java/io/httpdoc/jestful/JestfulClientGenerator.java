package io.httpdoc.jestful;

import io.httpdoc.core.*;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.annotation.HDAnnotationConstant;
import io.httpdoc.core.appender.FileAppender;
import io.httpdoc.core.fragment.*;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import org.qfox.jestful.core.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.httpdoc.core.Parameter.*;

/**
 * Jestful Client 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 15:59
 **/
public class JestfulClientGenerator implements Generator {

    @Override
    public void generate(Generation generation) throws IOException {
        Document document = generation.getDocument();
        String directory = generation.getDirectory();
        String pkg = generation.getPkg();
        Provider provider = generation.getProvider();

        Map<String, Schema> schemas = document.getSchemas();
        if (schemas != null) generate(directory, pkg, provider, schemas);

        List<Controller> controllers = document.getControllers();
        if (controllers != null) generate(directory, pkg, provider, controllers);
    }

    private void generate(String directory, String pkg, Provider provider, List<Controller> controllers) throws IOException {
        for (Controller controller : controllers) {
            String name = controller.getName();
            FileAppender appender = new FileAppender(directory + "/apis/" + name + ".java");
            ClassFragment interfase = new ClassFragment();
            interfase.setPkg(pkg);
            interfase.setCommentFragment(new CommentFragment(controller.getDescription()));
            interfase.setClazz(new HDClass(HDClass.Category.INTERFACE, pkg + "." + name));

            HDAnnotation http = new HDAnnotation(HTTP.class);
            http.getProperties().put("value", HDAnnotationConstant.valuesOf(controller.getPath()));
            interfase.getAnnotations().add(http);

            List<Operation> operations = controller.getOperations();
            if (operations != null) generate(provider, interfase, operations);

            interfase.joinTo(appender, new DefaultPreference());
            appender.close();
        }
    }

    private void generate(Provider provider, ClassFragment interfase, List<Operation> operations) {
        for (Operation operation : operations) {
            MethodFragment method = new MethodFragment();
            annotate(operation, method);
            method.setName(operation.getName());
            List<Parameter> parameters = operation.getParameters();
            if (parameters != null) generate(provider, method, parameters);
            interfase.getMethodFragments().add(method);
        }
    }

    private void generate(Provider provider, MethodFragment method, List<Parameter> parameters) {
        for (Parameter param : parameters) {
            ParameterFragment parameter = new ParameterFragment();
            parameter.setName(param.getName());
            annotate(param, parameter);
            parameter.setType(param.getType().toType(provider));
            method.getParameterFragments().add(parameter);
        }
    }

    private void annotate(Parameter parameter, ParameterFragment fragment) {
        switch (parameter.getScope()) {
            case HTTP_PARAM_SCOPE_HEADER: {
                HDAnnotation header = new HDAnnotation(Header.class);
                if (parameter.getName() != null) header.getProperties().put("value", HDAnnotationConstant.valuesOf(parameter.getName()));
                fragment.getAnnotations().add(header);
                break;
            }
            case HTTP_PARAM_SCOPE_PATH: {
                HDAnnotation path = new HDAnnotation(Path.class);
                if (parameter.getName() != null) path.getProperties().put("value", HDAnnotationConstant.valuesOf(parameter.getName()));
                fragment.getAnnotations().add(path);
                break;
            }
            case HTTP_PARAM_SCOPE_QUERY: {
                HDAnnotation query = new HDAnnotation(Query.class);
                if (parameter.getName() != null) query.getProperties().put("value", HDAnnotationConstant.valuesOf(parameter.getName()));
                fragment.getAnnotations().add(query);
                break;
            }
            case HTTP_PARAM_SCOPE_BODY: {
                HDAnnotation body = new HDAnnotation(Body.class);
                if (parameter.getName() != null) body.getProperties().put("value", HDAnnotationConstant.valuesOf(parameter.getName()));
                fragment.getAnnotations().add(body);
                break;
            }
            case HTTP_PARAM_SCOPE_COOKIE: {
                HDAnnotation cookie = new HDAnnotation(Cookie.class);
                if (parameter.getName() != null) cookie.getProperties().put("value", HDAnnotationConstant.valuesOf(parameter.getName()));
                fragment.getAnnotations().add(cookie);
                break;
            }
        }
    }

    private void annotate(Operation operation, MethodFragment fragment) {
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

    private void generate(String directory, String pkg, Provider provider, Map<String, Schema> schemas) throws IOException {
        for (Schema schema : schemas.values()) {
            String name = schema.getName();
            FileAppender appender = new FileAppender(directory + "/models/" + name + ".java");
            ClassFragment clazz = new ClassFragment();
            clazz.setPkg(pkg);
            clazz.setCommentFragment(new CommentFragment(schema.getDescription()));
            clazz.setClazz(new HDClass(pkg + "." + name));
            clazz.setSuperclass(schema.getSuperclass() != null ? new HDClass(pkg + "." + schema.getSuperclass().getName()) : null);
            Map<String, Property> properties = schema.getProperties();
            for (Map.Entry<String, Property> entry : properties.entrySet()) {
                Property property = entry.getValue();
                HDType type = property.getType().toType(provider);
                FieldFragment field = new FieldFragment();
                field.setName(entry.getKey());
                field.setType(type);
                field.setCommentFragment(new CommentFragment(property.getDescription()));
                clazz.getFieldFragments().add(field);

                GetterMethodFragment getter = new GetterMethodFragment(type, entry.getKey());
                clazz.getMethodFragments().add(getter);

                SetterMethodFragment setter = new SetterMethodFragment(type, entry.getKey());
                clazz.getMethodFragments().add(setter);
            }
            clazz.joinTo(appender, new DefaultPreference());
            appender.close();
        }
    }

}
