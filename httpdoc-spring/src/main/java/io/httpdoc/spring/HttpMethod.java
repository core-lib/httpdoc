package io.httpdoc.spring;

public enum HttpMethod {

    GET(false),
    HEAD(false),
    POST(true),
    PUT(true),
    PATCH(true),
    DELETE(false),
    OPTIONS(false),
    TRACE(false);

    public final boolean acceptBody;

    HttpMethod(boolean acceptBody) {
        this.acceptBody = acceptBody;
    }

    public static HttpMethod constantOf(String name) {
        if (name == null) throw new IllegalArgumentException("name == null");
        else return valueOf(name.toUpperCase());
    }

}