package com.mezzofy.mzcoupon.pojo;

/**
 * Created by LENOVO on 12/06/2015.
 */
public class CouponRes {

    private int couponCount;
    private String hashCode;
    private String merchantName;
    private String productDesc;
    private int productId;
    private String productImage;
    private String productName;
    private String status;
    private int qty;
    private int redemptionQuota;
    private String dailyLimitType;
    private String expirydue;
    private String expiryname;

    public String getExpiryname() {
        return expiryname;
    }

    public void setExpiryname(String expiryname) {
        this.expiryname = expiryname;
    }


    public String getExpirydue() {
        return expirydue;
    }

    public void setExpirydue(String expirydue) {
        this.expirydue = expirydue;
    }

    public String getDailyLimitType() {
        return dailyLimitType;
    }

    public void setDailyLimitType(String dailyLimitType) {
        this.dailyLimitType = dailyLimitType;
    }

    public int getRedemptionQuota() {
        return redemptionQuota;
    }

    public void setRedemptionQuota(int redemptionQuota) {
        this.redemptionQuota = redemptionQuota;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
