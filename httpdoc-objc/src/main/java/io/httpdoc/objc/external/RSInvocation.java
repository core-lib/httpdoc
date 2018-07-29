package io.httpdoc.objc.external;

import io.httpdoc.objc.ObjC;

public class RSInvocation implements ObjC {
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
