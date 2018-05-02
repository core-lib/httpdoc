package io.httpdoc.springmvc;

import io.httpdoc.core.Document;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.exception.DocumentTranslationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 钟宝林
 * @date 2018-04-27 21:20
 **/
@Component
public class SpringmvcServerTranslator implements Translator {

    @Resource
    private SpringmvcDocument document;

    @Override
    public Document translate(Translation translation) {
        return document.getDocument();
    }
}
