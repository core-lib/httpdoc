package io.httpdoc.core.generation;

import io.httpdoc.core.Schema;

/**
 * Schema生成上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 10:45
 **/
public class SchemaGenerateContext extends GenerateContext {
    private final Schema schema;

    public SchemaGenerateContext(Generation generation, Schema schema) {
        super(generation);
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
