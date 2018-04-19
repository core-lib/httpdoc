package io.httpdoc.core.exception;

import java.lang.reflect.Type;

/**
 * 不支持的Schema异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 11:28
 **/
public class SchemaUnsupportedException extends HttpdocRuntimeException {
    private static final long serialVersionUID = 3199452935883202550L;

    private final Type schema;

    public SchemaUnsupportedException(Type schema) {
        super("httpdoc not supports schema [" + schema + "] yet, may be it would be supported in later versions");
        this.schema = schema;
    }

    public Type getSchema() {
        return schema;
    }

}
