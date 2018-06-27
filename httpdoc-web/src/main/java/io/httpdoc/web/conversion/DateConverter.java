package io.httpdoc.web.conversion;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 日期转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:58
 **/
public class DateConverter implements Converter<Date> {

    @Override
    public boolean supports(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }

    @Override
    public Map<String, String[]> convert(String name, Date value, ConversionProvider provider) throws Exception {
        return Collections.singletonMap(name, new String[]{provider.getSerializationDateFormat().format(value)});
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == Date.class;
    }

    @Override
    public Date convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return (Date) conversion.value;
        String[] values = conversion.values;
        boolean decoded = conversion.decoded;
        String charset = conversion.charset;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) return (Date) conversion.value;
        if (!decoded) value = URLDecoder.decode(value, charset);
        return provider.getDeserializationDateFormat().parse(value);
    }
}
