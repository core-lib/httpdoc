package io.httpdoc.objective.c.bridge;

import io.httpdoc.core.Category;
import io.httpdoc.core.Schema;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.type.HDType;

/**
 * Objective-C Schema
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 20:51
 **/
public class ObjCSchema extends Schema {
    private final Schema schema;

    public ObjCSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public HDType toType(String pkg, boolean pkgForced, Supplier supplier) {
        return super.toType(pkg, pkgForced, supplier);
    }

    @Override
    public String toName() {
        return category == Category.DICTIONARY ? "dictionary" : super.toName();
    }
}
