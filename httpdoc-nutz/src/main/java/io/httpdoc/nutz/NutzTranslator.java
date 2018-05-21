package io.httpdoc.nutz;

import io.httpdoc.core.Document;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.nutz.mvc.NutMvcContext;
import org.nutz.mvc.config.AtMap;

import java.util.Map;

/**
 * Nutz Httpdoc 翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 12:27
 **/
public class NutzTranslator implements Translator {

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());

        Container container = translation.getContainer();
        NutMvcContext nutMvcContext = (NutMvcContext) container.get("__nutz__mvc__ctx");
        if (nutMvcContext == null) return document;
        Map<String, AtMap> mapping = nutMvcContext.atMaps;

        return document;
    }

    @Override
    public String normalize(String path) {
        return null;
    }

}
