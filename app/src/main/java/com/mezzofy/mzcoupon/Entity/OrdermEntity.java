package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 9/19/17.
 */

public class OrdermEntity {
    private OrderEntity order;
    private List<OrderItemmEntity> orderitems;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public List<OrderItemmEntity> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<OrderItemmEntity> orderitems) {
        this.orderitems = orderitems;
    }
}
