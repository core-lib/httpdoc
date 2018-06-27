package io.httpdoc.web.conversion;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;

/**
 * 基本类型的封装类型转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:43
 **/
public class WrapperConverter implements Converter<Object> {

    @Override
    public boolean supports(Class<?> type) {
        return type == Boolean.class
                || type == Byte.class
                || type == Short.class
                || type == Character.class
                || type == Integer.class
                || type == Float.class
                || type == Long.class
                || type == Double.class;
    }

    @Override
    public Map<String, String[]> convert(String name, Object value, ConversionProvider provider) throws Exception {
        return Collections.singletonMap(name, new String[]{String.valueOf(value)});
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == Boolean.class
                || conversion.type == Byte.class
                || conversion.type == Short.class
                || conversion.type == Character.class
                || conversion.type == Integer.class
                || conversion.type == Float.class
                || conversion.type == Long.class
                || conversion.type == Double.class;
    }

    @Override
    public Object convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return conversion.value;
        else if (conversion.type == Boolean.class) return Boolean.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Byte.class) return Byte.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Short.class) return Short.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Character.class) return conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset).charAt(0);
        else if (conversion.type == Integer.class) return Integer.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Float.class) return Float.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Long.class) return Long.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == Double.class) return Double.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else return conversion.value;
    }

}
