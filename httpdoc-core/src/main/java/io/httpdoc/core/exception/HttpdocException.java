package io.httpdoc.core.exception;

public class HttpdocException extends Exception {
    private static final long serialVersionUID = -2890269220183901286L;

    public HttpdocException() {
    }

    public HttpdocException(String message) {
        super(message);
    }

    public HttpdocException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpdocException(Throwable cause) {
        super(cause);
    }

}
