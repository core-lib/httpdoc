package io.httpdoc.sample.product;

import io.httpdoc.sample.ApiResult;

/**
 * 产品创建结果
 */
public class ProductCreateResult extends ApiResult {
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
