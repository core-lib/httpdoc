package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 重定义翻译包名
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-29 15:05
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Package {
}
