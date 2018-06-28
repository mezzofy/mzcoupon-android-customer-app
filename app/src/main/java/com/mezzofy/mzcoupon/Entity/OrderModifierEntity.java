package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 9/19/17.
 */

public class OrderModifierEntity {
    private String orderModifierId;
    private String orderItemId;
    private String modifierId;
    private String modifierName;

    public String getOrderModifierId() {
        return orderModifierId;
    }

    public void setOrderModifierId(String orderModifierId) {
        this.orderModifierId = orderModifierId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }
}
