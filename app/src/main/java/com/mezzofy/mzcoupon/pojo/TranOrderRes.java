package com.mezzofy.mzcoupon.pojo;

import java.util.List;

public class TranOrderRes {

    private String couponId;
    private String pickupDelivery;
    private String siteId;
    private Integer prodId;
    private Integer staffId;
    private String orderNo;
    private String orderDate;
    private String remark;
    private String contactNo;
    private String startendTime;
    private String touristAddress;
    private List<TranItemOrderRes> itemList;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPickupDelivery() {
        return pickupDelivery;
    }

    public void setPickupDelivery(String pickupDelivery) {
        this.pickupDelivery = pickupDelivery;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStartendTime() {
        return startendTime;
    }

    public void setStartendTime(String startendTime) {
        this.startendTime = startendTime;
    }

    public List<TranItemOrderRes> getItemList() {
        return itemList;
    }

    public void setItemList(List<TranItemOrderRes> itemList) {
        this.itemList = itemList;
    }

    public String getTouristAddress() {
        return touristAddress;
    }

    public void setTouristAddress(String touristAddress) {
        this.touristAddress = touristAddress;
    }
}
