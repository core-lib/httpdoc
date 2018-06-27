package io.httpdoc.web.conversion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Set转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:21
 **/
public class SetConverter implements Converter<Set<?>> {

    @Override
    public boolean supports(Class<?> type) {
        return Set.class.isAssignableFrom(type);
    }

    @Override
    public Map<String, String[]> convert(String name, Set<?> value, ConversionProvider provider) throws Exception {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        int index = 0;
        for (Object item : value) {
            Map<String, String[]> m = provider.convert(name + "[" + index++ + "]", item);
            map.putAll(m);
        }
        return map;
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type instanceof ParameterizedType
                && ((ParameterizedType) conversion.type).getRawType() == Set.class;
    }

    @Override
    public Set<?> convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion)) return (Set<?>) conversion.value;
        ParameterizedType setType = (ParameterizedType) conversion.type;
        Type elementType = setType.getActualTypeArguments()[0];
        if (conversion.name.equals(conversion.expression)) {
            Set set = conversion.value != null ? (Set) conversion.value : new LinkedHashSet();
            for (String value : conversion.values) {
                Conversion cvs = new Conversion(KEY, null, elementType, conversion.decoded, conversion.charset, KEY, new String[]{value});
                Object element = provider.convert(cvs);
                set.add(element);
            }
            return set;
        } else {
            return (Set<?>) conversion.value;
        }
    }

}
