package io.httpdoc.objc.type;

import io.httpdoc.core.Importable;
import io.httpdoc.objc.ObjC;

import java.util.Collections;
import java.util.Set;

/**
 * ObjC class 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:05
 **/
public class ObjCClass extends ObjCType implements Importable, Comparable<ObjCClass> {
    private final String name;
    private final Kind kind;
    private final Reference reference;
    private final String location;
    private final boolean external;

    public ObjCClass(String name, Kind kind, Reference reference, String location) {
        this(name, kind, reference, location, false);
    }

    public ObjCClass(String name, Kind kind, Reference reference, String location, boolean external) {
        this.name = name;
        this.kind = kind;
        this.reference = reference;
        this.location = location;
        this.external = external;
    }

    public ObjCClass(Class<? extends ObjC> objCClass) {
        try {
            ObjC objC = objCClass.newInstance();
            this.name = objC.getName();
            this.kind = objC.getKind();
            this.reference = objC.getReference();
            this.location = objC.getLocation();
            this.external = objC.isExternal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public Reference getReference() {
        return reference;
    }

    @Override
    public String getLocation() {
        return location.startsWith("<") && location.endsWith(">")
                || location.startsWith("\"") && location.endsWith("\"")
                ? location : "\"" + location + "\"";
    }

    @Override
    public boolean isExternal() {
        return external;
    }

    @Override
    public Set<ObjCClass> dependencies() {
        return Collections.singleton(this);
    }

    @Override
    public Set<String> imports() {
        switch (kind) {
            case CLASS:
                return external ? Collections.singleton("#import " + getLocation()) : Collections.singleton("@class " + name + ";");
            case PROTOCOL:
                return external ? Collections.singleton("#import " + getLocation()) : Collections.singleton("@protocol " + name + ";");
            case PRIMITIVE:
                return Collections.singleton("#import " + getLocation());
            case TYPEDEF:
                return Collections.singleton("#import " + getLocation());
            default:
                return Collections.emptySet();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCClass objCClass = (ObjCClass) o;

        return name != null ? name.equals(objCClass.name) : objCClass.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(ObjCClass that) {
        return this.name.compareTo(that.name);
    }

}
