package io.httpdoc.core.generation;

import io.httpdoc.core.Document;
import io.httpdoc.core.strategy.OverrideStrategy;
import io.httpdoc.core.strategy.Strategy;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;

/**
 * 生成对象
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-19 16:36
 **/
public class Generation {
    private Document document;
    private String directory = System.getProperty("user.dir");
    private String pkg = "";
    private boolean pkgForced = false;
    private Supplier supplier = new SystemSupplier();
    private Strategy strategy = new OverrideStrategy();

    public Generation(Document document) {
        this.document = document;
    }

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

    public boolean isPkgForced() {
        return pkgForced;
    }

    public void setPkgForced(boolean pkgForced) {
        this.pkgForced = pkgForced;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
