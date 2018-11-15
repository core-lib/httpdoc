package io.httpdoc.sample.product;

/**
 * SKU
 */
public class Sku {
    /**
     * 条码
     */
    private String barcode;
    /**
     * 颜色
     */
    private String color;
    /**
     * 尺码
     */
    private String size;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
