package io.httpdoc.springmvc;

import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.exception.DocumentTranslationException;

/**
 * SpringMVC 文档翻译器
 *
 * @author 钟宝林
 * @date 2018-04-27 21:20
 **/
public class SpringmvcServerTranslator implements Translator {

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        SpringmvcDocument instance = SpringmvcDocument.getInstance();
        if (!instance.isInit()) {
            throw new DocumentTranslationException("document is not ready");
        }
        return instance.getDocument();
    }

    @Override
    public String normalize(String path) {
        return null;
    }

}
