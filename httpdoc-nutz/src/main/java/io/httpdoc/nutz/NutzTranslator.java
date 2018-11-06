package io.httpdoc.nutz;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.interpretation.ClassInterpretation;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.MethodInterpretation;
import io.httpdoc.core.interpretation.Note;
import io.httpdoc.core.kit.ReflectionKit;
import io.httpdoc.core.reflection.ParameterizedTypeImpl;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.nutz.lang.util.MethodParamNamesScaner;
import org.nutz.mvc.*;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.injector.*;
import org.nutz.mvc.impl.ActionInvoker;
import org.nutz.mvc.impl.processor.AdaptorProcessor;
import org.nutz.mvc.upload.injector.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Nutz Httpdoc 翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 12:27
 **/
public class NutzTranslator implements Translator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());
        document.setDateFormat(translation.getDateFormat());
        document.setDescription(translation.getDescription());

        Container container = translation.getContainer();
        Supplier supplier = translation.getSupplier();
        Interpreter interpreter = translation.getInterpreter();
        NutMvcContext context = (NutMvcContext) container.get("__nutz__mvc__ctx");
        if (context == null) return document;
        NutConfig config = context.getDefaultNutConfig();
        UrlMapping mapping = config.getUrlMapping();

        Map<Class<?>, Controller> controllers = new LinkedHashMap<>();
        try {
            Map<String, ActionInvoker> map = ReflectionKit.getFieldValue(mapping, "map");
            for (Map.Entry<String, ActionInvoker> entry : map.entrySet()) {
                Method method = config.getAtMap().getMethodMapping().get(entry.getKey());
                Class<?> clazz = method.getDeclaringClass();
                Controller controller = controllers.get(clazz);
                if (controller == null) {
                    controller = new Controller();
                    controller.setPkg(clazz.getPackage().getName());
                    controller.setName(clazz.getSimpleName());
                    controller.setPath("");
                    ClassInterpretation interpretation = interpreter.interpret(clazz);
                    controller.setSummary(interpretation != null ? interpretation.getSummary() : null);
                    controller.setDescription(interpretation != null ? interpretation.getContent() : null);
                    controllers.put(clazz, controller);
                }

                Operation operation = new Operation();
                operation.setName(method.getName());
                ActionInvoker invoker = entry.getValue();

                String METHOD;
                ActionChain chain;
                Map<String, ActionChain> chains = ReflectionKit.getFieldValue(invoker, "chainMap");
                if (chains.isEmpty()) {
                    METHOD = "POST";
                    chain = ReflectionKit.getFieldValue(invoker, "defaultChain");
                } else {
                    Map.Entry<String, ActionChain> first = chains.entrySet().iterator().next();
                    METHOD = first.getKey();
                    chain = first.getValue();
                }
                operation.setMethod(METHOD);

                Map<String, String> m = new HashMap<>();
                MethodInterpretation interpretation = interpreter.interpret(method);
                Note[] notes = interpretation != null ? interpretation.getParamNotes() : null;
                for (int i = 0; notes != null && i < notes.length; i++) m.put(notes[i].getName(), notes[i].getText());
                List<String> names = MethodParamNamesScaner.getParamNames(method);


                Processor processor = ReflectionKit.getFieldValue(chain, "head");
                while (processor != null && !(processor instanceof AdaptorProcessor)) processor = processor.getNext();
                if (processor == null) continue;

                String path = entry.getKey();
                HttpAdaptor adaptor = ReflectionKit.getFieldValue(processor, "adaptor");
                ParamInjector[] injectors = ReflectionKit.getFieldValue(adaptor, "injs");
                for (int i = 0; injectors != null && i < injectors.length && i < method.getParameterTypes().length; i++) {
                    ParamInjector injector = injectors[i];
                    Parameter parameter = new Parameter();
                    parameter.setType(Schema.valueOf(method.getParameterTypes()[i], supplier, interpreter));

                    if (path.contains("?")) {
                        String name = names.get(i);
                        parameter.setName(name);
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_PATH);
                        path = path.replaceFirst("\\?", "{" + name + "}");
                    } else if (injector instanceof CookieInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        if ("_map".equals(name)) continue;
                        parameter.setName(name);
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_COOKIE);
                    } else if (injector instanceof ReqHeaderInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        if ("_map".equals(name)) continue;
                        parameter.setName(name);
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_HEADER);
                    } else if (injector instanceof NameInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        if ("?".equals(name)) continue;
                        parameter.setName(name);
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_QUERY);
                    } else if (injector instanceof JsonInjector || injector instanceof XmlInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        parameter.setName(name);
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    } else if (injector instanceof FileInjector
                            || injector instanceof FileMetaInjector
                            || injector instanceof TempFileInjector
                            || injector instanceof InputStreamInjector
                            || injector instanceof ReaderInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        parameter.setName(name);
                        parameter.setType(Schema.valueOf(File.class));
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    } else if (injector instanceof TempFileArrayInjector) {
                        String name = ReflectionKit.getFieldValue(injector, "name");
                        parameter.setName(name);
                        parameter.setType(Schema.valueOf(File[].class));
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    } else if (injector instanceof MapSelfInjector) {
                        String name = names.get(i);
                        parameter.setName(name);
                        parameter.setType(Schema.valueOf(new ParameterizedTypeImpl(Map.class, null, String.class, File.class)));
                        parameter.setScope(Parameter.HTTP_PARAM_SCOPE_BODY);
                    } else {
                        continue;
                    }
                    String key = names.get(i);
                    String description = m.get(key);
                    parameter.setDescription(description);
                    operation.getParameters().add(parameter);
                }
                operation.setPath(path);

                Result result = new Result();
                Schema type = Schema.valueOf(method.getGenericReturnType(), supplier, interpreter);
                result.setType(type);
                result.setDescription(interpretation != null && interpretation.getReturnNote() != null ? interpretation.getReturnNote().getText() : null);
                operation.setResult(result);

                operation.setSummary(interpretation != null ? interpretation.getSummary() : null);
                operation.setDescription(interpretation != null ? interpretation.getContent() : null);
                controller.getOperations().add(operation);
            }
        } catch (Exception e) {
            logger.warn("fail to translate nutz mappings", e);
            return document;
        }
        document.setControllers(new LinkedHashSet<>(controllers.values()));
        return document;
    }

    @Override
    public String normalize(String path) {
        Pattern pattern = Pattern.compile("[?]");
        Matcher matcher = pattern.matcher(path);
        int i = 0;
        while (matcher.find()) path = path.replaceFirst("[?]", "{" + (i++) + "}");
        return path;
    }

}
