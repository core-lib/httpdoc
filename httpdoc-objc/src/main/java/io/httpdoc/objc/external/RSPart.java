package io.httpdoc.objc.external;

import io.httpdoc.objc.ObjC;

/**
 * RSPart
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-30 10:26
 **/
public class RSPart implements ObjC {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Kind getKind() {
        return Kind.CLASS;
    }

    @Override
    public Reference getReference() {
        return Reference.STRONG;
    }

    @Override
    public String getLocation() {
        return "RSNetworking.h";
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
