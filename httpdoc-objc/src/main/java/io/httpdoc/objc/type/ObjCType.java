package io.httpdoc.objc.type;

import io.httpdoc.core.Importable;

/**
 * ObjC 类型父类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 11:36
 **/
public abstract class ObjCType implements Importable {

    public abstract String getName();

    public abstract boolean isPrimitive();

}
