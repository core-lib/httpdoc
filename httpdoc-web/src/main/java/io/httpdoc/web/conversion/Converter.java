package io.httpdoc.web.conversion;

import java.util.Map;

/**
 * 转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:21
 **/
public interface Converter<T> {

    boolean supports(Class<?> type);

    Map<String, String[]> convert(String name, T value, ConversionProvider provider) throws Exception;

    /**
     * 为了递归的缺省键
     */
    String KEY = "KEY";

    /**
     * 是否支持该转换动作
     *
     * @param conversion 转换动作
     * @return 支持:true 否则:false
     */
    boolean supports(Conversion conversion);

    /**
     * 转换
     *
     * @param conversion 转换动作
     * @param provider   转换提供器
     * @return 转换后的结果
     * @throws ConvertingException 转换异常
     */
    T convert(Conversion conversion, ConversionProvider provider) throws Exception;

}
