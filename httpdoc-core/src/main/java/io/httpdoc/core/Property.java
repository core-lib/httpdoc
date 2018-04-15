package io.httpdoc.core;

/**
 * 资源模型属性
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:43
 **/
public class Property extends Definition {
    private static final long serialVersionUID = 5280642997370612295L;

    private Schema schema;
    private String description;

    public Property() {
    }

    Property(Schema schema) {
        this.schema = schema;
    }

    Property(Schema schema, String description) {
        this.schema = schema;
        this.description = description;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return schema.toString();
    }
}
