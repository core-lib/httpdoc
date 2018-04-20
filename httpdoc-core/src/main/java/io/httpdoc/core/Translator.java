package io.httpdoc.core;

import io.httpdoc.core.exception.DocumentTranslationException;

/**
 * 文档翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 15:48
 **/
public interface Translator {

    Document translate(Translation translation) throws DocumentTranslationException;

}
