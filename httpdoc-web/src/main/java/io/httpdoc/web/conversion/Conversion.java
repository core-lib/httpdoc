package io.httpdoc.web.conversion;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 转换动作
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:11
 **/
public class Conversion {
    final String name;
    final Object value;
    final Type type;
    final boolean decoded;
    final String charset;
    final String expression;
    final String[] values;

    public Conversion(String name, Object value, Type type, boolean decoded, String charset, String expression, String[] values) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.decoded = decoded;
        this.charset = charset;
        this.expression = expression;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public boolean isDecoded() {
        return decoded;
    }

    public String getCharset() {
        return charset;
    }

    public String getExpression() {
        return expression;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Conversion{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type=" + type +
                ", decoded=" + decoded +
                ", charset='" + charset + '\'' +
                ", expression='" + expression + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
