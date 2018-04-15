package io.httpdoc.core.exception;

import java.lang.reflect.Type;

public class UnsupportedSchemaException extends HttpdocRuntimeException {
    private static final long serialVersionUID = 3199452935883202550L;

    private final Type schema;

    public UnsupportedSchemaException(Type schema) {
        super("httpdoc not supports schema [" + schema + "] yet, may be it would be supported in later versions");
        this.schema = schema;
    }

    public Type getSchema() {
        return schema;
    }

}
