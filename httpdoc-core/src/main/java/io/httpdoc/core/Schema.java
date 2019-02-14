package io.httpdoc.core;

import io.httpdoc.core.annotation.*;
import io.httpdoc.core.annotation.Package;
import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.exception.SchemaUnsupportedException;
import io.httpdoc.core.interpretation.*;
import io.httpdoc.core.kit.ReflectionKit;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
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
public class Schema extends Definition implements Ordered<Schema> {
    private static final long serialVersionUID = 9146240988324413872L;

    private Category category;
    private String pkg;
    private String name;
    private Schema superclass;
    private Map<String, Property> properties = new LinkedHashMap<>();
    private Schema component;
    private Schema owner;
    private Set<Constant> constants = new LinkedHashSet<>();
    private Collection<Schema> dependencies = new ArrayList<>();
    private String summary;
    private String deprecated;
    private int order = Integer.MAX_VALUE;
    private String style;

    public Schema() {
    }

    private Schema(Type type, Map<Type, Schema> cache, Supplier supplier, Interpreter interpreter) {
        try {
            cache.put(type, this);
            if (type instanceof Class<?>) {
                Class<?> clazz = (Class<?>) type;
                if (clazz.isArray()) {
                    this.category = Category.ARRAY;
                    this.component = Schema.valueOf(clazz.getComponentType(), cache, supplier, interpreter);
                    cache.remove(type);
                } else if (Collection.class.isAssignableFrom(clazz)) {
                    this.category = Category.ARRAY;
                    this.component = Schema.valueOf(Object.class, cache, supplier, interpreter);
                    cache.remove(type);
                } else if (Map.class.isAssignableFrom(clazz)) {
                    this.category = Category.DICTIONARY;
                    this.component = Schema.valueOf(Object.class, cache, supplier, interpreter);
                    cache.remove(type);
                } else if (clazz.isEnum()) {
                    ClassInterpretation classInterpretation = interpreter.interpret(clazz);
                    Class<? extends Enum> enumClass = clazz.asSubclass(Enum.class);
                    this.category = Category.ENUM;

                    String pkg = classInterpretation != null ? classInterpretation.getPackage() : null;
                    if (!StringKit.isBlank(pkg)) this.pkg = pkg.trim();
                    else this.pkg = clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName();

                    String name = classInterpretation != null ? classInterpretation.getName() : null;
                    if (!StringKit.isBlank(name)) this.name = name.trim();
                    else this.name = clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName();

                    this.owner = clazz.getEnclosingClass() != null ? Schema.valueOf(clazz.getEnclosingClass(), cache, supplier, interpreter) : null;
                    Enum<?>[] enumerations = enumClass.getEnumConstants();
                    for (Enum<?> enumeration : enumerations) {
                        EnumInterpretation enumInterpretation = interpreter.interpret(enumeration);
                        String description = enumInterpretation != null ? enumInterpretation.getContent() : null;
                        Constant constant = new Constant(enumeration.name(), description);
                        this.constants.add(constant);
                    }

                    this.summary = classInterpretation != null ? classInterpretation.getSummary() : null;
                    this.description = classInterpretation != null ? classInterpretation.getContent() : null;
                    this.deprecated = classInterpretation != null && classInterpretation.getDeprecated() != null
                            ? classInterpretation.getDeprecated()
                            : clazz.isAnnotationPresent(Deprecated.class)
                            ? "deprecated"
                            : null;
                    Integer order = classInterpretation != null ? classInterpretation.getOrder() : null;
                    if (order != null) this.order = order;
                    else this.order = clazz.isAnnotationPresent(Order.class) ? clazz.getAnnotation(Order.class).value() : Integer.MAX_VALUE;

                    String style = classInterpretation != null ? classInterpretation.getStyle() : null;
                    if (style != null && !style.trim().isEmpty()) this.style = style;
                    else this.style = clazz.isAnnotationPresent(Style.class) ? clazz.getAnnotation(Style.class).value() : null;
                } else {
                    ClassInterpretation classInterpretation = interpreter.interpret(clazz);

                    this.category = Category.OBJECT;

                    String pkg = classInterpretation != null ? classInterpretation.getPackage() : null;
                    if (!StringKit.isBlank(pkg)) this.pkg = pkg.trim();
                    else this.pkg = clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName();

                    String name = classInterpretation != null ? classInterpretation.getName() : null;
                    if (!StringKit.isBlank(name)) this.name = name.trim();
                    else this.name = clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName();

                    this.owner = clazz.getEnclosingClass() != null ? Schema.valueOf(clazz.getEnclosingClass(), cache, supplier, interpreter) : null;
                    this.superclass = Schema.valueOf(clazz.getSuperclass() != null ? clazz.getSuperclass() : Object.class, cache, supplier, interpreter);
                    PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                    List<String> indexes = ReflectionKit.getFieldNames(clazz);
                    for (PropertyDescriptor descriptor : descriptors) {
                        String field = descriptor.getName();
                        if (field.equals("class")) continue;
                        Method getter = descriptor.getReadMethod();
                        if (getter == null || getter.getDeclaringClass() != clazz) continue;
                        if (getter.isAnnotationPresent(Skip.class)) continue;
                        ExtendedInterpretation extendedInterpretation = (ExtendedInterpretation) interpreter.interpret(descriptor);
                        if (extendedInterpretation != null && extendedInterpretation.isSkip()) continue;
                        Type t = getter.getGenericReturnType();
                        Schema schema = Schema.valueOf(t, cache, supplier, interpreter);
                        String description = extendedInterpretation != null ? extendedInterpretation.getContent() : null;
                        Property property = new Property(schema, description);
                        String alias = extendedInterpretation != null ? extendedInterpretation.getAlias() : null;
                        if (alias != null && !alias.isEmpty()) {
                            property.setAlias(alias);
                        } else {
                            Alias annotation = getter.isAnnotationPresent(Alias.class) ? getter.getAnnotation(Alias.class) : null;
                            property.setAlias(annotation != null ? annotation.value() : field);
                        }
                        int index = indexes.indexOf(field);
                        if (index >= 0) property.setOrder(index);
                        Integer order = extendedInterpretation != null ? extendedInterpretation.getOrder() : null;
                        if (order != null) property.setOrder(order);
                        else property.setOrder(getter.isAnnotationPresent(Order.class) ? getter.getAnnotation(Order.class).value() : property.getOrder());
                        String style = extendedInterpretation != null ? extendedInterpretation.getStyle() : null;
                        if (style != null && !style.trim().isEmpty()) property.setStyle(style);
                        else property.setStyle(getter.isAnnotationPresent(Style.class) ? getter.getAnnotation(Style.class).value() : null);

                        this.properties.put(field, property);
                    }

                    this.summary = classInterpretation != null ? classInterpretation.getSummary() : null;
                    this.description = classInterpretation != null ? classInterpretation.getContent() : null;
                    this.deprecated = classInterpretation != null && classInterpretation.getDeprecated() != null
                            ? classInterpretation.getDeprecated()
                            : clazz.isAnnotationPresent(Deprecated.class)
                            ? "deprecated"
                            : null;
                    Integer order = classInterpretation != null ? classInterpretation.getOrder() : null;
                    if (order != null) this.order = order;
                    else this.order = clazz.isAnnotationPresent(Order.class) ? clazz.getAnnotation(Order.class).value() : Integer.MAX_VALUE;

                    String style = classInterpretation != null ? classInterpretation.getStyle() : null;
                    if (style != null && !style.trim().isEmpty()) this.style = style;
                    else this.style = clazz.isAnnotationPresent(Style.class) ? clazz.getAnnotation(Style.class).value() : null;
                }
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class<?>) {
                    Class<?> clazz = (Class<?>) rawType;
                    if (Collection.class.isAssignableFrom(clazz)) {
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                        this.category = Category.ARRAY;
                        this.component = Schema.valueOf(actualTypeArgument, cache, supplier, interpreter);
                        cache.remove(type);
                    } else if (Map.class.isAssignableFrom(clazz)) {
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[1];
                        this.category = Category.DICTIONARY;
                        this.component = Schema.valueOf(actualTypeArgument, cache, supplier, interpreter);
                        cache.remove(type);
                    } else {
                        ClassInterpretation classInterpretation = interpreter.interpret(clazz);

                        this.category = Category.OBJECT;

                        String pkg = classInterpretation != null ? classInterpretation.getPackage() : null;
                        if (!StringKit.isBlank(pkg)) this.pkg = pkg.trim();
                        else this.pkg = clazz.isAnnotationPresent(Package.class) ? clazz.getAnnotation(Package.class).value() : clazz.getPackage().getName();

                        String name = classInterpretation != null ? classInterpretation.getName() : null;
                        if (!StringKit.isBlank(name)) this.name = name.trim();
                        else this.name = clazz.isAnnotationPresent(Name.class) ? clazz.getAnnotation(Name.class).value() : clazz.getSimpleName();

                        this.owner = clazz.getEnclosingClass() != null ? Schema.valueOf(clazz.getEnclosingClass(), cache, supplier, interpreter) : null;
                        this.superclass = Schema.valueOf(clazz.getSuperclass() != null ? clazz.getSuperclass() : Object.class, cache, supplier, interpreter);
                        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                        List<String> indexes = ReflectionKit.getFieldNames(clazz);
                        for (PropertyDescriptor descriptor : descriptors) {
                            String field = descriptor.getName();
                            if (field.equals("class")) continue;
                            Method getter = descriptor.getReadMethod();
                            if (getter == null || getter.getDeclaringClass() != clazz) continue;
                            if (getter.isAnnotationPresent(Skip.class)) continue;
                            ExtendedInterpretation extendedInterpretation = (ExtendedInterpretation) interpreter.interpret(descriptor);
                            if (extendedInterpretation != null && extendedInterpretation.isSkip()) continue;
                            Type t = getter.getGenericReturnType();
                            Schema schema = Schema.valueOf(t, cache, supplier, interpreter);
                            String description = extendedInterpretation != null ? extendedInterpretation.getContent() : null;
                            Property property = new Property(schema, description);
                            String alias = extendedInterpretation != null ? extendedInterpretation.getAlias() : null;
                            if (alias != null && !alias.isEmpty()) {
                                property.setAlias(alias);
                            } else {
                                Alias annotation = getter.isAnnotationPresent(Alias.class) ? getter.getAnnotation(Alias.class) : null;
                                property.setAlias(annotation != null ? annotation.value() : field);
                            }
                            int index = indexes.indexOf(field);
                            if (index >= 0) property.setOrder(index);
                            Integer order = extendedInterpretation != null ? extendedInterpretation.getOrder() : null;
                            if (order != null) property.setOrder(order);
                            else property.setOrder(getter.isAnnotationPresent(Order.class) ? getter.getAnnotation(Order.class).value() : property.getOrder());
                            String style = extendedInterpretation != null ? extendedInterpretation.getStyle() : null;
                            if (style != null && !style.trim().isEmpty()) property.setStyle(style);
                            else property.setStyle(getter.isAnnotationPresent(Style.class) ? getter.getAnnotation(Style.class).value() : null);

                            this.properties.put(field, property);
                        }

                        this.summary = classInterpretation != null ? classInterpretation.getSummary() : null;
                        this.description = classInterpretation != null ? classInterpretation.getContent() : null;
                        this.deprecated = classInterpretation != null && classInterpretation.getDeprecated() != null
                                ? classInterpretation.getDeprecated()
                                : clazz.isAnnotationPresent(Deprecated.class)
                                ? "deprecated"
                                : null;
                        Integer order = classInterpretation != null ? classInterpretation.getOrder() : null;
                        if (order != null) this.order = order;
                        else this.order = clazz.isAnnotationPresent(Order.class) ? clazz.getAnnotation(Order.class).value() : Integer.MAX_VALUE;

                        String style = classInterpretation != null ? classInterpretation.getStyle() : null;
                        if (style != null && !style.trim().isEmpty()) this.style = style;
                        else this.style = clazz.isAnnotationPresent(Style.class) ? clazz.getAnnotation(Style.class).value() : null;

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
                this.component = Schema.valueOf(genericComponentType, cache, supplier, interpreter);
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

    @Override
    public int compareTo(Schema that) {
        int c = Integer.compare(this.getOrder(), that.getOrder());
        if (c != 0) return c;
        else return this.getName().compareTo(that.getName());
    }

    public static Schema valueOf(Type type) {
        return valueOf(type, new DefaultInterpreter());
    }

    public static Schema valueOf(Type type, Interpreter interpreter) {
        return valueOf(type, new SystemSupplier(), interpreter);
    }

    public static Schema valueOf(Type type, Supplier supplier) {
        return valueOf(type, supplier, new DefaultInterpreter());
    }

    public static Schema valueOf(Type type, Supplier supplier, Interpreter interpreter) {
        return valueOf(type, new HashMap<Type, Schema>(), supplier, interpreter);
    }

    private static Schema valueOf(Type type, Map<Type, Schema> cache, Supplier supplier, Interpreter interpreter) {
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
                : supplier.contains(type)
                ? supplier.acquire(type)
                : new Schema(type, cache, supplier, interpreter);
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

    public boolean isPrimitive() {
        return category == Category.BASIC && name != null && name.matches("boolean|byte|short|char|int|float|long|double");
    }

    public Schema toWrapper() {
        if (!isPrimitive()) throw new IllegalStateException();
        else if (name.equals("boolean")) return Schema.valueOf(Boolean.class);
        else if (name.equals("byte")) return Schema.valueOf(Byte.class);
        else if (name.equals("short")) return Schema.valueOf(Short.class);
        else if (name.equals("char")) return Schema.valueOf(Character.class);
        else if (name.equals("int")) return Schema.valueOf(Integer.class);
        else if (name.equals("float")) return Schema.valueOf(Float.class);
        else if (name.equals("long")) return Schema.valueOf(Long.class);
        else if (name.equals("double")) return Schema.valueOf(Double.class);
        else return null;
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

    public HDType toType(String pkg, boolean pkgForced, Supplier supplier) {
        switch (category) {
            case BASIC:
                return HDType.valueOf(supplier.acquire(this));
            case DICTIONARY:
                HDClass rawType = new HDClass(Map.class);
                HDType[] actualTypeArguments = new HDType[]{new HDClass(String.class), component.toType(pkg, pkgForced, supplier)};
                return new HDParameterizedType(rawType, null, actualTypeArguments);
            case ARRAY:
                HDType componentType = component.toType(pkg, pkgForced, supplier);
                return component.isPrimitive() ? new HDClass(componentType) : new HDParameterizedType(HDType.valueOf(List.class), null, componentType);
            case ENUM:
                String enumPkg = pkgForced || this.pkg == null ? pkg : this.pkg;
                return new HDClass(HDClass.Category.ENUM, (enumPkg == null || enumPkg.isEmpty() ? "" : enumPkg + ".") + name);
            case OBJECT:
                String classPkg = pkgForced || this.pkg == null ? pkg : this.pkg;
                return new HDClass((classPkg == null || classPkg.isEmpty() ? "" : classPkg + ".") + name);
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

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
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

    public void setComponent(Schema component) {
        this.component = component;
    }

    public Schema getOwner() {
        return owner;
    }

    public void setOwner(Schema owner) {
        this.owner = owner;
    }

    public Set<Constant> getConstants() {
        return constants;
    }

    public void setConstants(Set<Constant> constants) {
        this.constants = constants;
    }

    public Collection<Schema> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Collection<Schema> dependencies) {
        this.dependencies = dependencies;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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
