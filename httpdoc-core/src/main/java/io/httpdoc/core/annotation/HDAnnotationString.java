package io.httpdoc.core.annotation;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HDAnnotationString extends HDAnnotationConstant {
    private final String string;

    public HDAnnotationString(String string) {
        if (string == null) throw new NullPointerException();
        this.string = string;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("\"").append(string).append("\"");
    }

    @Override
    public List<String> imports() {
        return Collections.emptyList();
    }
}
