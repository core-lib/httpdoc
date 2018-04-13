package io.httpdoc.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 资源模型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:42
 **/
@JsonSerialize(using = Schema.SchemaReferenceSerializer.class)
@JsonDeserialize(using = Schema.SchemaReferenceDeserializer.class)
public class Schema {
    public static final Map<Type, Schema> PROVIDED = new HashMap<>();
    public static final String PREFIX = "#/schemas/";

    static {
        PROVIDED.put(boolean.class, new Schema("boolean"));
        PROVIDED.put(byte.class, new Schema("byte"));
        PROVIDED.put(short.class, new Schema("short"));
        PROVIDED.put(char.class, new Schema("char"));
        PROVIDED.put(int.class, new Schema("int"));
        PROVIDED.put(float.class, new Schema("float"));
        PROVIDED.put(long.class, new Schema("long"));
        PROVIDED.put(double.class, new Schema("double"));

        PROVIDED.put(Boolean.class, new Schema("Boolean"));
        PROVIDED.put(Byte.class, new Schema("Byte"));
        PROVIDED.put(Short.class, new Schema("Short"));
        PROVIDED.put(Character.class, new Schema("Character"));
        PROVIDED.put(Integer.class, new Schema("Integer"));
        PROVIDED.put(Float.class, new Schema("Float"));
        PROVIDED.put(Long.class, new Schema("Long"));
        PROVIDED.put(Double.class, new Schema("Double"));

        PROVIDED.put(String.class, new Schema("String"));
        PROVIDED.put(Object.class, new Schema("Object"));
        PROVIDED.put(Date.class, new Schema("Date"));
        PROVIDED.put(Enum.class, new Schema("Enum"));
    }

    public static final String MAP = "Map";
    public static final String ENUM = "Enum";
    public static final String ARRAY = "Array";
    public static final String OBJECT = "Object";

    private boolean provided;
    private String name;
    private Schema parent;
    private Map<String, Property> properties;
    private String comment;
    private Schema component;
    private Constant[] constants;
    private final Collection<Schema> dependencies;
    private final Map<String, Object> definitions;

    public Schema() {
        this.dependencies = null;
        this.definitions = null;
    }

    private Schema(String name) {
        this.name = name;
        this.provided = true;
        this.dependencies = null;
        this.definitions = null;
    }

    public Schema(Map<String, Object> definitions) {
        this.dependencies = null;
        this.definitions = definitions;
    }

    private Schema(Type type, Map<Type, Schema> cache) throws Exception {
        try {
            cache.put(type, this);
            if (type instanceof Class<?>) {
                Class<?> clazz = (Class<?>) type;
                if (clazz.isArray()) {
                    name = ARRAY;
                    component = Schema.valueOf(clazz.getComponentType(), cache);
                    provided = true;
                    cache.remove(type);
                } else if (Collection.class.isAssignableFrom(clazz)) {
                    name = ARRAY;
                    component = Schema.valueOf(Object.class, cache);
                    provided = true;
                    cache.remove(type);
                } else if (Map.class.isAssignableFrom(clazz)) {
                    name = MAP;
                    component = Schema.valueOf(Object.class, cache);
                    provided = true;
                    cache.remove(type);
                } else if (clazz.isEnum()) {
                    name = clazz.getSimpleName();
                    parent = Schema.valueOf(clazz.getSuperclass(), cache);
                    Object[] enumerations = clazz.getEnumConstants();
                    constants = new Constant[enumerations.length];
                    for (int i = 0; i < enumerations.length; i++) {
                        String constant = enumerations[i].toString();
                        String comment = DocKit.forConstant(clazz, constant);
                        constants[i] = new Constant(constant, comment);
                    }
                    comment = DocKit.forClass(clazz);
                } else {
                    name = clazz.getSimpleName();
                    parent = Schema.valueOf(clazz.getSuperclass(), cache);
                    PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                    properties = new LinkedHashMap<>();
                    for (PropertyDescriptor pd : descriptors) {
                        String field = pd.getName();
                        if (field.equals("class")) continue;
                        Method getter = pd.getReadMethod();
                        if (getter.getDeclaringClass() != clazz) continue;
                        Type rtype = getter.getGenericReturnType();
                        Property property = new Property(Schema.valueOf(rtype, cache), DocKit.forField(clazz, field));
                        properties.put(field, property);
                    }
                    comment = DocKit.forClass(clazz);
                }
            } else if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                Type rtype = ptype.getRawType();
                if (rtype instanceof Class<?>) {
                    Class<?> clazz = (Class<?>) rtype;
                    if (Collection.class.isAssignableFrom(clazz)) {
                        Type atype = ptype.getActualTypeArguments()[0];
                        name = ARRAY;
                        component = Schema.valueOf(atype, cache);
                        provided = true;
                        cache.remove(type);
                    } else if (Map.class.isAssignableFrom(clazz)) {
                        Type atype = ptype.getActualTypeArguments()[1];
                        name = MAP;
                        component = Schema.valueOf(atype, cache);
                        provided = true;
                        cache.remove(type);
                    } else {
                        throw new IllegalArgumentException("unsupported type : " + rtype);
                    }
                } else {
                    throw new IllegalArgumentException("unsupported type : " + type);
                }
            } else {
                throw new IllegalArgumentException("unsupported type : " + type);
            }
            dependencies = new HashSet<>(cache.values());
            definitions = null;
        } catch (Exception e) {
            cache.remove(type);
            throw e;
        }
    }

    public static Schema valueOf(Type type) throws Exception {
        Map<Type, Schema> map = new HashMap<>();
        return valueOf(type, map);
    }

    private static Schema valueOf(Type type, Map<Type, Schema> cache) throws Exception {
        Schema provided = PROVIDED.get(type);
        if (provided != null) return provided;
        Schema schema = cache.get(type);
        return schema != null ? schema : new Schema(type, cache);
    }

    public boolean isProvided() {
        return provided;
    }

    public void setProvided(boolean provided) {
        this.provided = provided;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema getParent() {
        return parent;
    }

    public void setParent(Schema parent) {
        this.parent = parent;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Schema getComponent() {
        return component;
    }

    public Constant[] getConstants() {
        return constants;
    }

    public void setConstants(Constant[] constants) {
        this.constants = constants;
    }

    public void setComponent(Schema component) {
        this.component = component;
    }

    public Collection<Schema> getDependencies() {
        return dependencies;
    }

    public Map<String, Object> getDefinitions() {
        return definitions;
    }

    @Override
    public String toString() {
        if (!provided) return PREFIX + name;
        switch (name) {
            case ARRAY:
                return component.toString() + "[]";
            case MAP:
                return MAP + "<String, " + component.toString() + ">";
            default:
                return name;
        }
    }

    public static class SchemaDefinitionSerializer extends JsonSerializer<Map<String, Schema>> {

        @Override
        public void serialize(Map<String, Schema> schemas, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
                String name = entry.getKey();
                Schema schema = entry.getValue();
                gen.writeObjectFieldStart(name);
                if (schema.parent != null) gen.writeObjectField("superclass", schema.parent);
                if (schema.constants != null) {
                    gen.writeObjectFieldStart("constants");
                    for (Constant constant : schema.constants) gen.writeStringField(constant.getName(), constant.getComment());
                    gen.writeEndObject();
                }
                if (schema.properties != null) {
                    gen.writeObjectFieldStart("properties");
                    for (Map.Entry<String, Property> e : schema.properties.entrySet()) {
                        String field = e.getKey();
                        String clazz = e.getValue().toString();
                        String comment = e.getValue().getComment();
                        if (comment == null || comment.trim().equals("")) {
                            gen.writeStringField(field, clazz);
                        } else {
                            gen.writeObjectFieldStart(field);
                            gen.writeStringField("type", clazz);
                            gen.writeStringField("comment", comment);
                            gen.writeEndObject();
                        }
                    }
                    gen.writeEndObject();
                }
                if (schema.comment != null) gen.writeStringField("comment", schema.comment);
                gen.writeEndObject();
            }
            gen.writeEndObject();
        }

    }

    public static class SchemaDefinitionDeserializer extends JsonDeserializer<Map<String, Schema>> {

        @Override
        public Map<String, Schema> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Map<String, Schema> schemas = new LinkedHashMap<>();
            Map<String, Map<String, Object>> map = jp.readValueAs(new TypeReference<Map<String, Map<String, Object>>>() {
            });
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) schemas.put(entry.getKey(), new Schema(entry.getValue()));
            assemble(schemas);
            return schemas;
        }

        private void assemble(Map<String, Schema> schemas) {
            for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
                Schema schema = entry.getValue();
                schema.name = entry.getKey();
                Map<String, Object> definitions = schema.definitions;
                assert definitions != null;
                for (Map.Entry<String, Object> definition : definitions.entrySet()) {
                    Object value = definition.getValue();
                    switch (definition.getKey()) {
                        case "superclass":
                            String superclass = (String) value;
                            if (superclass.startsWith(PREFIX)) schema.parent = schemas.get(superclass.substring(PREFIX.length()));
                            else schema.parent = new Schema(superclass);
                            break;
                        case "constants":
                            if (value.getClass().isArray()) {
                                int length = Array.getLength(value);
                                schema.constants = new Constant[length];
                                for (int i = 0; i < length; i++) schema.constants[i] = new Constant(Array.get(value, i).toString());
                            } else if (value instanceof Collection<?>) {
                                Collection<?> collection = (Collection<?>) value;
                                schema.constants = new Constant[collection.size()];
                                int index = 0;
                                for (Object constant : collection) schema.constants[index++] = new Constant(constant.toString());
                            } else if (value instanceof Map<?, ?>) {
                                Map<?, ?> map = (Map<?, ?>) value;
                                schema.constants = new Constant[map.size()];
                                int index = 0;
                                for (Map.Entry<?, ?> constant : map.entrySet()) schema.constants[index++] = new Constant(constant.getKey().toString(), constant.getValue().toString());
                            }
                            break;
                        case "properties":
                            Map<?, ?> properties = (Map<?, ?>) value;
                            schema.properties = new LinkedHashMap<>(properties.size());
                            for (Map.Entry<?, ?> property : properties.entrySet()) {
                                String name = property.getKey().toString();
                                Object clazz = property.getValue();
                                if (clazz instanceof String) {
                                    String expression = (String) clazz;
                                    schema.properties.put(name, new Property(convert(schemas, expression)));
                                } else {
                                    Map<?, ?> map = (Map<?, ?>) clazz;
                                    Object type = map.get("type");
                                    Object comment = map.get("comment");
                                    String expression = (String) type;
                                    schema.properties.put(name, new Property(convert(schemas, expression), (String) comment));
                                }
                            }
                            break;
                        case "comment":
                            schema.comment = value.toString();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        private Schema convert(Map<String, Schema> schemas, String expression) {
            expression = expression.replace(" ", "");
            Schema schema;
            if (expression.startsWith(PREFIX)) {
                String name = expression.replace("[]", "").substring(PREFIX.length());
                schema = schemas.get(name);
            } else if (expression.startsWith(MAP)) {
                String component = expression.substring((MAP + "<String,").length(), expression.length() - 1);
                schema = new Schema(MAP);
                schema.component = convert(schemas, component);
            } else {
                String name = expression.replace("[]", "");
                schema = new Schema(name);
            }
            while (expression.endsWith("[]")) {
                Schema array = new Schema(ARRAY);
                array.component = schema;
                schema = array;
                expression = expression.substring(0, expression.length() - 2);
            }
            return schema;
        }

    }

    public static class SchemaReferenceSerializer extends JsonSerializer<Schema> {

        @Override
        public void serialize(Schema schema, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(schema.toString());
        }

    }

    public static class SchemaReferenceDeserializer extends JsonDeserializer<Schema> {

        @Override
        public Schema deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }

    }

}
