package io.httpdoc.web.conversion;

import io.httpdoc.core.Config;
import io.httpdoc.core.Lifecycle;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.*;

/**
 * 标准的转换提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 13:47
 **/
public class StandardConversionProvider implements ConversionProvider, Lifecycle {
    private final List<Converter> converters;
    private volatile DateFormat serializationDateFormat = DateFormat.getDateTimeInstance();
    private volatile DateFormat deserializationDateFormat = DateFormat.getDateTimeInstance();

    public StandardConversionProvider(Collection<Converter> converters) {
        if (converters == null) throw new NullPointerException();
        this.converters = new ArrayList<>(converters);
    }

    @Override
    public DateFormat getSerializationDateFormat() {
        return serializationDateFormat;
    }

    @Override
    public void setSerializationDateFormat(DateFormat serializationDateFormat) {
        this.serializationDateFormat = serializationDateFormat;
    }

    @Override
    public DateFormat getDeserializationDateFormat() {
        return deserializationDateFormat;
    }

    @Override
    public void setDeserializationDateFormat(DateFormat deserializationDateFormat) {
        this.deserializationDateFormat = deserializationDateFormat;
    }

    @Override
    public boolean supports(Class<?> type) {
        for (Converter converter : converters) if (converter.supports(type)) return true;
        return false;
    }

    @Override
    public Map<String, String[]> convert(String name, Object value) throws ConvertingException {
        if (value == null) return Collections.emptyMap();
        try {
            name = name != null ? name.trim() : "";
            Class<?> type = value.getClass();
            for (Converter converter : converters) if (converter.supports(type)) return converter.convert(name, value, this);
            Map<String, String[]> map = new LinkedHashMap<String, String[]>();
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                String attrName = descriptor.getName();
                if (attrName.equals("class")) continue;
                Method getter = descriptor.getReadMethod();
                Object attrValue = getter.invoke(value);
                if (attrValue == null) continue;
                Map<String, String[]> m = this.convert(name.isEmpty() ? attrName : name + "." + attrName, attrValue);
                map.putAll(m);
            }
            return map;
        } catch (Exception e) {
            throw new ConvertingException(e);
        }
    }

    @Override
    public boolean supports(Conversion conversion) {
        for (Converter<?> converter : converters) if (converter.supports(conversion)) return true;
        return false;
    }

    @Override
    public Object convert(Conversion conversion) throws ConvertingException {
        try {
            for (Converter<?> converter : converters) if (converter.supports(conversion)) return converter.convert(conversion, this);

            Class<?> clazz = conversion.type instanceof Class<?>
                    ? (Class<?>) conversion.type
                    : conversion.type instanceof ParameterizedType && ((ParameterizedType) conversion.type).getRawType() instanceof Class<?>
                    ? (Class<?>) ((ParameterizedType) conversion.type).getRawType()
                    : null;

            if (clazz == null) return conversion.value;

            if (conversion.name.isEmpty()) {
                String expression = conversion.expression;
                int i1 = expression.indexOf(".");
                int i2 = expression.indexOf("[");
                int i = i1 < 0 && i2 < 0 ? -1 : Math.min(i1, i2);
                String field = i < 0 ? expression : expression.substring(0, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance);
                Conversion cvs = new Conversion(field, value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else if (conversion.expression.startsWith(conversion.name + ".")) {
                String expression = conversion.expression.substring(conversion.name.length() + 1);
                int i1 = expression.indexOf(".");
                int i2 = expression.indexOf("[");
                int i = i1 * i2 >= 0 ? Math.min(i1, i2) : Math.max(i1, i2);
                String field = i < 0 ? expression : expression.substring(0, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance);
                Conversion cvs = new Conversion(field, value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else if (conversion.expression.startsWith(conversion.name + "['")) {
                String expression = conversion.expression.substring(conversion.name.length());
                int i = expression.indexOf("']");
                if (i < 0) return conversion.value;
                String field = expression.substring(2, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance, field);
                Conversion cvs = new Conversion("['" + field + "']", value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else {
                return conversion.value;
            }
        } catch (IntrospectionException e) {
            return conversion.value;
        } catch (Exception e) {
            throw new ConvertingException(e, conversion);
        }
    }

    @Override
    public void initial(Config config) throws Exception {
        for (Converter<?> converter : converters) {
            if (converter instanceof Lifecycle) {
                ((Lifecycle) converter).initial(config);
            }
        }
    }

    @Override
    public void destroy() {
        for (Converter<?> converter : converters) {
            if (converter instanceof Lifecycle) {
                ((Lifecycle) converter).destroy();
            }
        }
    }
}
