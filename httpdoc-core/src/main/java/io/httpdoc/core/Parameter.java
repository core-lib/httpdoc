package io.httpdoc.core;

/**
 * 资源操作符形式参数
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:41
 **/
public class Parameter extends Definition {
    private static final long serialVersionUID = 8199679343694443326L;

    private String name;
    private String scope; // query body header path
    private Schema type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Schema getType() {
        return type;
    }

    public void setType(Schema type) {
        this.type = type;
    }

}
