package io.httpdoc.objc.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ObjC 类型注解
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 15:58
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjC {

    boolean primitive();

    String name();

}
