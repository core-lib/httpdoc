package io.httpdoc.objective.c.bridge;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;

import java.util.Map;
import java.util.Set;

/**
 * Objective-C 文档
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 21:02
 **/
public class ObjCDocument extends Document {
    private final Document document;

    public ObjCDocument(Document document) {
        this.document = document;
    }

    @Override
    public Set<Controller> getControllers() {
        return document.getControllers();
    }

    @Override
    public Map<String, Schema> getSchemas() {
        return document.getSchemas();
    }
}
