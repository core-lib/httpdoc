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
    private JavaType rawType;
    private JavaType ownerType;
    private JavaType[] actualTypeArguments;

    JavaParameterizedType() {
    }

    public JavaParameterizedType(JavaType rawType, JavaType ownerType, JavaType[] actualTypeArguments) {
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.actualTypeArguments = actualTypeArguments;
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
        List<String> imports = new ArrayList<>(rawType.imports());
        for (JavaType actualTypeArgument : actualTypeArguments) imports.addAll(actualTypeArgument.imports());
        return imports;
    }

    public JavaType getRawType() {
        return rawType;
    }

    void setRawType(JavaType rawType) {
        this.rawType = rawType;
    }

    public JavaType getOwnerType() {
        return ownerType;
    }

    void setOwnerType(JavaType ownerType) {
        this.ownerType = ownerType;
    }

    public JavaType[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    void setActualTypeArguments(JavaType[] actualTypeArguments) {
        this.actualTypeArguments = actualTypeArguments;
    }
}
