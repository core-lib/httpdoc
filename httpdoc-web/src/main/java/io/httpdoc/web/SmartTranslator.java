package io.httpdoc.web;

import io.httpdoc.core.Document;
import io.httpdoc.core.Loader;
import io.httpdoc.core.Translation;
import io.httpdoc.core.Translator;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.web.exception.UnknownSerializerException;
import io.httpdoc.web.exception.UnknownTranslatorException;

import java.net.URL;
import java.util.Properties;
import java.util.Set;

/**
 * 缺省的翻译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:16
 **/
public class SmartTranslator implements Translator {
    private final Translator translator;

    SmartTranslator() {
        try {
            Set<URL> urls = Loader.load(this.getClass().getClassLoader());
            for (URL url : urls) {
                if (!url.getFile().endsWith("/translator.properties")) continue;
                Properties properties = new Properties();
                properties.load(url.openStream());
                if (properties.isEmpty()) continue;
                String className = (String) properties.values().iterator().next();
                translator = Class.forName(className).asSubclass(Translator.class).newInstance();
                return;
            }
            throw new UnknownTranslatorException("could not find any translator");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Document translate(Translation translation) throws DocumentTranslationException {
        return translator.translate(translation);
    }

}
