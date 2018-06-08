package io.httpdoc.core;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.exception.SchemaUnsupportedException;
import io.httpdoc.core.interpretation.*;
import io.httpdoc.core.provider.Provider;
import io.httpdoc.core.provider.SystemProvider;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDParameterizedType;
import io.httpdoc.core.type.HDType;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
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
                    this.superclass = Schema.valueOf(clazz.getSuperclass() != null ? clazz.getSuperclass() : Object.class, cache, provider, interpreter);
                    PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                    for (PropertyDescriptor descriptor : descriptors) {
                        String field = descriptor.getName();
                        if (field.equals("class")) continue;
                        Method getter = descriptor.getReadMethod();
                        if (getter == null || getter.getDeclaringClass() != clazz) continue;
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
                        this.category = Category.OBJECT;
                        this.name = clazz.getSimpleName();
                        this.superclass = Schema.valueOf(clazz.getSuperclass() != null ? clazz.getSuperclass() : Object.class, cache, provider, interpreter);
                        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                        for (PropertyDescriptor descriptor : descriptors) {
                            String field = descriptor.getName();
                            if (field.equals("class")) continue;
                            Method getter = descriptor.getReadMethod();
                            if (getter == null || getter.getDeclaringClass() != clazz) continue;
                            Type t = getter.getGenericReturnType();
                            Schema schema = Schema.valueOf(t, cache, provider, interpreter);
                            Interpretation interpretation = interpreter.interpret(descriptor);
                            String description = interpretation != null ? interpretation.getContent() : null;
                            Property property = new Property(schema, description);
                            this.properties.put(field, property);
                        }
                        ClassInterpretation interpretation = interpreter.interpret(clazz);
                        this.description = interpretation != null ? interpretation.getContent() : null;
                        cache.remove(type);
                        cache.put(clazz, this);
                    }
                } else {
                    throw new SchemaUnsupportedException(type);
                }
            } else if (type instanceof GenericArrayType) {
                GenericArrayType genericArrayType = (GenericArrayType) type;
                Type genericComponentType = genericArrayType.getGenericComponentType();
                this.category = Category.ARRAY;
                this.component = Schema.valueOf(genericComponentType, cache, provider, interpreter);
                cache.remove(type);
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
        return valueOf(type, new SystemProvider(), interpreter);
    }

    public static Schema valueOf(Type type, Provider provider) {
        return valueOf(type, provider, new DefaultInterpreter());
    }

    public static Schema valueOf(Type type, Provider provider, Interpreter interpreter) {
        return valueOf(type, new HashMap<Type, Schema>(), provider, interpreter);
    }

    private static Schema valueOf(Type type, Map<Type, Schema> cache, Provider provider, Interpreter interpreter) {
        while (type instanceof TypeVariable<?> || type instanceof WildcardType) {
            if (type instanceof TypeVariable<?>) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) type;
                type = typeVariable.getBounds() != null && typeVariable.getBounds().length > 0 ? typeVariable.getBounds()[0] : Object.class;
            }
            if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                type = wildcardType.getUpperBounds() != null && wildcardType.getUpperBounds().length > 0 ? wildcardType.getUpperBounds()[0] : Object.class;
            }
        }
        return cache.containsKey(type)
                ? cache.get(type)
                : provider.contains(type)
                ? provider.acquire(type)
                : new Schema(type, cache, provider, interpreter);
    }

    public boolean isPart() {
        switch (category) {
            case BASIC:
                return "File".equals(name);
            case DICTIONARY:
                return component.isPart();
            case ARRAY:
                return component.isPart();
            case ENUM:
                return false;
            case OBJECT:
                return false;
            default:
                return false;
        }
    }

    public boolean isVoid() {
        return category == Category.BASIC && "void".equals(name);
    }

    public String toName() {
        switch (category) {
            case BASIC:
                return name.toLowerCase();
            case DICTIONARY:
                return "map";
            case ARRAY:
                return component.getCategory() != Category.ARRAY ? component.toName() + "s" : component.toName();
            case ENUM: {
                int index = 0;
                for (int i = 0; i < name.length() && name.charAt(i) >= 'A' && name.charAt(i) <= 'Z'; i++) index++;
                return name.substring(0, index > 1 ? index - 1 : index).toLowerCase() + name.substring(index > 1 ? index - 1 : index);
            }
            case OBJECT: {
                int index = 0;
                for (int i = 0; i < name.length() && name.charAt(i) >= 'A' && name.charAt(i) <= 'Z'; i++) index++;
                return name.substring(0, index > 1 ? index - 1 : index).toLowerCase() + name.substring(index > 1 ? index - 1 : index);
            }
            default:
                return null;
        }
    }

    public HDType toType(String pkg, Provider provider) {
        switch (category) {
            case BASIC:
                return HDType.valueOf(provider.acquire(this));
            case DICTIONARY:
                HDClass rawType = new HDClass(Map.class);
                HDType[] actualTypeArguments = new HDType[]{new HDClass(String.class), component.toType(pkg, provider)};
                return new HDParameterizedType(rawType, null, actualTypeArguments);
            case ARRAY:
                HDType componentType = component.toType(pkg, provider);
                if (!(componentType instanceof HDClass)) {
                    System.out.println(componentType);
                }
                return new HDClass(componentType);
            case ENUM:
                return new HDClass(HDClass.Category.ENUM, pkg + "." + name);
            case OBJECT:
                return new HDClass(pkg + "." + name);
            default:
                throw new IllegalStateException();
        }
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

    @Override
    public String toString() {
        return "Schema{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
