package io.httpdoc.core.exception;

/**
 * 文档翻译异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:05
 **/
public class DocumentTranslationException extends HttpdocException {
    private static final long serialVersionUID = -4164428123267841738L;

    public DocumentTranslationException() {
    }

    public DocumentTranslationException(String message) {
        super(message);
    }

    public DocumentTranslationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentTranslationException(Throwable cause) {
        super(cause);
    }

}
