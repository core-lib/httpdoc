package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 别名, 为了适配不同语言之间有些关键字不同的问题, 通过别名和JSON KEY转换
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-12 14:42
 **/
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias {

    String value();

}
