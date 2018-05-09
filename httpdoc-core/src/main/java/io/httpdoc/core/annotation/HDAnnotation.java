package io.httpdoc.core.annotation;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class HDAnnotation extends HDAnnotationConstant {
    private final HDClass type;
    private final Map<CharSequence, HDAnnotationConstant[]> properties;

    public HDAnnotation(String type) {
        this(type, null);
    }

    public HDAnnotation(Class<? extends Annotation> type) {
        this(type, null);
    }

    public HDAnnotation(HDClass type) {
        this(type, null);
    }

    public HDAnnotation(String type, Map<CharSequence, HDAnnotationConstant[]> properties) {
        this.type = new HDClass(type);
        this.properties = properties != null ? new LinkedHashMap<>(properties) : new LinkedHashMap<CharSequence, HDAnnotationConstant[]>();
    }

    public HDAnnotation(Class<? extends Annotation> type, Map<CharSequence, HDAnnotationConstant[]> properties) {
        this.type = new HDClass(type);
        this.properties = properties != null ? new LinkedHashMap<>(properties) : new LinkedHashMap<CharSequence, HDAnnotationConstant[]>();
    }

    public HDAnnotation(HDClass type, Map<CharSequence, HDAnnotationConstant[]> properties) {
        if (type == null) throw new NullPointerException();
        this.type = type;
        this.properties = properties != null ? new LinkedHashMap<>(properties) : new LinkedHashMap<CharSequence, HDAnnotationConstant[]>();
    }

    public HDAnnotation(Annotation annotation) {
        try {
            if (annotation == null) throw new NullPointerException();
            this.type = new HDClass(annotation.annotationType());
            Map<CharSequence, HDAnnotationConstant[]> properties = new LinkedHashMap<>();
            Method[] methods = annotation.annotationType().getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() != annotation.annotationType()) continue;
                if (method.getParameterTypes().length > 0) continue;
                Object value = method.invoke(annotation);
                String name = method.getName();
                HDAnnotationConstant[] constants = value.getClass().isArray() ? valuesOf((Object[]) value) : new HDAnnotationConstant[]{valueOf(value)};
                Object defaultValue = method.getDefaultValue();
                boolean original = value.equals(defaultValue);
                properties.put(new HDAnnotationKey(name, original), constants);
            }
            this.properties = properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("@");
        appender.append(type);
        if (properties.isEmpty()) return;
        appender.append("(");
        boolean first = true;
        Set<CharSequence> keys = new LinkedHashSet<>(properties.keySet());
        if (preference.isAnnotationDefaultValueHidden()) {
            Iterator<CharSequence> iterator = keys.iterator();
            while (iterator.hasNext()) {
                CharSequence key = iterator.next();
                if (key instanceof HDAnnotationKey && ((HDAnnotationKey) key).isOriginal()) iterator.remove();
            }
        }

        for (CharSequence key : keys) {
            if (!first) appender.append(", ");
            // 如果只有一个属性而且这个属性名叫value而且用户允许隐藏, 那么隐藏掉!
            if (!preference.isAnnotationValueKeyHiddenIfUnnecessary() || keys.size() != 1 || !"value".equals(key.toString())) {
                appender.append(key);
                appender.append(" = ");
            }
            HDAnnotationConstant[] constants = properties.get(key);
            if (constants == null || constants.length == 0) {
                appender.append("{}");
            } else if (constants.length == 1) {
                constants[0].joinTo(appender, preference);
            } else {
                appender.append("{");
                for (int i = 0; i < constants.length; i++) {
                    if (i > 0) appender.append(", ");
                    constants[i].joinTo(appender, preference);
                }
                appender.append("}");
            }
            first = false;
        }
        appender.append(")");
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>(type.imports());
        for (HDAnnotationConstant[] constants : properties.values()) for (HDAnnotationConstant constant : constants) imports.addAll(constant.imports());
        return imports;
    }

    public HDClass getType() {
        return type;
    }

    public Map<CharSequence, HDAnnotationConstant[]> getProperties() {
        return properties;
    }

}
