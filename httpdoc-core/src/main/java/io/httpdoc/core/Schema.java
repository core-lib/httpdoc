package io.httpdoc.core;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.exception.SchemaUnsupportedException;
import io.httpdoc.core.interpretation.*;
import io.httpdoc.core.provider.DefaultProvider;
import io.httpdoc.core.provider.Provider;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
public class Schema extends Definition {
    private static final long serialVersionUID = 9146240988324413872L;

    private Category category;
    private String name;
    private Schema superclass;
    private Map<String, Property> properties = new LinkedHashMap<>();
    private Schema component;
    private Set<Constant> constants = new LinkedHashSet<>();
    private Collection<Schema> dependencies = new ArrayList<>();

    public Schema() {
    }

    private Schema(Type type, Map<Type, Schema> cache, Provider provider, Interpreter interpreter) {
        try {
            cache.put(type, this);
            if (type instanceof Class<?>) {
                Class<?> clazz = (Class<?>) type;
                if (clazz.isArray()) {
                    this.category = Category.ARRAY;
                    this.component = Schema.valueOf(clazz.getComponentType(), cache, provider, interpreter);
                    cache.remove(type);
                } else if (Collection.class.isAssignableFrom(clazz)) {
                    this.category = Category.ARRAY;
                    this.component = Schema.valueOf(Object.class, cache, provider, interpreter);
                    cache.remove(type);
                } else if (Map.class.isAssignableFrom(clazz)) {
                    this.category = Category.DICTIONARY;
                    this.component = Schema.valueOf(Object.class, cache, provider, interpreter);
                    cache.remove(type);
                } else if (clazz.isEnum()) {
                    Class<? extends Enum> enumClass = clazz.asSubclass(Enum.class);
                    this.category = Category.ENUM;
                    this.name = clazz.getSimpleName();
                    Enum<?>[] enumerations = enumClass.getEnumConstants();
                    for (Enum<?> enumeration : enumerations) {
                        EnumInterpretation interpretation = interpreter.interpret(enumeration);
                        String description = interpretation != null ? interpretation.getContent() : null;
                        Constant constant = new Constant(enumeration.name(), description);
                        this.constants.add(constant);
                    }
                    ClassInterpretation interpretation = interpreter.interpret(clazz);
                    this.description = interpretation != null ? interpretation.getContent() : null;
                } else {
                    this.category = Category.OBJECT;
                    this.name = clazz.getSimpleName();
                    this.superclass = Schema.valueOf(clazz.getSuperclass(), cache, provider, interpreter);
                    PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                    for (PropertyDescriptor descriptor : descriptors) {
                        String field = descriptor.getName();
                        if (field.equals("class")) continue;
                        Method getter = descriptor.getReadMethod();
                        if (getter.getDeclaringClass() != clazz) continue;
                        Type t = getter.getGenericReturnType();
                        Schema schema = Schema.valueOf(t, cache, provider, interpreter);
                        Interpretation interpretation = interpreter.interpret(descriptor);
                        String description = interpretation != null ? interpretation.getContent() : null;
                        Property property = new Property(schema, description);
                        this.properties.put(field, property);
                    }
                    ClassInterpretation interpretation = interpreter.interpret(clazz);
                    this.description = interpretation != null ? interpretation.getContent() : null;
                }
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class<?>) {
                    Class<?> clazz = (Class<?>) rawType;
                    if (Collection.class.isAssignableFrom(clazz)) {
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                        this.category = Category.ARRAY;
                        this.component = Schema.valueOf(actualTypeArgument, cache, provider, interpreter);
                        cache.remove(type);
                    } else if (Map.class.isAssignableFrom(clazz)) {
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[1];
                        this.category = Category.DICTIONARY;
                        this.component = Schema.valueOf(actualTypeArgument, cache, provider, interpreter);
                        cache.remove(type);
                    } else {
                        throw new SchemaUnsupportedException(rawType);
                    }
                } else {
                    throw new SchemaUnsupportedException(type);
                }
            } else {
                throw new SchemaUnsupportedException(type);
            }
            this.dependencies = new HashSet<>(cache.values());
        } catch (Exception e) {
            cache.remove(type);
            throw new HttpdocRuntimeException(e);
        }
    }

    public static Schema valueOf(Type type) {
        return valueOf(type, new DefaultInterpreter());
    }

    public static Schema valueOf(Type type, Interpreter interpreter) {
        return valueOf(type, new DefaultProvider(), interpreter);
    }

    public static Schema valueOf(Type type, Provider provider) {
        return valueOf(type, provider, new DefaultInterpreter());
    }

    public static Schema valueOf(Type type, Provider provider, Interpreter interpreter) {
        return valueOf(type, new HashMap<Type, Schema>(), provider, interpreter);
    }

    private static Schema valueOf(Type type, Map<Type, Schema> cache, Provider provider, Interpreter interpreter) {
        return cache.containsKey(type)
                ? cache.get(type)
                : provider.contains(type)
                ? provider.acquire(type)
                : new Schema(type, cache, provider, interpreter);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema getSuperclass() {
        return superclass;
    }

    public void setSuperclass(Schema superclass) {
        this.superclass = superclass;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public Schema getComponent() {
        return component;
    }

    public Set<Constant> getConstants() {
        return constants;
    }

    public void setConstants(Set<Constant> constants) {
        this.constants = constants;
    }

    public void setComponent(Schema component) {
        this.component = component;
    }

    public Collection<Schema> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Collection<Schema> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schema schema = (Schema) o;

        return category == schema.category && (name != null ? name.equals(schema.name) : schema.name == null);
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
