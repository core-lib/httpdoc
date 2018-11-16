package io.httpdoc.sample.order;

import java.util.List;

/**
 * 订单
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/16
 * @style normal
 */
public class Order {
    /**
     * 用户ID
     *
     * @order 1
     */
    private String userId;
    /**
     * 收货地址
     *
     * @order 2
     */
    private String address;
    /**
     * 电话
     *
     * @order 3
     */
    private String telephone;
    /**
     * 订单项
     *
     * @order 4
     * @style table
     */
    private List<OrderItem> items;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
