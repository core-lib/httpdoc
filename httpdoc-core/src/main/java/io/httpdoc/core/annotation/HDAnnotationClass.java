package io.httpdoc.core.annotation;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;

import java.io.IOException;
import java.util.List;

public class HDAnnotationClass extends HDAnnotationConstant {
    private final HDClass clazz;

    public HDAnnotationClass(HDClass clazz) {
        if (clazz == null) throw new NullPointerException();
        this.clazz = clazz;
    }

    public HDAnnotationClass(Class<?> clazz) {
        this(clazz != null ? new HDClass(clazz) : null);
    }

    public HDAnnotationClass(String className) {
        this(className != null ? new HDClass(className) : null);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {

    }

    @Override
    public List<String> imports() {
        return null;
    }
}
