package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 跳过
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/6
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Skip {
}
