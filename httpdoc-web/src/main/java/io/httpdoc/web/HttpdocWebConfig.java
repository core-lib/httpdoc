package io.httpdoc.web;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Httpdoc web 配置
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 11:23
 **/
public interface HttpdocWebConfig {

    String getName();

    ServletContext getServletContext();

    String getInitParameter(String name);

    Enumeration<String> getInitParameterNames();

}
