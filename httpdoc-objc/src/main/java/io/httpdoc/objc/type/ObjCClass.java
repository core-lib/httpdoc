package io.httpdoc.objc.type;

import io.httpdoc.objc.foundation.Foundation;
import io.httpdoc.objc.foundation.ObjC;

import java.util.Set;

/**
 * ObjC class 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:05
 **/
public class ObjCClass extends ObjCType {
    private final String name;
    private final boolean primitive;

    public ObjCClass(String name, boolean primitive) {
        this.name = name;
        this.primitive = primitive;
    }

    public ObjCClass(Class<? extends Foundation> clazz) {
        ObjC objC = clazz.getAnnotation(ObjC.class);
        this.name = objC.name();
        this.primitive = objC.primitive();
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    @Override
    public Set<String> imports() {
        return super.imports();
    }

}
