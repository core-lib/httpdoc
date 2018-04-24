package io.httpdoc.web.exception;

import io.httpdoc.core.exception.HttpdocException;

/**
 * 未知文档序列化器异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 14:06
 **/
public class UnknownTranslatorException extends HttpdocException {

    public UnknownTranslatorException() {
    }

    public UnknownTranslatorException(String message) {
        super(message);
    }

    public UnknownTranslatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownTranslatorException(Throwable cause) {
        super(cause);
    }

}
