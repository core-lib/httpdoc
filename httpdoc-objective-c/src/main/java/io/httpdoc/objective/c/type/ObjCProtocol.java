package io.httpdoc.objective.c.type;

import io.httpdoc.core.type.HDType;
import io.httpdoc.objective.c.ObjC;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Objective-C 协议类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 11:10
 **/
public class ObjCProtocol extends HDType implements ObjC {
    private final ObjCClass rawType;
    private final ObjCClass protocolType;

    public ObjCProtocol(ObjCClass rawType, ObjCClass protocolType) {
        this.rawType = rawType;
        this.protocolType = protocolType;
    }

    @Override
    public CharSequence getAbbrName() {
        String rawType = this.rawType.getSimpleName();
        String protocolType = this.protocolType.getSimpleName();
        return rawType + "<" + protocolType + ">" + (rawType.equals("id") ? "" : " *");
    }

    @Override
    public CharSequence getTypeName() {
        return getAbbrName();
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        imports.addAll(rawType.imports());
        imports.addAll(protocolType.imports());
        return imports;
    }

    public ObjCClass getRawType() {
        return rawType;
    }

    public ObjCClass getProtocolType() {
        return protocolType;
    }

    @Override
    public ObjCReferenceType getReferenceType() {
        return rawType.getReferenceType();
    }

}
