package io.httpdoc.sample.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品模型
 */
public class Product {
    /**
     * 产品ID
     */
    private Long id;
    /**
     * 货号
     */
    private String number;
    /**
     * 名称
     */
    private String name;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 品牌
     */
    private Brand brand;
    /**
     * SKU列表
     *
     * @style table
     */
    private List<Sku> skus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
