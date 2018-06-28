package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 9/19/17.
 */

public class OrderListmEntity {
    private List<OrdermEntity> orders;
    private SizemEnity size;

    public List<OrdermEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdermEntity> orders) {
        this.orders = orders;
    }

    public SizemEnity getSize() {
        return size;
    }

    public void setSize(SizemEnity size) {
        this.size = size;
    }
}
