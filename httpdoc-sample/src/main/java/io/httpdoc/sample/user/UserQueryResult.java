package io.httpdoc.sample.user;

import io.httpdoc.sample.ApiResult;

import java.util.List;

/**
 * 用户分页查询结果
 */
public class UserQueryResult extends ApiResult {
    /**
     * 总条数
     */
    private int total;
    /**
     * 当前页面用户列表
     */
    private List<User> users;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
