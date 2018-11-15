package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 样式
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-12 14:42
 **/
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Style {

    /**
     * 样式：normal|table|grid
     *
     * @return 样式
     */
    String value();

}
