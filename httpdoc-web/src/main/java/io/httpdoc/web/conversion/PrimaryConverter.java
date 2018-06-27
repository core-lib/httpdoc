package io.httpdoc.web.conversion;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;

/**
 * 基本类型转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:36
 **/
public class PrimaryConverter implements Converter<Object> {

    @Override
    public boolean supports(Class<?> type) {
        return type == boolean.class
                || type == byte.class
                || type == short.class
                || type == char.class
                || type == int.class
                || type == float.class
                || type == long.class
                || type == double.class;
    }

    @Override
    public Map<String, String[]> convert(String name, Object value, ConversionProvider provider) throws Exception {
        return Collections.singletonMap(name, new String[]{String.valueOf(value)});
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == boolean.class
                || conversion.type == byte.class
                || conversion.type == short.class
                || conversion.type == char.class
                || conversion.type == int.class
                || conversion.type == float.class
                || conversion.type == long.class
                || conversion.type == double.class;
    }

    @Override
    public Object convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return conversion.value;
        else if (conversion.type == boolean.class) return Boolean.parseBoolean(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == byte.class) return Byte.parseByte(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == short.class) return Short.parseShort(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == char.class) return conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset).charAt(0);
        else if (conversion.type == int.class) return Integer.parseInt(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == float.class) return Float.parseFloat(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == long.class) return Long.parseLong(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == double.class) return Double.parseDouble(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else return conversion.value;
    }

}
