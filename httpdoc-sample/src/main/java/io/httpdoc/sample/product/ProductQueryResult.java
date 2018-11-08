package io.httpdoc.sample.product;

import io.httpdoc.sample.ApiResult;

/**
 * 产品查询结果
 */
public class ProductQueryResult extends ApiResult {
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
