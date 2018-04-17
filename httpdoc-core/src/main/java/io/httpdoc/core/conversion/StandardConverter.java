package io.httpdoc.core.conversion;

import io.httpdoc.core.*;
import io.httpdoc.core.exception.UndefinedSchemaException;

import java.util.*;

/**
 * 抽象的文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:40
 **/
public class StandardConverter implements Converter {
    private static final String REFERENCE_PREFIX = "@/schemas/";
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

    protected List<Map<String, Object>> doConvertControllers(List<Controller> controllers) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Controller controller : controllers) list.add(doConvertController(controller));
        return list;
    }

    protected Map<String, Object> doConvertController(Controller controller) {
        return null;
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
                    Schema s = property.getSchema();
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
                map.put("properties", m);
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
                return DICTIONARY_PREFIX + doConvertReference(schema.getComponent()) + DICTIONARY_SUFFIX;
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
        doConvertSchemas(document, dictionary);
        doConvertControllers(document, dictionary);
        return document;
    }

    protected void doConvertSchemas(Document document, Map<String, Object> dictionary) {
        Object schemas = dictionary.get("schemas");
        if (schemas == null) return;
        Map<?, ?> map = (Map<?, ?>) schemas;
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
            definition.assemble(document);
        }
    }

    protected void doConvertControllers(Document document, Map<String, Object> dictionary) {

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

        void assemble(Document document) {
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
                        property.setSchema(doConvertReference(document, reference));
                    } else if (value instanceof Map<?, ?>) {
                        Map<?, ?> m = (Map<?, ?>) value;
                        String type = (String) m.get("type");
                        String description = (String) m.get("description");
                        property.setSchema(doConvertReference(document, type));
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
    }

}
