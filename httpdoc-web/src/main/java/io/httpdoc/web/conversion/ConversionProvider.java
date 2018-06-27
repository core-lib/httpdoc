package io.httpdoc.web.conversion;

import java.text.DateFormat;
import java.util.Map;

/**
 * 转换提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:10
 **/
public interface ConversionProvider {

    DateFormat getSerializationDateFormat();

    void setSerializationDateFormat(DateFormat dateFormat);

    DateFormat getDeserializationDateFormat();

    void setDeserializationDateFormat(DateFormat dateFormat);

    /**
     * 是否支持该类型的转换
     *
     * @param type 类型
     * @return 支持:true 否则:false
     */
    boolean supports(Class<?> type);

    /**
     * 黑盒转换
     *
     * @param name  参数名称
     * @param value 参数值
     * @return 转换后的结果
     * @throws ConvertingException 转换异常
     */
    Map<String, String[]> convert(String name, Object value) throws ConvertingException;

    /**
     * 是否支持该转换动作
     *
     * @param conversion 转换动作
     * @return 支持:true 否则:false
     */
    boolean supports(Conversion conversion);

    /**
     * 黑盒转换, 屏蔽所有细节,提供所有类型的转换
     *
     * @param conversion 转换动作
     * @return 转换后的结果
     * @throws ConvertingException 转换异常
     */
    Object convert(Conversion conversion) throws ConvertingException;

}
