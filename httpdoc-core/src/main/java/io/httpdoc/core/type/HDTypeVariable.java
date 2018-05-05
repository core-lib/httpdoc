package io.httpdoc.core.type;

import java.util.List;

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
    public List<String> imports() {
        return null;
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
}
