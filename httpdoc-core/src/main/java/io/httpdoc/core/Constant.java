package io.httpdoc.core;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-13 14:16
 **/
public class Constant extends Definition {
    private static final long serialVersionUID = 852426625919743178L;

    private String name;

    public Constant() {
    }

    public Constant(String name) {
        this.name = name;
    }

    public Constant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constant constant = (Constant) o;

        return name != null ? name.equals(constant.name) : constant.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
