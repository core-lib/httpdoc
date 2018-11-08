package io.httpdoc.sample;

import io.httpdoc.sample.user.User;

/**
 * 登陆结果
 */
public class LoginResult extends ApiResult {
    /**
     * 用户数据，登录失败时该字段为null
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
