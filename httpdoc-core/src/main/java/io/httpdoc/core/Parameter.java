package io.httpdoc.core;

/**
 * 资源操作符形式参数
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:41
 **/
public class Parameter {
    private int index;
    private String name;
    private String scope;
    private Schema schema;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

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

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
