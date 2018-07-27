package io.httpdoc.objc;

import io.httpdoc.core.Importable;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * 协议
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 14:49
 **/
public class ObjCProtocol implements Importable {
    private String name;
    private Set<String> imports = new TreeSet<>();

    public ObjCProtocol(String name, String... imports) {
        this.name = name;
        this.imports.addAll(Arrays.asList(imports));
    }

    @Override
    public Set<String> imports() {
        return imports;
    }

    public String getName() {
        return name;
    }

    public ObjCProtocol setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getImports() {
        return imports;
    }

    public ObjCProtocol setImports(Set<String> imports) {
        this.imports = imports;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjCProtocol that = (ObjCProtocol) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
