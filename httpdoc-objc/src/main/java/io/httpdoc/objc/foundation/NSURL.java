package io.httpdoc.objc.foundation;

import io.httpdoc.objc.ObjC;

public class NSURL implements ObjC {

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
