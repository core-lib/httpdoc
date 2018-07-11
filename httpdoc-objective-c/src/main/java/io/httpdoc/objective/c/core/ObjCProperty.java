package io.httpdoc.objective.c.core;

import io.httpdoc.core.Property;
import io.httpdoc.core.Schema;

public class ObjCProperty extends Property {
    private final String prefix;
    private final Property property;

    public ObjCProperty(String prefix, Property property) {
        this.prefix = prefix;
        this.property = property;
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
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public void setDescription(String description) {
        property.setDescription(description);
    }

}
