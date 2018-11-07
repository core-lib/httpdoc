package io.httpdoc.core.conversion;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.SchemaUnrecognizedException;
import io.httpdoc.core.kit.StringKit;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 抽象的文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:40
 **/
public class StandardConverter implements Converter {

    @Override
    public Map<String, Object> convert(Document document) {
        return convert(document, new DefaultFormat());
    }

    @Override
    public Map<String, Object> convert(Document document, Format format) {
        for (Controller controller : document.getControllers()) {
            for (Operation operation : controller.getOperations()) {
                for (Parameter parameter : operation.getParameters()) {
                    Schema type = parameter.getType();
                    Collection<Schema> dependencies = type.getDependencies();
                    for (Schema schema : dependencies) document.getSchemas().put((format.isPkgIncluded() ? schema.getPkg() + "." : "") + schema.getName(), schema);
                }
                Schema type = operation.getResult().getType();
                Collection<Schema> dependencies = type.getDependencies();
                for (Schema schema : dependencies) document.getSchemas().put((format.isPkgIncluded() ? schema.getPkg() + "." : "") + schema.getName(), schema);
            }
        }
        // 排序
        {
            List<Controller> controllers = new ArrayList<>(document.getControllers());
            for (Controller controller : controllers) Collections.sort(controller.getOperations());
            Collections.sort(controllers);
            document.setControllers(new LinkedHashSet<>(controllers));

            Map<String, Schema> schemas = document.getSchemas();
            List<Map.Entry<String, Schema>> entries = new ArrayList<>(schemas.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, Schema>>() {
                @Override
                public int compare(Map.Entry<String, Schema> a, Map.Entry<String, Schema> b) {
                    int c = Integer.compare(a.getValue().getOrder(), b.getValue().getOrder());
                    if (c != 0) return c;
                    else return a.getKey().compareTo(b.getKey());
                }
            });
            Map<String, Schema> map = new LinkedHashMap<>();
            for (Map.Entry<String, Schema> entry : entries) map.put(entry.getKey(), entry.getValue());
            document.setSchemas(map);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        if (document.getHttpdoc() != null) map.put("httpdoc", document.getHttpdoc());
        if (document.getProtocol() != null) map.put("protocol", document.getProtocol());
        if (document.getHostname() != null) map.put("hostname", document.getHostname());
        if (document.getPort() != null) map.put("port", document.getPort());
        if (document.getContext() != null) map.put("context", document.getContext());
        if (document.getVersion() != null) map.put("version", document.getVersion());
        if (document.getDateFormat() != null) map.put("dateFormat", document.getDateFormat());
        if (document.getDescription() != null) map.put("description", document.getDescription());
        if (!Format.REF_PREFIX.equals(format.getRefPrefix())) map.put("refPrefix", format.getRefPrefix());
        if (!Format.REF_SUFFIX.equals(format.getRefSuffix())) map.put("refSuffix", format.getRefSuffix());
        if (!Format.MAP_PREFIX.equals(format.getMapPrefix())) map.put("mapPrefix", format.getMapPrefix());
        if (!Format.MAP_SUFFIX.equals(format.getMapSuffix())) map.put("mapSuffix", format.getMapSuffix());
        if (!Format.ARR_PREFIX.equals(format.getArrPrefix())) map.put("arrPrefix", format.getArrPrefix());
        if (!Format.ARR_SUFFIX.equals(format.getArrSuffix())) map.put("arrSuffix", format.getArrSuffix());
        if (document.getControllers() != null) map.put("controllers", doConvertControllers(document.getControllers(), format));
        if (document.getSchemas() != null) map.put("schemas", doConvertSchemas(document.getSchemas(), format));
        return map;
    }

    private Object doConvertControllers(Set<Controller> controllers, Format format) {
        if (!format.isCanonical() && controllers.size() == 1) {
            return doConvertController(controllers.iterator().next(), format);
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Controller controller : controllers) list.add(doConvertController(controller, format));
            return list;
        }
    }

    private Map<String, Object> doConvertController(Controller controller, Format format) {
        Map<String, Object> map = new LinkedHashMap<>();

        String name = controller.getName();
        if (name != null) map.put("name", name);

        String pkg = controller.getPkg();
        if (pkg != null && format.isPkgIncluded()) map.put("pkg", pkg);

        String path = controller.getPath();
        if (path != null) map.put("path", path);

        List<String> produces = controller.getProduces();
        if (produces != null && !produces.isEmpty()) map.put("produces", doConvertProduces(produces, format));

        List<String> consumes = controller.getConsumes();
        if (consumes != null && !consumes.isEmpty()) map.put("consumes", doConvertConsumes(consumes, format));

        List<Operation> operations = controller.getOperations();
        if (operations != null && !operations.isEmpty()) map.put("operations", doConvertOperations(operations, format));

        List<String> tags = controller.getTags();
        if (tags != null && !tags.isEmpty()) map.put("tags", doConvertTags(tags, format));

        String summary = controller.getSummary();
        if (!StringKit.isBlank(summary)) map.put("summary", summary);

        String deprecated = controller.getDeprecated();
        if (!StringKit.isBlank(deprecated)) map.put("deprecated", deprecated);

        String description = controller.getDescription();
        if (!StringKit.isBlank(description)) map.put("description", description);

        return map;
    }

    private Object doConvertStrings(List<String> produces) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < produces.size(); i++) {
            if (i > 0) builder.append(", ");
            builder.append(produces.get(i));
        }
        return builder.toString();
    }

    private Object doConvertProduces(List<String> produces, Format format) {
        return format.isCanonical() ? produces : doConvertStrings(produces);
    }

    private Object doConvertConsumes(List<String> consumes, Format format) {
        return format.isCanonical() ? consumes : doConvertStrings(consumes);
    }

    private Object doConvertOperations(List<Operation> operations, Format format) {
        if (!format.isCanonical() && operations.size() == 1) {
            return doConvertOperation(operations.get(0), format);
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Operation operation : operations) list.add(doConvertOperation(operation, format));
            return list;
        }
    }

    private Map<String, Object> doConvertOperation(Operation operation, Format format) {
        Map<String, Object> map = new LinkedHashMap<>();

        String name = operation.getName();
        if (name != null) map.put("name", name);

        String path = operation.getPath();
        if (path != null) map.put("path", path);

        String method = operation.getMethod();
        if (method != null) map.put("method", method);

        boolean multipart = operation.isMultipart();
        if (multipart) map.put("multipart", true);

        List<String> produces = operation.getProduces();
        if (produces != null && !produces.isEmpty()) map.put("produces", doConvertProduces(produces, format));

        List<String> consumes = operation.getConsumes();
        if (consumes != null && !consumes.isEmpty()) map.put("consumes", doConvertConsumes(consumes, format));

        List<Parameter> parameters = operation.getParameters();
        if (parameters != null && !parameters.isEmpty()) map.put("parameters", doConvertParameters(parameters, format));

        Result result = operation.getResult();
        if (result != null) map.put("result", doConvertResult(result, format));

        List<String> tags = operation.getTags();
        if (tags != null && !tags.isEmpty()) map.put("tags", doConvertTags(tags, format));

        String summary = operation.getSummary();
        if (!StringKit.isBlank(summary)) map.put("summary", summary);

        String deprecated = operation.getDeprecated();
        if (!StringKit.isBlank(deprecated)) map.put("deprecated", deprecated);

        String description = operation.getDescription();
        if (!StringKit.isBlank(description)) map.put("description", description);

        return map;
    }

    private Object doConvertTags(List<String> tags, Format format) {
        return format.isCanonical() ? tags : doConvertStrings(tags);
    }

    private Object doConvertParameters(List<Parameter> parameters, Format format) {
        if (!format.isCanonical() && parameters.size() == 1) {
            return doConvertParameter(parameters.get(0), format);
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Parameter parameter : parameters) list.add(doConvertParameter(parameter, format));
            return list;
        }
    }

    private Map<String, Object> doConvertParameter(Parameter parameter, Format format) {
        Map<String, Object> map = new LinkedHashMap<>();
        String name = parameter.getName();
        if (name != null) map.put("name", name);

        String alias = parameter.getAlias();
        if (!StringKit.isBlank(alias) && !alias.equals(name)) map.put("alias", alias);

        String scope = parameter.getScope();
        if (scope != null) map.put("scope", scope);

        String path = parameter.getPath();
        if (path != null) map.put("path", path);

        Schema type = parameter.getType();
        if (type != null) map.put("type", doConvertReference(type, format));

        String description = parameter.getDescription();
        if (!StringKit.isBlank(description)) map.put("description", description);

        return map;
    }

    private Object doConvertResult(Result result, Format format) {
        Schema type = result.getType();
        if (type == null) return null;

        String reference = doConvertReference(type, format);
        String description = result.getDescription();
        if (format.isCanonical() || !StringKit.isBlank(description)) {
            Map<String, String> map = new LinkedHashMap<>();
            if (reference != null) map.put("type", reference);
            if (!StringKit.isBlank(description)) map.put("description", description);
            return map;
        }

        return reference;
    }

    private Map<String, Map<String, Object>> doConvertSchemas(Map<String, Schema> schemas, Format format) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
            String name = entry.getKey();
            Schema schema = entry.getValue();
            Map<String, Object> m = doConvertSchema(schema, format);
            if (m == null) continue;
            map.put(name, m);
        }
        return map;
    }

    private Map<String, Object> doConvertSchema(Schema schema, Format format) {
        Category category = schema.getCategory();
        Map<String, Object> map = new LinkedHashMap<>();
        String pkg = schema.getPkg();
        if (pkg != null && format.isPkgIncluded()) map.put("pkg", pkg);
        switch (category) {
            case BASIC:
                return null;
            case DICTIONARY:
                return null;
            case ARRAY:
                return null;
            case ENUM:
                Set<Constant> constants = schema.getConstants();
                boolean commented = false;
                for (Constant constant : constants) commented = commented || !StringKit.isBlank(constant.getDescription());
                if (format.isCanonical() || commented) {
                    Map<String, String> m = new LinkedHashMap<>();
                    for (Constant constant : constants) m.put(constant.getName(), constant.getDescription());
                    map.put("constants", m);
                } else {
                    List<String> names = new ArrayList<>();
                    for (Constant constant : constants) names.add(constant.getName());
                    map.put("constants", names);
                }
                break;
            case OBJECT:
                Schema superclass = schema.getSuperclass();
                if (superclass != null) map.put("superclass", doConvertReference(superclass, format));
                Map<String, Property> properties = schema.getProperties();
                Map<String, Object> m = new LinkedHashMap<>();
                for (Map.Entry<String, Property> entry : properties.entrySet()) {
                    String name = entry.getKey();
                    Property property = entry.getValue();
                    String alias = property.getAlias();
                    Schema type = property.getType();
                    String description = property.getDescription();
                    String reference = doConvertReference(type, format);
                    if (format.isCanonical() || !StringKit.isBlank(description) || (!StringKit.isBlank(alias) && !alias.equals(name))) {
                        Map<String, Object> p = new LinkedHashMap<>();
                        if (!StringKit.isBlank(alias) && !alias.equals(name)) p.put("alias", alias);
                        if (reference != null) p.put("type", reference);
                        if (!StringKit.isBlank(description)) p.put("description", description);
                        m.put(name, p);
                    } else {
                        m.put(name, reference);
                    }
                }
                if (!m.isEmpty()) map.put("properties", m);
                break;
        }
        String summary = schema.getSummary();
        if (!StringKit.isBlank(summary)) map.put("summary", summary);

        String deprecated = schema.getDeprecated();
        if (!StringKit.isBlank(deprecated)) map.put("deprecated", deprecated);

        String description = schema.getDescription();
        if (!StringKit.isBlank(description)) map.put("description", description);

        return map;
    }

    private String doConvertReference(Schema schema, Format format) {
        Category category = schema.getCategory();
        switch (category) {
            case BASIC:
                return schema.getName();
            case DICTIONARY:
                return format.getMapPrefix() + " " + doConvertReference(schema.getComponent(), format) + format.getMapSuffix();
            case ARRAY:
                return format.getArrPrefix() + doConvertReference(schema.getComponent(), format) + format.getArrSuffix();
            case ENUM:
                return format.getRefPrefix() + (format.isPkgIncluded() ? schema.getPkg() + "." : "") + schema.getName() + format.getRefSuffix();
            case OBJECT:
                return format.getRefPrefix() + (format.isPkgIncluded() ? schema.getPkg() + "." : "") + schema.getName() + format.getRefSuffix();
            default:
                return null;
        }
    }

    @Override
    public Document convert(Map<String, Object> dictionary) {
        Document document = new Document();
        document.setHttpdoc((String) dictionary.get("httpdoc"));
        document.setProtocol((String) dictionary.get("protocol"));
        document.setHostname((String) dictionary.get("hostname"));
        document.setPort(dictionary.containsKey("port") ? Integer.valueOf(dictionary.get("port").toString()) : null);
        document.setContext((String) dictionary.get("context"));
        document.setVersion((String) dictionary.get("version"));
        document.setDateFormat((String) dictionary.get("dateFormat"));
        document.setDescription((String) dictionary.get("description"));

        String refPrefix = (String) dictionary.get("refPrefix");
        if (refPrefix != null) document.setRefPrefix(refPrefix);
        String refSuffix = (String) dictionary.get("refSuffix");
        if (refSuffix != null) document.setRefSuffix(refSuffix);

        String mapPrefix = (String) dictionary.get("mapPrefix");
        if (mapPrefix != null) document.setMapPrefix(mapPrefix);
        String mapSuffix = (String) dictionary.get("mapSuffix");
        if (mapSuffix != null) document.setMapSuffix(mapSuffix);

        String arrPrefix = (String) dictionary.get("arrPrefix");
        if (arrPrefix != null) document.setRefPrefix(arrPrefix);
        String arrSuffix = (String) dictionary.get("arrSuffix");
        if (arrSuffix != null) document.setRefSuffix(arrSuffix);

        doConvertSchemas(document, dictionary.get("schemas"));
        doConvertControllers(document, dictionary.get("controllers"));
        return document;
    }

    private void doConvertSchemas(Document document, Object object) {
        if (object == null) return;
        Map<?, ?> map = (Map<?, ?>) object;
        Map<String, SchemaDefinition> definitions = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            Map<?, ?> definition = (Map<?, ?>) entry.getValue();
            Schema schema = new Schema();
            String[] names = key.split("\\.");
            schema.setName(names[names.length - 1]);
            document.getSchemas().put(key, schema);
            definitions.put(key, new SchemaDefinition(schema, definition));
        }
        for (Map.Entry<String, SchemaDefinition> entry : definitions.entrySet()) {
            SchemaDefinition definition = entry.getValue();
            doConvertSchema(document, definition);
        }
    }

    private void doConvertSchema(Document document, SchemaDefinition schemaDefinition) {
        Schema schema = schemaDefinition.schema;
        Map<?, ?> definition = schemaDefinition.definition;
        schema.setCategory(Category.OBJECT);

        String pkg = (String) definition.get("pkg");
        schema.setPkg(pkg);
        String superclass = (String) definition.get("superclass");
        if (superclass != null) schema.setSuperclass(doConvertReference(document, superclass));

        Object properties = definition.get("properties");
        if (properties == null) {
            schema.setProperties(null);
        } else if (properties instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) properties;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String name = (String) entry.getKey();
                Object value = entry.getValue();
                Property property = new Property();
                if (value instanceof String) {
                    String reference = (String) value;
                    property.setType(doConvertReference(document, reference));
                } else if (value instanceof Map<?, ?>) {
                    Map<?, ?> m = (Map<?, ?>) value;
                    String alias = (String) m.get("alias");
                    String type = (String) m.get("type");
                    String description = (String) m.get("description");
                    property.setAlias(!StringKit.isBlank(alias) ? alias : name);
                    property.setType(doConvertReference(document, type));
                    property.setDescription(description);
                } else {
                    continue;
                }
                schema.getProperties().put(name, property);
            }
        } else {
            schema.setProperties(null);
        }

        Object constants = definition.get("constants");
        if (constants == null) {
            schema.setConstants(null);
        } else if (Map.class.isInstance(constants)) {
            schema.setCategory(Category.ENUM);
            Map<?, ?> map = (Map<?, ?>) constants;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String name = (String) entry.getKey();
                String description = (String) entry.getValue();
                schema.getConstants().add(new Constant(name, description));
            }
        } else if (Collection.class.isInstance(constants)) {
            schema.setCategory(Category.ENUM);
            Collection<?> collection = (Collection<?>) constants;
            for (Object element : collection) {
                String name = (String) element;
                schema.getConstants().add(new Constant(name));
            }
        } else if (constants.getClass().isArray()) {
            schema.setCategory(Category.ENUM);
            Object[] array = (Object[]) constants;
            for (Object element : array) {
                String name = (String) element;
                schema.getConstants().add(new Constant(name));
            }
        } else {
            schema.setProperties(null);
        }

        String summary = (String) definition.get("summary");
        schema.setSummary(summary);

        String deprecated = (String) definition.get("deprecated");
        schema.setDeprecated(deprecated);

        String description = (String) definition.get("description");
        schema.setDescription(description);
    }

    private void doConvertControllers(Document document, Object object) {
        if (object == null) {
            document.setControllers(null);
        } else if (object instanceof Collection<?>) {
            Collection<?> controllers = (Collection<?>) object;
            for (Object controller : controllers) {
                Map<?, ?> map = (Map<?, ?>) controller;
                doConvertController(document, map);
            }
        } else if (object instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) object;
            doConvertController(document, map);
        } else {
            document.setControllers(null);
        }
    }

    private void doConvertController(Document document, Map<?, ?> map) {
        Controller controller = new Controller();

        controller.setPkg((String) map.get("pkg"));
        controller.setName((String) map.get("name"));
        controller.setPath((String) map.get("path"));

        Object produces = map.get("produces");
        controller.setProduces(doConvertProduces(document, produces));

        Object consumes = map.get("consumes");
        controller.setConsumes(doConvertConsumes(document, consumes));

        Object operations = map.get("operations");
        controller.setOperations(doConvertOperations(document, operations));

        Object tags = map.get("tags");
        controller.setTags(doConvertTags(document, tags));

        controller.setSummary((String) map.get("summary"));
        controller.setDeprecated((String) map.get("deprecated"));
        controller.setDescription((String) map.get("description"));

        document.getControllers().add(controller);
    }

    private List<String> doConvertStrings(Object object) {
        List<String> list = new ArrayList<>();
        if (object == null) {
            list = null;
        } else if (object instanceof String) {
            String string = (String) object;
            string = string.trim();
            if (string.startsWith("[") && string.endsWith("]")) string = string.substring(1, string.length() - 1);
            else if (string.startsWith("{") && string.endsWith("}")) string = string.substring(1, string.length() - 1);
            String[] produces = string.split("\\s*,\\s*");
            for (String produce : produces) if (!produce.equals("")) list.add(produce);
        } else if (object instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) object;
            for (Object produce : collection) list.add((String) produce);
        } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) list.add((String) Array.get(object, i));
        } else {
            list = null;
        }
        return list;
    }

    private List<String> doConvertProduces(Document document, Object object) {
        return document != null ? doConvertStrings(object) : null;
    }

    private List<String> doConvertConsumes(Document document, Object object) {
        return document != null ? doConvertStrings(object) : null;
    }

    private List<Operation> doConvertOperations(Document document, Object object) {
        List<Operation> list = new ArrayList<>();

        if (object == null) {
            list = null;
        } else if (object instanceof Collection<?>) {
            Collection<?> controllers = (Collection<?>) object;
            for (Object controller : controllers) {
                Map<?, ?> map = (Map<?, ?>) controller;
                Operation operation = doConvertOperation(document, map);
                list.add(operation);
            }
        } else if (object instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) object;
            Operation operation = doConvertOperation(document, map);
            list.add(operation);
        } else {
            list = null;
        }

        return list;
    }

    private Operation doConvertOperation(Document document, Map<?, ?> map) {
        Operation operation = new Operation();

        operation.setName((String) map.get("name"));
        operation.setPath((String) map.get("path"));
        operation.setMethod((String) map.get("method"));
        operation.setMultipart(map.containsKey("multipart") && (Boolean) map.get("multipart"));

        Object produces = map.get("produces");
        operation.setProduces(doConvertProduces(document, produces));

        Object consumes = map.get("consumes");
        operation.setConsumes(doConvertConsumes(document, consumes));

        Object parameters = map.get("parameters");
        operation.setParameters(doConvertParameters(document, parameters));

        Object result = map.get("result");
        operation.setResult(doConvertResult(document, result));

        Object tags = map.get("tags");
        operation.setTags(doConvertTags(document, tags));

        operation.setSummary((String) map.get("summary"));
        operation.setDeprecated((String) map.get("deprecated"));
        operation.setDescription((String) map.get("description"));

        return operation;
    }

    private List<String> doConvertTags(Document document, Object object) {
        return document != null ? doConvertStrings(object) : null;
    }

    private List<Parameter> doConvertParameters(Document document, Object object) {
        List<Parameter> list = new ArrayList<>();

        if (object == null) {
            list = null;
        } else if (object instanceof Collection<?>) {
            Collection<?> controllers = (Collection<?>) object;
            for (Object controller : controllers) {
                Map<?, ?> map = (Map<?, ?>) controller;
                Parameter parameter = doConvertParameter(document, map);
                list.add(parameter);
            }
        } else if (object instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) object;
            Parameter parameter = doConvertParameter(document, map);
            list.add(parameter);
        } else {
            list = null;
        }

        return list;
    }

    private Parameter doConvertParameter(Document document, Map<?, ?> map) {
        Parameter parameter = new Parameter();

        String name = (String) map.get("name");
        String alias = (String) map.get("alias");
        String scope = (String) map.get("scope");
        String path = (String) map.get("path");
        parameter.setName(name);
        parameter.setAlias(!StringKit.isBlank(alias) ? alias : name);
        parameter.setScope(scope);
        parameter.setPath(path);

        String reference = (String) map.get("type");
        parameter.setType(doConvertReference(document, reference));

        String description = (String) map.get("description");
        parameter.setDescription(description);

        return parameter;
    }

    private Result doConvertResult(Document document, Object object) {
        Result result = new Result();
        if (object == null) {
            result = null;
        } else if (object instanceof String) {
            String reference = (String) object;
            Schema type = doConvertReference(document, reference);
            result.setType(type);
        } else if (object instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) object;
            String reference = (String) map.get("type");
            Schema type = doConvertReference(document, reference);
            result.setType(type);
            result.setDescription((String) map.get("description"));
        } else {
            result = null;
        }
        return result;
    }

    private Schema doConvertReference(Document document, String reference) {
        Schema schema;
        Map<String, Schema> schemas = document.getSchemas();
        reference = reference.replace(" ", "");
        int dimension = 0;
        while (reference.startsWith(document.getArrPrefix()) && reference.endsWith(document.getArrSuffix())) {
            reference = reference.substring(document.getArrPrefix().length(), reference.length() - document.getArrSuffix().length());
            dimension++;
        }
        if (reference.startsWith(document.getRefPrefix()) && reference.endsWith(document.getRefSuffix())) {
            String name = reference.substring(document.getRefPrefix().length(), reference.length() - document.getRefSuffix().length());
            schema = schemas.get(name);
            if (schema == null) throw new SchemaUnrecognizedException(name);
        } else if (reference.startsWith(document.getMapPrefix()) && reference.endsWith(document.getMapSuffix())) {
            reference = reference.substring(document.getMapPrefix().length(), reference.length() - document.getMapSuffix().length());
            schema = new Schema();
            schema.setCategory(Category.DICTIONARY);
            schema.setComponent(doConvertReference(document, reference));
        } else {
            schema = new Schema();
            schema.setCategory(Category.BASIC);
            schema.setName(reference);
        }
        for (int i = 0; i < dimension; i++) {
            Schema array = new Schema();
            array.setCategory(Category.ARRAY);
            array.setComponent(schema);
            schema = array;
        }
        return schema;
    }

    private class SchemaDefinition {
        private final Schema schema;
        private final Map<?, ?> definition;

        SchemaDefinition(Schema schema, Map<?, ?> definition) {
            this.schema = schema;
            this.definition = definition;
        }

    }

}
