package io.httpdoc.core;

import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

public interface Src<P extends Preference> {

    <T extends LineAppender<T>> void joinTo(T appender, P preference) throws IOException;

}
