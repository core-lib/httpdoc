package io.httpdoc.core;

/**
 * 资源模型属性
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:43
 **/
public class Property extends Definition {
    private static final long serialVersionUID = 5280642997370612295L;

    private Schema type;

    public Property() {
    }

    Property(Schema type, String description) {
        this.type = type;
        this.description = description;
    }

    public Schema getType() {
        return type;
    }

    public void setType(Schema type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
