package io.httpdoc.nutz;

/**
 * 评论
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:39
 **/
public class Comment {
    private User user;
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
