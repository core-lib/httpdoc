package io.httpdoc.nutz;

import org.nutz.mvc.annotation.At;

import javax.servlet.http.HttpServletRequest;

/**
 * 文章管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:38
 **/
@At("/articles")
public class ArticleController {

    @At("/?/?")
    public ArticleListResult list(HttpServletRequest request, int page, int size) {
        ArticleListResult result = new ArticleListResult();

        return result;
    }

}
