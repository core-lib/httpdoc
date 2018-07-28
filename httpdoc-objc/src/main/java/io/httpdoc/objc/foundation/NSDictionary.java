package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

/**
 * NSDictionary
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:12
 **/
public class NSDictionary implements ObjC {

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
        return Reference.COPY;
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
