package io.httpdoc.core;

import java.io.Serializable;

public class Definition implements Serializable {
    private static final long serialVersionUID = -9151357695973200936L;

    protected String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
