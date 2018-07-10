package io.httpdoc.core.exception;

import io.httpdoc.core.Schema;
import io.httpdoc.core.modeler.Modeler;

/**
 * Schema 不可设计的异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:11
 **/
public class SchemaDesignException extends HttpdocRuntimeException {
    private static final long serialVersionUID = -4466227061085246982L;

    private final Modeler<?> modeler;
    private final Schema schema;

    public SchemaDesignException(Modeler<?> modeler, Schema schema) {
        this.modeler = modeler;
        this.schema = schema;
    }

    public SchemaDesignException(String message, Modeler<?> modeler, Schema schema) {
        super(message);
        this.modeler = modeler;
        this.schema = schema;
    }

    public SchemaDesignException(String message, Throwable cause, Modeler<?> modeler, Schema schema) {
        super(message, cause);
        this.modeler = modeler;
        this.schema = schema;
    }

    public SchemaDesignException(Throwable cause, Modeler<?> modeler, Schema schema) {
        super(cause);
        this.modeler = modeler;
        this.schema = schema;
    }

    public Modeler<?> getModeler() {
        return modeler;
    }

    public Schema getSchema() {
        return schema;
    }
}
