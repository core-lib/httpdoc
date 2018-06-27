package io.httpdoc.web.conversion;

/**
 * 转换异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:16
 **/
public class ConvertingException extends Exception {
    private static final long serialVersionUID = -1469979177747097062L;

    private final Conversion conversion;

    public ConvertingException() {
        this.conversion = null;
    }

    public ConvertingException(String message) {
        super(message);
        this.conversion = null;
    }

    public ConvertingException(String message, Throwable cause) {
        super(message, cause);
        this.conversion = null;
    }

    public ConvertingException(Throwable cause) {
        super(cause);
        this.conversion = null;
    }

    public ConvertingException(Conversion conversion) {
        this.conversion = conversion;
    }

    public ConvertingException(String message, Conversion conversion) {
        super(message);
        this.conversion = conversion;
    }

    public ConvertingException(String message, Throwable cause, Conversion conversion) {
        super(message, cause);
        this.conversion = conversion;
    }

    public ConvertingException(Throwable cause, Conversion conversion) {
        super(cause);
        this.conversion = conversion;
    }


    public Conversion getConversion() {
        return conversion;
    }

}
