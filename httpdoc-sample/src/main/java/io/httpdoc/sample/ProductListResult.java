package io.httpdoc.sample;

public class ProductListResult {
    private Product<Sample>[] products = new Product[10];

    public Product<Sample>[] getProducts() {
        return products;
    }

    public void setProducts(Product<Sample>[] products) {
        this.products = products;
    }
}
