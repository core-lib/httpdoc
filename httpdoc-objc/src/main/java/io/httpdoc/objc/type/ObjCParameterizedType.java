package io.httpdoc.objc.type;

import java.util.Set;

/**
 * ObjCConstant 参数化类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 15:48
 **/
public class ObjCParameterizedType extends ObjCType {


    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Set<String> imports() {
        return null;
    }
}
