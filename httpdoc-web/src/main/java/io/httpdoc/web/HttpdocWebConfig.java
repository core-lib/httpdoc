package io.httpdoc.web;

import io.httpdoc.core.Config;

import javax.servlet.ServletContext;

/**
 * Httpdoc web 配置
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 11:23
 **/
public interface HttpdocWebConfig extends Config {

    String getName();

    ServletContext getServletContext();

}
