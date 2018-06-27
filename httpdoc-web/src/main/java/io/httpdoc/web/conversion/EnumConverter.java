package io.httpdoc.web.conversion;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;

/**
 * 枚举转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:23
 **/
public class EnumConverter implements Converter<Enum<?>> {

    @Override
    public boolean supports(Class<?> type) {
        return type.isEnum();
    }

    @Override
    public Map<String, String[]> convert(String name, Enum<?> value, ConversionProvider provider) throws Exception {
        return Collections.singletonMap(name, new String[]{value.name()});
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type instanceof Class<?>
                && ((Class<?>) conversion.type).isEnum();
    }

    @Override
    public Enum<?> convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return (Enum<?>) conversion.value;
        String[] values = conversion.values;
        boolean decoded = conversion.decoded;
        String charset = conversion.charset;
        Class<?> clazz = (Class<?>) conversion.type;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) return null;
        if (!decoded) value = URLDecoder.decode(value, charset);
        Object result = clazz.getMethod("valueOf", String.class).invoke(null, value);
        return (Enum<?>) result;
    }
}
