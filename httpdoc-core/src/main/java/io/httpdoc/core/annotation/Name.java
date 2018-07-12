package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 重定义翻译名称
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-29 15:03
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Name {

    String value();

}
