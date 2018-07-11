package io.httpdoc.objective.c.core;

import io.httpdoc.core.Parameter;
import io.httpdoc.core.Schema;

public class ObjCParameter extends Parameter {
    private final Parameter parameter;

    public ObjCParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.getName();
    }

    @Override
    public void setName(String name) {
        parameter.setName(name);
    }

    @Override
    public String getScope() {
        return parameter.getScope();
    }

    @Override
    public void setScope(String scope) {
        parameter.setScope(scope);
    }

    @Override
    public Schema getType() {
        Schema type = parameter.getType();
        if (type == null) return null;
        return new ObjCSchema(type);
    }

    @Override
    public void setType(Schema type) {
        parameter.setType(type);
    }

    @Override
    public String getDescription() {
        return parameter.getDescription();
    }

    @Override
    public void setDescription(String description) {
        parameter.setDescription(description);
    }
}
