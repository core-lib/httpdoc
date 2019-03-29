package io.httpdoc.objc;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.objc.fragment.ObjCParameterFragment;

import java.io.IOException;
import java.util.Set;

/**
 * Selector 命名策略
 *
 * @author Payne 646742615@qq.com
 * 2019/3/29 15:37
 */
public interface ObjCSELNamingStrategy {

    /**
     * 将方法命名并拼接到拼接器上
     *
     * @param appender           拼接器
     * @param preference         偏好设置
     * @param name               方法名
     * @param parameterFragments 参数碎片
     * @param <T>                拼接器类型
     * @throws IOException I/O 异常
     */
    <T extends LineAppender<T>> void joinTo(T appender, Preference preference, String name, Set<ObjCParameterFragment> parameterFragments) throws IOException;

}
