package io.httpdoc.core.translation;

import java.util.Enumeration;
import java.util.Map;

/**
 * 应用容器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 15:56
 **/
public interface Container {

    Object get(String name);

    <T> Map<String, T> get(Class<T> type);

    Enumeration<String> names();

    void remove(String name);

    void set(String name, Object value);

}
