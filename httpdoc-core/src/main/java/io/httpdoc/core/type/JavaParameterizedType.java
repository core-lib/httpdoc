package io.httpdoc.core.type;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数化类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 11:14
 **/
public class JavaParameterizedType extends JavaType {
    private JavaType[] actualTypeArguments;
    private final JavaType rawType;
    private final JavaType ownerType;

    public JavaParameterizedType(JavaType rawType, JavaType ownerType) {
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getFormatName());
        for (int i = 0; actualTypeArguments != null && i < actualTypeArguments.length; i++) {
            if (i == 0) builder.append("<");
            else builder.append(", ");
            builder.append(actualTypeArguments[i] instanceof JavaTypeVariable ? ((JavaTypeVariable) actualTypeArguments[i]).getName() : actualTypeArguments[i].getFormatName());
            if (i == actualTypeArguments.length - 1) builder.append(">");
        }
        return builder;
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>();
        imports.addAll(rawType.imports());
        for (JavaType actualTypeArgument : actualTypeArguments) imports.addAll(actualTypeArgument.imports());
        return imports;
    }

    public JavaType[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    void setActualTypeArguments(JavaType[] actualTypeArguments) {
        this.actualTypeArguments = actualTypeArguments;
    }

    public JavaType getRawType() {
        return rawType;
    }

    public JavaType getOwnerType() {
        return ownerType;
    }

}
