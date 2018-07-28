package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * char
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:06
 **/
public class Cchar implements ObjC {

    @Override
    public String getName() {
        return "char";
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
