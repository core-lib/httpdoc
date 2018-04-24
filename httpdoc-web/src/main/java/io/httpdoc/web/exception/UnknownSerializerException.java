package io.httpdoc.web.exception;

import io.httpdoc.core.exception.HttpdocException;

/**
 * 未知文档序列化器异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 14:06
 **/
public class UnknownSerializerException extends HttpdocException {

    public UnknownSerializerException() {
    }

    public UnknownSerializerException(String message) {
        super(message);
    }

    public UnknownSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownSerializerException(Throwable cause) {
        super(cause);
    }

}
