package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 9/19/17.
 */

public class OrderItemmEntity {
    private OrderItemEntity orderitem;
    private List<OrderModifiermEntity> ordermodifiers;

    public OrderItemEntity getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(OrderItemEntity orderitem) {
        this.orderitem = orderitem;
    }

    public List<OrderModifiermEntity> getOrdermodifiers() {
        return ordermodifiers;
    }

    public void setOrdermodifiers(List<OrderModifiermEntity> ordermodifiers) {
        this.ordermodifiers = ordermodifiers;
    }
}
