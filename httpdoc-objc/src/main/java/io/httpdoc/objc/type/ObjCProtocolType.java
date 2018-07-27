package io.httpdoc.objc.type;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ObjC 协议类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:18
 **/
public class ObjCProtocolType extends ObjCType {
    private final ObjCClass rawType;
    private final ObjCClass[] protocols;

    public ObjCProtocolType(ObjCClass rawType, ObjCClass... protocols) {
        this.rawType = rawType;
        this.protocols = protocols;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getName());
        builder.append("<");
        for (int i = 0; i < protocols.length; i++) {
            if (i > 0) builder.append(", ");
            ObjCClass actualTypeArgument = protocols[i];
            builder.append(actualTypeArgument.getName());
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public boolean isPrimitive() {
        return rawType.isPrimitive();
    }

    @Override
    public String getReferenceType() {
        return rawType.getReferenceType();
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        imports.addAll(rawType.imports());
        for (ObjCType type : protocols) imports.addAll(type.imports());
        return imports;
    }

    public ObjCClass getRawType() {
        return rawType;
    }

    public ObjCClass[] getProtocols() {
        return protocols;
    }

}
