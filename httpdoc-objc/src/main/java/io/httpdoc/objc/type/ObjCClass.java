package io.httpdoc.objc.type;

import io.httpdoc.core.type.HDClass;

import java.util.Set;

/**
 * ObjCConstant 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 11:35
 **/
public class ObjCClass extends ObjCType {

    public ObjCClass(String prefix, HDClass hdClass) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Set<String> imports() {
        return null;
    }

}
