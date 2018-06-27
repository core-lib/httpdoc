package io.httpdoc.web.conversion;

import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数组转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:21
 **/
public class ArrayConverter implements Converter<Object> {

    @Override
    public boolean supports(Class<?> type) {
        return type.isArray();
    }

    @Override
    public Map<String, String[]> convert(String name, Object value, ConversionProvider provider) throws Exception {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(value, i);
            Map<String, String[]> m = provider.convert(name + "[" + i + "]", item);
            map.putAll(m);
        }
        return map;
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type instanceof Class<?>
                && ((Class<?>) conversion.type).isArray();
    }

    @Override
    public Object convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion)) return conversion.value;
        Class<?> arrayType = (Class<?>) conversion.type;
        Class<?> componentType = arrayType.getComponentType();
        if (conversion.name.equals(conversion.expression)) {
            Object array = conversion.value != null ? conversion.value : Array.newInstance(componentType, 0);
            for (String value : conversion.values) {
                Conversion cvs = new Conversion(KEY, null, componentType, conversion.decoded, conversion.charset, KEY, new String[]{value});
                Object element = provider.convert(cvs);
                int length = Array.getLength(array);
                Object arr = Array.newInstance(componentType, length + 1);
                System.arraycopy(array, 0, arr, 0, length);
                Array.set(arr, length, element);
                array = arr;
            }
            return array;
        } else if (conversion.expression.startsWith(conversion.name + "[")) {
            String expression = conversion.expression.substring(conversion.name.length());
            int i = expression.indexOf("]");
            if (i < 0) return conversion.value;
            String idx = expression.substring(1, i);
            idx = conversion.decoded ? idx : URLDecoder.decode(idx, conversion.charset);
            if (!idx.matches("\\d+")) return conversion.value;
            int index = Integer.valueOf(idx);
            Object array = conversion.value != null ? conversion.value : Array.newInstance(componentType, 0);
            int length = Array.getLength(array);
            if (length <= index) {
                Object arr = Array.newInstance(componentType, index + 1);
                System.arraycopy(array, 0, arr, 0, length);
                array = arr;
            }
            Object element = Array.get(array, index);
            Conversion cvs = new Conversion(expression.substring(0, i + 1), element, componentType, conversion.decoded, conversion.charset, expression, conversion.values);
            element = provider.convert(cvs);
            Array.set(array, index, element);
            return array;
        } else {
            return conversion.value;
        }
    }

}
