package io.httpdoc.spring.mvc;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {

    GET(false, false),
    HEAD(false, false),
    POST(true, true),
    PUT(true, true),
    PATCH(true, true),
    DELETE(true, false),
    OPTIONS(true, false),
    TRACE(false, false);

    private static final Map<String, HttpMethod> mappings = new HashMap<String, HttpMethod>(8);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    private boolean permitsRequestBody;
    private boolean requiresRequestBody;

    HttpMethod(boolean permitsRequestBody, boolean requiresRequestBody) {
        this.permitsRequestBody = permitsRequestBody;
        this.requiresRequestBody = requiresRequestBody;
    }

    public static HttpMethod resolve(String method) {
        return (method != null ? mappings.get(method) : null);
    }

    public boolean matches(String method) {
        return (this == resolve(method));
    }

    /**
     * 是否允许有请求体
     */
    public boolean isPermitsRequestBody() {
        return requiresRequestBody;
    }

    /**
     * 是否要求有请求体
     */
    public boolean isRequiresRequestBody() {
        return requiresRequestBody;
    }


}