package io.httpdoc.sample.order;

import io.httpdoc.sample.ApiResult;

import java.math.BigDecimal;

/**
 * 订单创建结果
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/16
 */
public class OrderCreateResult extends ApiResult {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 总金额
     */
    private BigDecimal amount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
