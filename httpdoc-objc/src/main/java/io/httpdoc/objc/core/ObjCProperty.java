package io.httpdoc.objc.core;

import io.httpdoc.core.Property;
import io.httpdoc.core.Schema;

public class ObjCProperty extends Property {
    private static final long serialVersionUID = 5785907913303818283L;
    private final String prefix;
    private final Property property;

    public ObjCProperty(String prefix, Property property) {
        this.prefix = prefix;
        this.property = property;
    }

    @Override
    public String getAlias() {
        return property.getAlias();
    }

    @Override
    public void setAlias(String alias) {
        property.setAlias(alias);
    }

    @Override
    public Schema getType() {
        Schema type = property.getType();
        if (type == null) return null;
        return new ObjCSchema(prefix, type);
    }

    @Override
    public void setType(Schema type) {
        property.setType(type);
    }

    @Override
    public int getOrder() {
        return property.getOrder();
    }

    @Override
    public void setOrder(int order) {
        property.setOrder(order);
    }

    @Override
    public String getStyle() {
        return super.getStyle();
    }

    @Override
    public void setStyle(String style) {
        super.setStyle(style);
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public void setDescription(String description) {
        property.setDescription(description);
    }

}
