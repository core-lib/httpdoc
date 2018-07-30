package io.httpdoc.nutz;

/**
 * 评论
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-21 14:39
 **/
public class Comment {
    private Customer customer;
    private String content;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
