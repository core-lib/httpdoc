package io.httpdoc.springmvc;

import io.httpdoc.core.Document;
import org.springframework.stereotype.Component;

/**
 * SpringMVC 接口文档封装
 *
 * @author 钟宝林
 * @date 2018-04-27 21:34
 **/
@Component
public class SpringmvcDocument {

    private Document document;

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
