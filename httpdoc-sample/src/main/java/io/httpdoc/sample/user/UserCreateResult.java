package io.httpdoc.sample.user;

import io.httpdoc.sample.ApiResult;

/**
 * 用户创建结果
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/5
 */
public class UserCreateResult extends ApiResult {
    /**
     * 用户ID，当请求失败时该字段为null
     */
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
