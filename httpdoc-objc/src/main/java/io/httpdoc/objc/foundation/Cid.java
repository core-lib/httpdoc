package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * id
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:13
 **/
public class Cid implements ObjC {

    @Override
    public String getName() {
        return "id";
    }

    @Override
    public Kind getKind() {
        return Kind.TYPEDEF;
    }

    @Override
    public Reference getReference() {
        return Reference.STRONG;
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
