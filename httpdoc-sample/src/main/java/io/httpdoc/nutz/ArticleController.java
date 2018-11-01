package io.httpdoc.nutz;

import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

/**
 * 文章管理器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:38
 **/
@At("/articles")
public class ArticleController {

    @POST
    @At("/?/?")
    public ArticleListResult list(int page, int size, @ReqHeader("name") String name, @Param("article") String article, @Param("article2") String article2) {
        ArticleListResult result = new ArticleListResult();

        return result;
    }

    @At("/")
    @POST
    @AdaptBy(type = JsonAdaptor.class)
    public ArticleCreateResult create(@Param("..") Article article) {
        ArticleCreateResult result = new ArticleCreateResult();

        return result;
    }

}
