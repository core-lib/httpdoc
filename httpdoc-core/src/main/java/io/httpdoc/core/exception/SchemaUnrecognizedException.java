package io.httpdoc.core.exception;

/**
 * 无法识别的Schema异常
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 11:28
 **/
public class SchemaUnrecognizedException extends HttpdocRuntimeException {
    private static final long serialVersionUID = -7434306557309703581L;

    private final String name;

    public SchemaUnrecognizedException(String name) {
        super("unrecognized schema named " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
