package io.httpdoc.objc.type;

import io.httpdoc.core.Importable;
import io.httpdoc.core.type.HDType;

/**
 * ObjCConstant 类型父类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 11:36
 **/
public abstract class ObjCType extends HDType implements Importable {

    public static ObjCType valueOf(String prefix, HDType type) {

        return null;
    }

    public abstract String getName();

    public abstract boolean isPrimitive();

    @Override
    public CharSequence getAbbrName() {
        return getName();
    }

    @Override
    public CharSequence getTypeName() {
        return getName();
    }

}
