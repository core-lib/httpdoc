package io.httpdoc.core;

import io.httpdoc.core.provider.Provider;

/**
 * 生成对象
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:36
 **/
public class Generation {
    private Document document;
    private String directory;
    private String pkg;
    private Provider provider;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
