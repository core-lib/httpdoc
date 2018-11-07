package io.httpdoc.sample.user;

import io.httpdoc.sample.ApiResult;

/**
 * 用户创建结果
 */
public class UserCreateResult extends ApiResult {
    /**
     * 新建用户的ID，当请求失败时该字段为null
     */
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
