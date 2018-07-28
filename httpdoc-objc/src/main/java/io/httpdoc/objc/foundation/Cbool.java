package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * BOOL
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 15:56
 **/
public class Cbool implements ObjC {

    @Override
    public String getName() {
        return "bool";
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
