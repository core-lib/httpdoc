package io.httpdoc.core;

/**
 * 容器属性
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 15:59
 **/
public final class Attribute {
    private final String name;
    private final Object value;

    public Attribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        return name != null ? name.equals(attribute.name) : attribute.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{" + name + " : " + value + "}";
    }
}
