package io.httpdoc.spring.mvc;

import io.httpdoc.core.Document;
import io.httpdoc.core.translation.Translation;

public class ControllerTranslation extends SpringMVCTranslation {
    private final Document document;

    public ControllerTranslation(Translation translation, Document document) {
        super(translation);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

}
