package io.httpdoc.core.type;

import java.util.List;

/**
 * 类型变量
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 16:34
 **/
public class JavaTypeVariable extends JavaType {
    private final String name;
    private JavaType bound;

    JavaTypeVariable(String name) {
        this.name = name;
    }

    public JavaTypeVariable(String name, JavaType bound) {
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
    public List<String> imports() {
        return null;
    }

    public String getName() {
        return name;
    }

    public JavaType getBound() {
        return bound;
    }

    void setBound(JavaType bound) {
        this.bound = bound;
    }
}
