package io.httpdoc.sample;

import io.httpdoc.sample.user.User;

public class LoginResult extends ApiResult {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
