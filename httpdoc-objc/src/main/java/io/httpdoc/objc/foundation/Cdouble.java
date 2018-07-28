package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * double
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:08
 **/
public class Cdouble implements ObjC {

    @Override
    public String getName() {
        return "double";
    }

    @Override
    public Kind getKind() {
        return Kind.PRIMITIVE;
    }

    @Override
    public Reference getReference() {
        return Reference.ASSIGN;
    }

    @Override
    public String getLocation() {
        return FOUNDATION;
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
