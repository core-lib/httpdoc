package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 序号，序号越小越在前面
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/7
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {

    /**
     * @return 序号
     */
    int value();

}
