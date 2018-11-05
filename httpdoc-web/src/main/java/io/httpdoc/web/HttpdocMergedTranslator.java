package io.httpdoc.web;

import io.httpdoc.core.Document;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.kit.LoadKit;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;

import java.util.Collection;

/**
 * 缺省的翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:16
 **/
public class HttpdocMergedTranslator implements Translator {
    private final Collection<Translator> translators = LoadKit.load(this.getClass().getClassLoader(), Translator.class).values();

    HttpdocMergedTranslator() {
    }

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        Document document = new Document();
        document.setHttpdoc(translation.getHttpdoc());
        document.setProtocol(translation.getProtocol());
        document.setHostname(translation.getHostname());
        document.setPort(translation.getPort());
        document.setContext(translation.getContext());
        document.setVersion(translation.getVersion());
        document.setDescription(translation.getDescription());
        for (Translator translator : translators) {
            Document doc = translator.translate(translation);
            document.getControllers().addAll(doc.getControllers());
            document.getSchemas().putAll(doc.getSchemas());
        }
        return document;
    }

    @Override
    public String normalize(String path) {
        return null;
    }

}
