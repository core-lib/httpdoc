package io.httpdoc.core;

/**
 * 资源模型属性
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:43
 **/
public class Property extends Definition implements Ordered<Property> {
    private static final long serialVersionUID = 5280642997370612295L;

    private String alias;
    private Schema type;
    private int order = Integer.MAX_VALUE;
    private String style;

    public Property() {
    }

    Property(Schema type, String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public int compareTo(Property that) {
        int c = Integer.compare(this.getOrder(), that.getOrder());
        if (c != 0) return c;
        else return this.getAlias().compareTo(that.getAlias());
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Schema getType() {
        return type;
    }

    public void setType(Schema type) {
        this.type = type;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
