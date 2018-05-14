package io.httpdoc.core.type;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 类型变量
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 16:34
 **/
public class HDTypeVariable extends HDType {
    private final String name;
    private HDType bound;

    HDTypeVariable(String name) {
        this.name = name;
    }

    public HDTypeVariable(String name, HDType bound) {
        this.name = name;
        this.bound = bound;
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        if (bound != null) builder.append(" extends ").append(bound.getFormatName());
        return builder;
    }

    @Override
    public Set<String> imports() {
        return bound != null ? bound.imports() : Collections.<String>emptySet();
    }

    public String getName() {
        return name;
    }

    public HDType getBound() {
        return bound;
    }

    void setBound(HDType bound) {
        this.bound = bound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HDTypeVariable that = (HDTypeVariable) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return bound != null ? bound.equals(that.bound) : that.bound == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (bound != null ? bound.hashCode() : 0);
        return result;
    }

}
