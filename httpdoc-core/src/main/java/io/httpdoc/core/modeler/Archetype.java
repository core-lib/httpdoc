package io.httpdoc.core.modeler;

import io.httpdoc.core.Schema;
import io.httpdoc.core.supplier.Supplier;

/**
 * 结构
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:02
 **/
public class Archetype {
    private String pkg;
    private boolean pkgForced;
    private Supplier supplier;
    private Schema schema;

    public Archetype() {
    }

    public Archetype(String pkg, boolean pkgForced, Supplier supplier, Schema schema) {
        this.pkg = pkg;
        this.pkgForced = pkgForced;
        this.supplier = supplier;
        this.schema = schema;
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

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
