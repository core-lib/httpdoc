package io.httpdoc.web.conversion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * List转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:21
 **/
public class ListConverter implements Converter<List<?>> {

    @Override
    public boolean supports(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    public Map<String, String[]> convert(String name, List<?> value, ConversionProvider provider) throws Exception {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        for (int i = 0; i < value.size(); i++) {
            Object item = value.get(i);
            Map<String, String[]> m = provider.convert(name + "[" + i + "]", item);
            map.putAll(m);
        }
        return map;
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type instanceof ParameterizedType
                && ((ParameterizedType) conversion.type).getRawType() == List.class;
    }

    @Override
    public List<?> convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion)) return (List<?>) conversion.value;
        ParameterizedType listType = (ParameterizedType) conversion.type;
        Type elementType = listType.getActualTypeArguments()[0];
        if (conversion.name.equals(conversion.expression)) {
            List list = conversion.value != null ? (List) conversion.value : new ArrayList();
            for (String value : conversion.values) {
                Conversion cvs = new Conversion(KEY, null, elementType, conversion.decoded, conversion.charset, KEY, new String[]{value});
                Object element = provider.convert(cvs);
                list.add(element);
            }
            return list;
        } else if (conversion.expression.startsWith(conversion.name + "[")) {
            String expression = conversion.expression.substring(conversion.name.length());
            int i = expression.indexOf("]");
            if (i < 0) return (List<?>) conversion.value;
            String idx = expression.substring(1, i);
            idx = conversion.decoded ? idx : URLDecoder.decode(idx, conversion.charset);
            if (!idx.matches("\\d+")) return (List<?>) conversion.value;
            int index = Integer.valueOf(idx);
            List list = conversion.value != null ? (List) conversion.value : new ArrayList();
            for (int k = list.size(); k <= index; k++) list.add(null); // 扩容
            Object element = list.size() > index ? list.get(index) : null;
            Conversion cvs = new Conversion(expression.substring(0, i + 1), element, elementType, conversion.decoded, conversion.charset, expression, conversion.values);
            element = provider.convert(cvs);
            list.set(index, element); // 替换
            return list;
        } else {
            return (List<?>) conversion.value;
        }
    }

}
