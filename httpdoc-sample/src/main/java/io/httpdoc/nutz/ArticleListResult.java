package io.httpdoc.nutz;

import java.util.List;

/**
 * 文章列表结果
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:40
 **/
public class ArticleListResult {
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
