package io.httpdoc.nutz;

import java.util.List;

/**
 * 文章
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:39
 **/
public class Article {
    private User author;
    private String title;
    private String content;
    private List<Comment> comments;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
