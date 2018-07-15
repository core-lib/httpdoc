package io.httpdoc.jestful.server;

import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;

public abstract class JestfulTranslation {
    private final Translation translation;

    protected JestfulTranslation(Translation translation) {
        this.translation = translation;
    }

    protected JestfulTranslation(JestfulTranslation parent) {
        this.translation = parent.translation;
    }

    public String getHttpdoc() {
        return translation.getHttpdoc();
    }

    public String getProtocol() {
        return translation.getProtocol();
    }

    public String getHostname() {
        return translation.getHostname();
    }

    public Integer getPort() {
        return translation.getPort();
    }

    public String getContext() {
        return translation.getContext();
    }

    public String getVersion() {
        return translation.getVersion();
    }

    public Container getContainer() {
        return translation.getContainer();
    }

    public Supplier getSupplier() {
        return translation.getSupplier();
    }

    public Interpreter getInterpreter() {
        return translation.getInterpreter();
    }

}
