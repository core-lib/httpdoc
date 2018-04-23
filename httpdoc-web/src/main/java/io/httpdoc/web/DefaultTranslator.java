package io.httpdoc.web;

import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.exception.DocumentTranslationException;

/**
 * 缺省的翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:16
 **/
public class DefaultTranslator implements Translator {

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        return new Document();
    }

}
