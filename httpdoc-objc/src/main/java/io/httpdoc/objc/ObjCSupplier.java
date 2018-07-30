package io.httpdoc.objc;

import io.httpdoc.core.Category;
import io.httpdoc.core.Schema;
import io.httpdoc.core.supplier.SystemSupplier;
import io.httpdoc.objc.external.RSPart;
import io.httpdoc.objc.foundation.*;

import java.lang.reflect.Type;

/**
 * ObjC系统类型提供者
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 15:55
 **/
public class ObjCSupplier extends SystemSupplier {

    @Override
    public Type acquire(Schema schema) {
        if (schema == null || schema.getCategory() != Category.BASIC) return null;
        String name = schema.getName();
        switch (name) {
            case "boolean":
                return Cbool.class;
            case "byte":
                return Cint.class;
            case "short":
                return Cshort.class;
            case "char":
                return Cchar.class;
            case "int":
                return Cint.class;
            case "float":
                return Cfloat.class;
            case "long":
                return Clong.class;
            case "double":
                return Cdouble.class;

            case "void":
                return Cvoid.class;

            case "Boolean":
                return NSNumber.class;
            case "Byte":
                return NSNumber.class;
            case "Short":
                return NSNumber.class;
            case "Character":
                return NSNumber.class;
            case "Integer":
                return NSNumber.class;
            case "Float":
                return NSNumber.class;
            case "Long":
                return NSNumber.class;
            case "Double":
                return NSNumber.class;

            case "String":
                return NSString.class;
            case "Number":
                return NSNumber.class;
            case "Date":
                return NSDate.class;
            case "File":
                return RSPart.class;
            case "Object":
                return NSObject.class;
        }
        return null;
    }

}
