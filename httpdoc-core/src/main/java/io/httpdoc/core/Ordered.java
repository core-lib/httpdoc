package io.httpdoc.core;

/**
 * 排序的
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/7
 */
public interface Ordered<T extends Ordered<T>> extends Comparable<T> {

    int getOrder();

    void setOrder(int order);

}
