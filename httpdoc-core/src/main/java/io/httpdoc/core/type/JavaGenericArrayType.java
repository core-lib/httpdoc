package io.httpdoc.core.type;

import java.util.List;

/**
 * 泛型数组类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 14:57
 **/
public class JavaGenericArrayType extends JavaType {
    private final JavaType genericComponentType;

    public JavaGenericArrayType(JavaType genericComponentType) {
        this.genericComponentType = genericComponentType;
    }

    @Override
    public CharSequence getFormatName() {
        return genericComponentType.getFormatName() + "[]";
    }

    @Override
    public List<String> imports() {
        return genericComponentType.imports();
    }

    public JavaType getGenericComponentType() {
        return genericComponentType;
    }

}
