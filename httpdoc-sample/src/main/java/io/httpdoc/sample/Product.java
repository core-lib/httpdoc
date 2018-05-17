package io.httpdoc.sample;

import java.math.BigDecimal;

/**
 * 产品
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:19
 **/
public class Product {
    /**
     * 产品ID
     */
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品价格
     */
    private BigDecimal price;
    /**
     * 产品状态
     */
    private ProductStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
