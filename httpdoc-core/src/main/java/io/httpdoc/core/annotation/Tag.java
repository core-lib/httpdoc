package io.httpdoc.core.annotation;

import java.lang.annotation.*;

/**
 * 所属标签
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-12 14:36
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tag {

    /**
     * 如果放在方法上则表明是否覆盖Controller的标签
     *
     * @return 是否覆盖
     */
    boolean override() default true;

    /**
     * 所属标签列表
     *
     * @return 所属标签列表
     */
    String[] value();

}
