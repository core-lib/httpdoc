package io.httpdoc.sample.order;

/**
 * 订单项
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/16
 */
public class OrderItem {
    /**
     * SKU条码
     */
    private String barcode;
    /**
     * 数量
     */
    private int quantity;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
