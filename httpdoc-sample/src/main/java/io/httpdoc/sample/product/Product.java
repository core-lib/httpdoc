package io.httpdoc.sample.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品模型
 *
 * @style grid
 */
public class Product {
    /**
     * 产品ID
     *
     * @order 1
     */
    private Long id;
    /**
     * 货号
     *
     * @order 2
     */
    private String number;
    /**
     * 名称
     *
     * @order 3
     */
    private String name;
    /**
     * 价格
     *
     * @order 4
     */
    private BigDecimal price;
    /**
     * 品牌
     *
     * @order 5
     */
    private Brand brand;
    /**
     * 类别
     * @order 6
     */
    private Category category;
    /**
     * SKU列表
     *
     * @order 7
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
