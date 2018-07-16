package io.httpdoc.web.conversion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:21
 **/
public class MapConverter implements Converter<Map<?, ?>> {

    @Override
    public boolean supports(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    @Override
    public Map<String, String[]> convert(String name, Map<?, ?> value, ConversionProvider provider) throws Exception {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            Map<String, String[]> m = provider.convert(name + "['" + entry.getKey() + "']", entry.getValue());
            map.putAll(m);
        }
        return map;
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type instanceof ParameterizedType
                && ((ParameterizedType) conversion.type).getRawType() == Map.class;
    }

    @Override
    public Map<?, ?> convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion)) return (Map<?, ?>) conversion.value;
        ParameterizedType mapType = (ParameterizedType) conversion.type;
        Type keyType = mapType.getActualTypeArguments()[0];
        Type valueType = mapType.getActualTypeArguments()[1];
        if (conversion.name.isEmpty()) {
            Map map = conversion.value != null ? (Map<?, ?>) conversion.value : new LinkedHashMap();
            Conversion kcvs = new Conversion(KEY, null, keyType, conversion.decoded, conversion.charset, KEY, new String[]{conversion.expression});
            Object key = provider.convert(kcvs);
            Object value = map.get(key);
            Conversion vcvs = new Conversion(KEY, value, valueType, conversion.decoded, conversion.charset, KEY, conversion.values);
            value = provider.convert(vcvs);
            map.put(key, value);
            return map;
        } else if (conversion.expression.startsWith(conversion.name + ".")) {
            String expression = conversion.expression.substring(conversion.name.length() + 1);
            int i1 = expression.indexOf(".");
            int i2 = expression.indexOf("[");
            int i = i1 * i2 >= 0 ? Math.min(i1, i2) : Math.max(i1, i2);
            String field = i < 0 ? expression : expression.substring(0, i);
            Conversion kcvs = new Conversion(KEY, null, keyType, conversion.decoded, conversion.charset, KEY, new String[]{field});
            Object key = provider.convert(kcvs);
            Map map = conversion.value != null ? (Map<?, ?>) conversion.value : new LinkedHashMap();
            Object value = map.get(key);
            Conversion vcvs = new Conversion(field, value, valueType, conversion.decoded, conversion.charset, expression, conversion.values);
            value = provider.convert(vcvs);
            map.put(key, value);
            return map;
        } else if (conversion.expression.startsWith(conversion.name + "['")) {
            String expression = conversion.expression.substring(conversion.name.length());
            int i = expression.indexOf("']");
            if (i < 0) return (Map<?, ?>) conversion.value;
            String field = expression.substring(2, i);
            Conversion kcvs = new Conversion(KEY, null, keyType, conversion.decoded, conversion.charset, KEY, new String[]{field});
            Object key = provider.convert(kcvs);
            Map map = conversion.value != null ? (Map<?, ?>) conversion.value : new LinkedHashMap();
            Object value = map.get(key);
            Conversion vcvs = new Conversion("['" + field + "']", value, valueType, conversion.decoded, conversion.charset, expression, conversion.values);
            value = provider.convert(vcvs);
            map.put(key, value);
            return map;
        } else {
            return (Map<?, ?>) conversion.value;
        }
    }

}
