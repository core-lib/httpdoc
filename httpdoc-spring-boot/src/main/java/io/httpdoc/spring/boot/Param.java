package io.httpdoc.spring.boot;

import java.lang.annotation.*;

/**
 * Filter Init Parameter
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 10:39
 **/
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Param {

    /**
     * @return Filter Init Parameter Name
     */
    String name();

    /**
     * @return Filter Init Parameter Value
     */
    String value();

}
