package io.httpdoc.core.annotation;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;

import java.io.IOException;
import java.util.Set;

public class HDAnnotationEnum extends HDAnnotationConstant {
    private final HDClass type;
    private final String name;

    public HDAnnotationEnum(HDClass type, String name) {
        if (type == null || name == null) throw new NullPointerException();
        if (name.trim().isEmpty()) throw new IllegalArgumentException("enum name is empty or blank");
        this.type = type;
        this.name = name;
    }

    public HDAnnotationEnum(Enum<?> e) {
        this(e != null ? new HDClass(e.getClass()) : null, e != null ? e.name() : null);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append(type).append(".").append(name);
    }

    @Override
    public Set<String> imports() {
        return type.imports();
    }
}
