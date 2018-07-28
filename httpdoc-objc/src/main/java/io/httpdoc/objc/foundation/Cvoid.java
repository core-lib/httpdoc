package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * void
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:10
 **/
public class Cvoid implements ObjC {

    @Override
    public String getName() {
        return "void";
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
