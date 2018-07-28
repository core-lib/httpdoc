package io.httpdoc.objc.type;

import io.httpdoc.objc.foundation.Metadata;
import io.httpdoc.objc.foundation.ObjC;

import java.util.Collections;
import java.util.Set;

/**
 * ObjC class 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:05
 **/
public class ObjCClass extends ObjCType {
    private final String name;
    private final int flag;

    public ObjCClass(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }

    public ObjCClass(Class<? extends ObjC> clazz) {
        Metadata metadata = clazz.getAnnotation(Metadata.class);
        this.name = metadata.name().isEmpty() ? clazz.getSimpleName() : metadata.name();
        this.flag = metadata.flag();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPrimitive() {
        return (flag & FLAG_PRIMITIVE) != 0;
    }

    @Override
    public boolean isTypedef() {
        return (flag & FLAG_TYPEDEF) != 0;
    }

    @Override
    public String getReferenceType() {
        if ((flag & FLAG_STRONG) != 0) return STRONG;
        else if ((flag & FLAG_WEAK) != 0) return WEAK;
        else if ((flag & FLAG_COPY) != 0) return COPY;
        else if ((flag & FLAG_ASSIGN) != 0) return ASSIGN;
        else return STRONG;
    }

    @Override
    public Set<String> imports() {
        if (isPrimitive()) return Collections.emptySet();
        else if (isTypedef()) return Collections.singleton("#import \"" + name + ".h\"");
        else if (isFoundation()) return Collections.singleton(FOUNDATION);
        else if (isEnum()) return Collections.singleton("#import \"" + name + ".h\"");
        else return Collections.singleton("@class " + name + ";");
    }

    public int getFlag() {
        return flag;
    }

    public boolean isEnum() {
        return (flag & FLAG_ENUM) != 0;
    }

    public boolean isFoundation() {
        return (flag & FLAG_FOUNDATION) != 0;
    }


}
