package io.httpdoc.core.exception;

public class HttpdocRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3564185526541821107L;

    public HttpdocRuntimeException() {
    }

    public HttpdocRuntimeException(String message) {
        super(message);
    }

    public HttpdocRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpdocRuntimeException(Throwable cause) {
        super(cause);
    }

}
