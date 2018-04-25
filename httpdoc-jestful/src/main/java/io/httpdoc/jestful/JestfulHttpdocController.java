package io.httpdoc.jestful;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jestful Server 服务
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 16:23
 **/
@HTTP("/jestful")
@Controller
public class JestfulHttpdocController {

    @GET("/httpdoc")
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @GET("/httpdoc.{suffix:.*}")
    public void get(@Path("suffix") String suffix, HttpServletRequest request, HttpServletResponse response) {

    }

}
