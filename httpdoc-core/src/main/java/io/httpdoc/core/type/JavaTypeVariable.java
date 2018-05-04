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
    private final JavaType[] bounds;

    public JavaTypeVariable(String name) {
        this(name, new JavaType[0]);
    }

    public JavaTypeVariable(String name, JavaType[] bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (int i = 0; bounds != null && i < bounds.length; i++) {
            if (i == 0) builder.append(" extends ");
            else builder.append(", ");
            builder.append(bounds[i].getFormatName());
        }
        return builder;
    }

    @Override
    public List<String> imports() {
        return null;
    }

    public String getName() {
        return name;
    }

    public JavaType[] getBounds() {
        return bounds;
    }
}
