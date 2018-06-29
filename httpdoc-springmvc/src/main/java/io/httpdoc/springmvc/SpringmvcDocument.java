package io.httpdoc.springmvc;

import io.httpdoc.core.Document;

/**
 * SpringMVC 接口文档封装
 *
 * @author 钟宝林
 * @date 2018-04-27 21:34
 **/
public class SpringmvcDocument {

    private static volatile SpringmvcDocument instance;
    private Document document;

    private SpringmvcDocument() {

    }

    public static SpringmvcDocument getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SpringmvcDocument.class) {
            if (instance != null) {
                return instance;
            }
            instance = new SpringmvcDocument();
        }
        return instance;
    }

    public boolean isInit() {
        return document != null;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
