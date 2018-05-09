package io.httpdoc.sample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 产品
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:19
 **/
public class Product<T extends Sample> {
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

    private T[] ts;

    private List<T> list;
    private List<T[][]> lists;

    private Map<String, T> map;
    private Map<String, T[]> maps;

    private List<?> numbers;

    public List<?> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<?> numbers) {
        this.numbers = numbers;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T[][]> getLists() {
        return lists;
    }

    public void setLists(List<T[][]> lists) {
        this.lists = lists;
    }

    public Map<String, T> getMap() {
        return map;
    }

    public void setMap(Map<String, T> map) {
        this.map = map;
    }

    public Map<String, T[]> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, T[]> maps) {
        this.maps = maps;
    }

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

    public T[] getTs() {
        return ts;
    }

    public void setTs(T[] ts) {
        this.ts = ts;
    }
}
