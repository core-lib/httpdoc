package io.httpdoc.springmvc;

import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;

/**
 * @author 钟宝林
 * @date 2018-04-27 21:20
 **/
public class SpringmvcServerTranslator implements Translator {

    private SpringmvcDocument document = SpringmvcDocument.getInstance();

    @Override
    public Document translate(Translation translation) {
        return document.getDocument();
    }
}
