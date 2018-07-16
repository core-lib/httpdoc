package io.httpdoc.objective.c.core;

import io.httpdoc.core.Parameter;
import io.httpdoc.core.Schema;

public class ObjCParameter extends Parameter {
    private static final long serialVersionUID = -7634617766193937946L;
    private final String prefix;
    private final Parameter parameter;

    public ObjCParameter(String prefix, Parameter parameter) {
        this.prefix = prefix;
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
    public String getAlias() {
        return parameter.getAlias();
    }

    @Override
    public void setAlias(String alias) {
        parameter.setAlias(alias);
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
        return new ObjCSchema(prefix, type);
    }

    @Override
    public void setType(Schema type) {
        parameter.setType(type);
    }

    @Override
    public String getPath() {
        return parameter.getPath();
    }

    @Override
    public void setPath(String path) {
        parameter.setPath(path);
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
