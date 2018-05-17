package io.httpdoc.sample;

import java.util.ArrayList;
import java.util.List;

public class ProductListResult {
    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
