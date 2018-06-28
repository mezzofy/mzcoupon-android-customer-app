package com.mezzofy.mzcoupon.pojo;

import java.util.List;

public class TranItemOrderRes {

    private String couponId;
    private Integer groupId;
    private Integer itemId;
    private String itemName;
    private String groupName;
    private List<OrderModifierRes> modifierList;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public List<OrderModifierRes> getModifierList() {
        return modifierList;
    }

    public void setModifierList(List<OrderModifierRes> modifierList) {
        this.modifierList = modifierList;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
