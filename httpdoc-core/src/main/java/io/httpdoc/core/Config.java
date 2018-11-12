package io.httpdoc.core;

import java.util.Enumeration;

/**
 * 配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/12
 */
public interface Config {

    String getInitParameter(String name);

    Enumeration<String> getInitParameterNames();

}
