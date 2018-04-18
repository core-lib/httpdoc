package io.httpdoc.core.conversion;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.UndefinedSchemaException;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 抽象的文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:40
 **/
public class StandardConverter implements Converter {
    private static final String REFERENCE_PREFIX = "$/schemas/";
    private static final String REFERENCE_SUFFIX = "";
    private static final String DICTIONARY_PREFIX = "Dictionary<String,";
    private static final String DICTIONARY_SUFFIX = ">";
    private static final String ARRAY_PREFIX = "";
    private static final String ARRAY_SUFFIX = "[]";

    @Override
    public Map<String, Object> convert(Document document) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("httpdoc", document.getHttpdoc());
        map.put("protocol", document.getProtocol());
        map.put("hostname", document.getHostname());
        map.put("ctxtpath", document.getCtxtpath());
        map.put("version", document.getVersion());
        map.put("controllers", doConvertControllers(document.getControllers()));
        map.put("schemas", doConvertSchemas(document.getSchemas()));
        return map;
    }

    protected Object doConvertControllers(List<Controller> controllers) {
        if (controllers.size() == 1) {
            return doConvertController(controllers.get(0));
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Controller controller : controllers) list.add(doConvertController(controller));
            return list;
        }
    }

    protected Map<String, Object> doConvertController(Controller controller) {
        Map<String, Object> map = new LinkedHashMap<>();

        String name = controller.getName();
        if (name != null) map.put("name", name);

        String path = controller.getPath();
        if (path != null) map.put("path", path);

        List<String> produces = controller.getProduces();
        if (produces != null && !produces.isEmpty()) map.put("produces", doConvertProduces(produces));

        List<String> consumes = controller.getConsumes();
        if (consumes != null && !consumes.isEmpty()) map.put("consumes", doConvertConsumes(consumes));

        List<Operation> operations = controller.getOperations();
        if (operations != null && !operations.isEmpty()) map.put("operations", doConvertOperations(operations));

        String description = controller.getDescription();
        if (description != null) map.put("description", description);

        return map;
    }

    protected Object doConvertProduces(List<String> produces) {
        if (produces.size() == 1) return produces.get(0);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < produces.size(); i++) {
            if (i > 0) builder.append(", ");
            builder.append(produces.get(i));
        }
        return builder.toString();
    }

    protected Object doConvertConsumes(List<String> consumes) {
        return doConvertProduces(consumes);
    }

    protected Object doConvertOperations(List<Operation> operations) {
        if (operations.size() == 1) {
            return doConvertOperation(operations.get(0));
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Operation operation : operations) list.add(doConvertOperation(operation));
            return list;
        }
    }

    protected Map<String, Object> doConvertOperation(Operation operation) {
        Map<String, Object> map = new LinkedHashMap<>();

        String name = operation.getName();
        if (name != null) map.put("name", name);

        String path = operation.getPath();
        if (path != null) map.put("path", path);

        String method = operation.getMethod();
        if (method != null) map.put("method", method);

        List<String> produces = operation.getProduces();
        if (produces != null && !produces.isEmpty()) map.put("produces", doConvertProduces(produces));

        List<String> consumes = operation.getConsumes();
        if (consumes != null && !consumes.isEmpty()) map.put("consumes", doConvertConsumes(consumes));

        List<Parameter> parameters = operation.getParameters();
        if (parameters != null && !parameters.isEmpty()) map.put("parameters", doConvertParameters(parameters));

        Result result = operation.getResult();
        if (result != null) map.put("result", doConvertResult(result));

        String description = operation.getDescription();
        if (description != null) map.put("description", description);

        return map;
    }

    protected Object doConvertParameters(List<Parameter> parameters) {
        if (parameters.size() == 1) {
            return doConvertParameter(parameters.get(0));
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Parameter parameter : parameters) list.add(doConvertParameter(parameter));
            return list;
        }
    }

    protected Map<String, Object> doConvertParameter(Parameter parameter) {
        Map<String, Object> map = new LinkedHashMap<>();
        String name = parameter.getName();
        if (name != null) map.put("name", name);

        String scope = parameter.getScope();
        if (scope != null) map.put("scope", scope);

        Schema type = parameter.getType();
        if (type != null) map.put("type", doConvertReference(type));

        String description = parameter.getDescription();
        if (description != null) map.put("description", description);

        return map;
    }

    protected Object doConvertResult(Result result) {
        Schema type = result.getType();
        if (type == null) return null;

        String description = result.getDescription();
        if (description != null) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("type", doConvertReference(type));
            map.put("description", description);
            return map;
        }

        return doConvertReference(type);
    }

    protected Map<String, Map<String, Object>> doConvertSchemas(Map<String, Schema> schemas) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
            String name = entry.getKey();
            Schema schema = entry.getValue();
            Map<String, Object> m = doConvertSchema(schema);
            if (m == null) continue;
            map.put(name, m);
        }
        return map;
    }

    protected Map<String, Object> doConvertSchema(Schema schema) {
        Category category = schema.getCategory();
        Map<String, Object> map = new LinkedHashMap<>();
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
                for (Constant constant : constants) commented = commented || constant.getDescription() != null;
                if (commented) {
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
                if (superclass != null) map.put("superclass", doConvertReference(superclass));
                Map<String, Property> properties = schema.getProperties();
                Map<String, Object> m = new LinkedHashMap<>();
                for (Map.Entry<String, Property> entry : properties.entrySet()) {
                    String name = entry.getKey();
                    Property property = entry.getValue();
                    Schema s = property.getType();
                    String description = property.getDescription();
                    String reference = doConvertReference(s);
                    if (description != null) {
                        Map<String, Object> p = new LinkedHashMap<>();
                        p.put("type", reference);
                        p.put("description", description);
                        m.put("name", p);
                    } else {
                        m.put(name, reference);
                    }
                }
                if (!m.isEmpty()) map.put("properties", m);
                break;
        }
        String description = schema.getDescription();
        if (description != null) map.put("description", description);
        return map;
    }

    protected String doConvertReference(Schema schema) {
        Category category = schema.getCategory();
        switch (category) {
            case BASIC:
                return schema.getName();
            case DICTIONARY:
                return DICTIONARY_PREFIX + " " + doConvertReference(schema.getComponent()) + DICTIONARY_SUFFIX;
            case ARRAY:
                return ARRAY_PREFIX + doConvertReference(schema.getComponent()) + ARRAY_SUFFIX;
            case ENUM:
                return REFERENCE_PREFIX + schema.getName() + REFERENCE_SUFFIX;
            case OBJECT:
                return REFERENCE_PREFIX + schema.getName() + REFERENCE_SUFFIX;
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
        document.setCtxtpath((String) dictionary.get("ctxtpath"));
        document.setVersion((String) dictionary.get("version"));
        doConvertSchemas(document, dictionary.get("schemas"));
        doConvertControllers(document, dictionary.get("controllers"));
        return document;
    }

    protected void doConvertSchemas(Document document, Object object) {
        if (object == null) return;
        Map<?, ?> map = (Map<?, ?>) object;
        Map<String, SchemaDefinition> definitions = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String name = (String) entry.getKey();
            Map<?, ?> definition = (Map<?, ?>) entry.getValue();
            Schema schema = new Schema();
            schema.setName(name);
            document.getSchemas().put(name, schema);
            definitions.put(name, new SchemaDefinition(schema, definition));
        }
        for (Map.Entry<String, SchemaDefinition> entry : definitions.entrySet()) {
            SchemaDefinition definition = entry.getValue();
            doConvertSchema(document, definition);
        }
    }

    protected void doConvertSchema(Document document, SchemaDefinition schemaDefinition) {
        Schema schema = schemaDefinition.schema;
        Map<?, ?> definition = schemaDefinition.definition;
        schema.setCategory(Category.OBJECT);

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
                    String type = (String) m.get("type");
                    String description = (String) m.get("description");
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

        String description = (String) definition.get("description");
        schema.setDescription(description);
    }

    protected void doConvertControllers(Document document, Object object) {
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

    protected void doConvertController(Document document, Map<?, ?> map) {
        Controller controller = new Controller();

        controller.setName((String) map.get("name"));
        controller.setPath((String) map.get("path"));

        Object produces = map.get("produces");
        controller.setProduces(doConvertProduces(document, produces));

        Object consumes = map.get("consumes");
        controller.setConsumes(doConvertConsumes(document, consumes));

        Object operations = map.get("operations");
        controller.setOperations(doConvertOperations(document, operations));

        controller.setDescription((String) map.get("description"));

        document.getControllers().add(controller);
    }

    protected List<String> doConvertProduces(Document document, Object object) {
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

    protected List<String> doConvertConsumes(Document document, Object object) {
        return doConvertProduces(document, object);
    }

    protected List<Operation> doConvertOperations(Document document, Object object) {
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

    protected Operation doConvertOperation(Document document, Map<?, ?> map) {
        Operation operation = new Operation();

        operation.setName((String) map.get("name"));
        operation.setPath((String) map.get("path"));
        operation.setMethod((String) map.get("method"));

        Object produces = map.get("produces");
        operation.setProduces(doConvertProduces(document, produces));

        Object consumes = map.get("consumes");
        operation.setConsumes(doConvertConsumes(document, consumes));

        Object parameters = map.get("parameters");
        operation.setParameters(doConvertParameters(document, parameters));

        Object result = map.get("result");
        operation.setResult(doConvertResult(document, result));

        operation.setDescription((String) map.get("description"));

        return operation;
    }

    protected List<Parameter> doConvertParameters(Document document, Object object) {
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

    protected Parameter doConvertParameter(Document document, Map<?, ?> map) {
        Parameter parameter = new Parameter();

        parameter.setName((String) map.get("name"));
        parameter.setScope((String) map.get("scope"));

        String reference = (String) map.get("type");
        parameter.setType(doConvertReference(document, reference));

        parameter.setDescription((String) map.get("description"));

        return parameter;
    }

    protected Result doConvertResult(Document document, Object object) {
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

    protected Schema doConvertReference(Document document, String reference) {
        Schema schema;
        Map<String, Schema> schemas = document.getSchemas();
        reference = reference.replace(" ", "");
        int dimension = 0;
        while (reference.startsWith(ARRAY_PREFIX) && reference.endsWith(ARRAY_SUFFIX)) {
            reference = reference.substring(ARRAY_PREFIX.length(), reference.length() - ARRAY_SUFFIX.length());
            dimension++;
        }
        if (reference.startsWith(REFERENCE_PREFIX) && reference.endsWith(REFERENCE_SUFFIX)) {
            String name = reference.substring(REFERENCE_PREFIX.length(), reference.length() - REFERENCE_SUFFIX.length());
            schema = schemas.get(name);
            if (schema == null) throw new UndefinedSchemaException(name);
        } else if (reference.startsWith(DICTIONARY_PREFIX) && reference.endsWith(DICTIONARY_SUFFIX)) {
            reference = reference.substring(DICTIONARY_PREFIX.length(), reference.length() - DICTIONARY_SUFFIX.length());
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
