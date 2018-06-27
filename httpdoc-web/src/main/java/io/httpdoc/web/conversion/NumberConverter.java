package io.httpdoc.web.conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 数字类型转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:05
 **/
public class NumberConverter implements Converter<Number> {

    @Override
    public boolean supports(Class<?> type) {
        return Number.class.isAssignableFrom(type);
    }

    @Override
    public Map<String, String[]> convert(String name, Number value, ConversionProvider provider) throws Exception {
        return Collections.singletonMap(name, new String[]{String.valueOf(value)});
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == BigInteger.class
                || conversion.type == BigDecimal.class
                || conversion.type == AtomicInteger.class
                || conversion.type == AtomicLong.class
                || conversion.type == Number.class;
    }

    @Override
    public Number convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return (Number) conversion.value;
        else if (conversion.type == BigInteger.class) return new BigInteger(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == BigDecimal.class) return new BigDecimal(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == AtomicInteger.class) return new AtomicInteger(Integer.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset)));
        else if (conversion.type == AtomicLong.class) return new AtomicLong(Long.valueOf(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset)));
        else if (conversion.type == Number.class) return new BigDecimal(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else return (Number) conversion.value;
    }

}
