package io.httpdoc.spring.mvc;

import io.httpdoc.core.Document;

/**
 * SpringMVC 接口文档封装
 *
 * @author 钟宝林
 * @date 2018-04-27 21:34
 **/
public class SpringMVCDocument {

    private static volatile SpringMVCDocument instance;
    private Document document;

    private SpringMVCDocument() {

    }

    public static SpringMVCDocument getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SpringMVCDocument.class) {
            if (instance != null) {
                return instance;
            }
            instance = new SpringMVCDocument();
        }
        return instance;
    }

    public boolean isInitialized() {
        return document != null;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
