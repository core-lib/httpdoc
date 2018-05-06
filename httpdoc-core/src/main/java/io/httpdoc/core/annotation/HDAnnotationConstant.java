package io.httpdoc.core.annotation;

import io.httpdoc.core.DefaultPreference;
import io.httpdoc.core.Importable;
import io.httpdoc.core.Preference;
import io.httpdoc.core.Src;
import io.httpdoc.core.appender.AppendableAppender;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

public abstract class HDAnnotationConstant implements Src<Preference>, Importable {

    public static HDAnnotationConstant[] valuesOf(Object... objects) {
        if (objects == null) throw new NullPointerException();
        HDAnnotationConstant[] constants = new HDAnnotationConstant[objects.length];
        for (int i = 0; i < objects.length; i++) constants[i] = valueOf(objects[i]);
        return constants;
    }

    public static HDAnnotationConstant valueOf(Object object) {
        if (object == null) throw new NullPointerException();
        else if (object.getClass().isArray()) throw new IllegalArgumentException("object can not be array, may be you should call method valuesOf(Object[] objects)");
        else if (object.getClass().isPrimitive()) return new HDAnnotationPrimary(object);
        else if (object instanceof Boolean) return new HDAnnotationPrimary(object);
        else if (object instanceof Byte) return new HDAnnotationPrimary(object);
        else if (object instanceof Short) return new HDAnnotationPrimary(object);
        else if (object instanceof Character) return new HDAnnotationPrimary(object);
        else if (object instanceof Integer) return new HDAnnotationPrimary(object);
        else if (object instanceof Float) return new HDAnnotationPrimary(object);
        else if (object instanceof Long) return new HDAnnotationPrimary(object);
        else if (object instanceof Double) return new HDAnnotationPrimary(object);
        else if (object instanceof String) return new HDAnnotationString((String) object);
        else if (object instanceof Enum<?>) return new HDAnnotationEnum((Enum<?>) object);
        else if (object instanceof Class<?>) return new HDAnnotationClass((Class<?>) object);
        else if (object instanceof Annotation) return new HDAnnotation((Annotation) object);
        else throw new IllegalArgumentException("annotation property value must be type of primary, String, Enum, Class, Annotation or it's array type");
    }

    @Override
    public String toString() {
        try {
            StringWriter writer = new StringWriter();
            AppendableAppender appender = new AppendableAppender(writer);
            joinTo(appender, new DefaultPreference());
            appender.close();
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
