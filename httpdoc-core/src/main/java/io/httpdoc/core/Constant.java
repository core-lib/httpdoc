package io.httpdoc.core;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-13 14:16
 **/
public class Constant {
    private String name;
    private String comment;

    public Constant() {
    }

    public Constant(String name) {
        this.name = name;
    }

    public Constant(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
