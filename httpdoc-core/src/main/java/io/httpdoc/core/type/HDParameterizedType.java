package io.httpdoc.core.type;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数化类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 11:14
 **/
public class HDParameterizedType extends HDType {
    private HDClass rawType;
    private HDType ownerType;
    private HDType[] actualTypeArguments;

    HDParameterizedType() {
    }

    public HDParameterizedType(HDClass rawType, HDType ownerType, HDType[] actualTypeArguments) {
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.actualTypeArguments = actualTypeArguments;
    }

    public HDParameterizedType(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
        this.rawType = HDType.valueOf(rawType);
        this.ownerType = HDType.valueOf(ownerType);
        this.actualTypeArguments = HDType.valuesOf(actualTypeArguments);
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getFormatName());
        for (int i = 0; actualTypeArguments != null && i < actualTypeArguments.length; i++) {
            if (i == 0) builder.append("<");
            else builder.append(", ");
            builder.append(actualTypeArguments[i] instanceof HDTypeVariable ? ((HDTypeVariable) actualTypeArguments[i]).getName() : actualTypeArguments[i].getFormatName());
            if (i == actualTypeArguments.length - 1) builder.append(">");
        }
        return builder;
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>(rawType.imports());
        for (HDType actualTypeArgument : actualTypeArguments) imports.addAll(actualTypeArgument.imports());
        return imports;
    }

    public HDClass getRawType() {
        return rawType;
    }

    void setRawType(HDClass rawType) {
        this.rawType = rawType;
    }

    public HDType getOwnerType() {
        return ownerType;
    }

    void setOwnerType(HDType ownerType) {
        this.ownerType = ownerType;
    }

    public HDType[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    void setActualTypeArguments(HDType[] actualTypeArguments) {
        this.actualTypeArguments = actualTypeArguments;
    }
}
