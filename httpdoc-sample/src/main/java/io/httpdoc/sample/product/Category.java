package io.httpdoc.sample.product;

/**
 * 类别
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/16
 */
public class Category {
    /**
     * 类别ID
     */
    private Long id;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 父类别，自引用/递归属性
     */
    private Category parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
