package io.httpdoc.core;

/**
 * 资源模型属性
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:43
 **/
public class Property {
    private Schema schema;
    private String comment;

    public Property() {
    }

    Property(Schema schema) {
        this.schema = schema;
    }

    Property(Schema schema, String comment) {
        this.schema = schema;
        this.comment = comment;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return schema.toString();
    }
}
