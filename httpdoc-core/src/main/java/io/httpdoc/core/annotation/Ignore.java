package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 翻译时忽略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-29 15:05
 **/
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore {
}
