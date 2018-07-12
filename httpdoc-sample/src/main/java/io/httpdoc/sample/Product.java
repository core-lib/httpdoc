package io.httpdoc.sample;

import io.httpdoc.core.annotation.Alias;
import io.httpdoc.core.annotation.Package;

import java.math.BigDecimal;

/**
 * 产品
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 12:19
 **/
@Package("io.httpdoc.test2")
public class Product {
    private boolean yon;
    private byte b;
    private short s;
    private char c;
    private int i;
    private float f;
    private long l;
    private double d;

    private byte[][] bytes;
    private int[] ints;

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

    public boolean isYon() {
        return yon;
    }

    public void setYon(boolean yon) {
        this.yon = yon;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    @Alias("ID")
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

    public byte[][] getBytes() {
        return bytes;
    }

    public void setBytes(byte[][] bytes) {
        this.bytes = bytes;
    }

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }
}
