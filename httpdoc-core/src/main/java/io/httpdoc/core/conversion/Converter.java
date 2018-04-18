package io.httpdoc.core.conversion;

import io.httpdoc.core.Document;

import java.util.Map;

/**
 * 文档转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 9:59
 **/
public interface Converter {

    Map<String, Object> convert(Document document);

    Map<String, Object> convert(Document document, Format format);

    Document convert(Map<String, Object> dictionary);

}
