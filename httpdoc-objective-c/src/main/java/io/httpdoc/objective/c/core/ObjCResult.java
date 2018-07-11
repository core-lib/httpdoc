package io.httpdoc.objective.c.core;

import io.httpdoc.core.Result;
import io.httpdoc.core.Schema;

public class ObjCResult extends Result {
    private final String prefix;
    private final Result result;

    public ObjCResult(String prefix, Result result) {
        this.prefix = prefix;
        this.result = result;
    }

    @Override
    public Schema getType() {
        Schema type = result.getType();
        if (type == null) return null;
        return new ObjCSchema(prefix, type);
    }

    @Override
    public void setType(Schema type) {
        result.setType(type);
    }

    @Override
    public String getDescription() {
        return result.getDescription();
    }

    @Override
    public void setDescription(String description) {
        result.setDescription(description);
    }
}
