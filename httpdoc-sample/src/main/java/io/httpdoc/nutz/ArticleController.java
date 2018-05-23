package io.httpdoc.nutz;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.PUT;

/**
 * 文章管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:38
 **/
@At("/articles")
public class ArticleController {

    @At("/?/?")
    public ArticleListResult list(int page, int size) {
        ArticleListResult result = new ArticleListResult();

        return result;
    }

    @At("/")
    @PUT
    @POST
    public ArticleCreateResult create(Article article) {
        ArticleCreateResult result = new ArticleCreateResult();

        return result;
    }

}
