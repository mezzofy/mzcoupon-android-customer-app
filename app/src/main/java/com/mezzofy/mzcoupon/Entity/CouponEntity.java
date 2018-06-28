package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by aruna on 8/18/17.
 */

@Entity
public class CouponEntity {
    @Column(name = "couponId")
    private String couponId;
    @Column(name = "productId")
    private String productId;
    @Column(name = "allocationId")
    private String allocationId;
    @Column(name = "campaignId")
    private String campaignId;
    @Column(name = "customerId")
    private String customerId;
    @Column(name = "campaignCode")
    private String campaignCode;
    @Column(name = "couponNo")
    private String couponNo;
    @Column(name = "purchaseDate")
    private String purchaseDate;
    @Column(name = "redeemDate")
    private String redeemDate;
    @Column(name = "startDate")
    private String startDate;
    @Column(name = "couponName")
    private String couponName;
    @Column(name = "endDate")
    private String endDate;
    @Column(name = "couponStatus")
    private String couponStatus;
    @Column(name = "hashCode")
    private String hashCode;
    @Column(name = "createdOn")
    private String createdOn;
    @Column(name = "updatedOn")
    private String updatedOn;
    @Column(name = "productNote1")
    private String productNote1;
    @Column(name = "productNote2")
    private String productNote2;
    @Column(name = "productNote3")
    private String productNote3;
    @Column(name = "sellingPrice")
    private Float sellingPrice;
    @Column(name = "orginalPrice")
    private Float orginalPrice;
    @Column(name = "productDesc")
    private String productDesc;
    @Column(name = "productImageurl")
    private String productImageurl;


    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getProductNote1() {
        return productNote1;
    }

    public void setProductNote1(String productNote1) {
        this.productNote1 = productNote1;
    }

    public String getProductNote2() {
        return productNote2;
    }

    public void setProductNote2(String productNote2) {
        this.productNote2 = productNote2;
    }

    public String getProductNote3() {
        return productNote3;
    }

    public void setProductNote3(String productNote3) {
        this.productNote3 = productNote3;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Float getOrginalPrice() {
        return orginalPrice;
    }

    public void setOrginalPrice(Float orginalPrice) {
        this.orginalPrice = orginalPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImageurl() {
        return productImageurl;
    }

    public void setProductImageurl(String productImageurl) {
        this.productImageurl = productImageurl;
    }
}
