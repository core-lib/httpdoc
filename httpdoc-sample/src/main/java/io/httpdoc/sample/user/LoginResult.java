package io.httpdoc.sample.user;

import io.httpdoc.sample.ApiResult;

public class LoginResult extends ApiResult {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
