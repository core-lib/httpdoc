package io.httpdoc.core.annotation;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HDAnnotationPrimary extends HDAnnotationConstant {
    private final Object primary;

    HDAnnotationPrimary(Object primary) {
        this.primary = primary;
    }

    public HDAnnotationPrimary(boolean b) {
        this.primary = b;
    }

    public HDAnnotationPrimary(byte b) {
        this.primary = b;
    }

    public HDAnnotationPrimary(short s) {
        this.primary = s;
    }

    public HDAnnotationPrimary(char c) {
        this.primary = c;
    }

    public HDAnnotationPrimary(int i) {
        this.primary = i;
    }

    public HDAnnotationPrimary(float f) {
        this.primary = f;
    }

    public HDAnnotationPrimary(long l) {
        this.primary = l;
    }

    public HDAnnotationPrimary(double d) {
        this.primary = d;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append(String.valueOf(primary));
    }

    @Override
    public List<String> imports() {
        return Collections.emptyList();
    }

}
