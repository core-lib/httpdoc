package io.httpdoc.core.exception;

/**
 * 程序生成异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:07
 **/
public class ProgramGenerationException extends HttpdocException {
    private static final long serialVersionUID = -2657763710293183014L;

    public ProgramGenerationException() {
    }

    public ProgramGenerationException(String message) {
        super(message);
    }

    public ProgramGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramGenerationException(Throwable cause) {
        super(cause);
    }
}
