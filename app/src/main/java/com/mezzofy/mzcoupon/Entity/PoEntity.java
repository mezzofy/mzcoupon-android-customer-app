package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

import java.util.List;

/**
 * Created by Aruna on 28/03/17.
 */

@Entity
public class PoEntity {
    @Column(name = "poId")
    private String poId;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "mechantName")
    private String mechantName;
    @Column(name = "customerId")
    private String customerId;
    @Column(name = "customerName")
    private String customerName;
    @Column(name = "poDate")
    private String poDate;
    @Column(name = "poNo")
    private String poNo;
    @Column(name = "poTotal")
    private Double poTotal;
    @Column(name = "poStatus")
    private String poStatus;
    @Column(name = "payReceipt")
    private String payReceipt;
    @Column(name = "payResponse")
    private String payResponse;
    @Column(name = "transferTo")
    private String transferTo;
    @Column(name = "transferFrom")
    private String transferFrom;
    @Column(name = "payToken")
    private String payToken;
    @Column(name = "rewardPoint")
    private Double rewardPoint;
    @Column(name = "hashCode")
    private String hashCode;
    @Column(name = "updatedOn")
    private String updatedOn;
    @Column(name = "couponType")
    private String couponType;
    @Column(name = "paymentDetailId")
    private String paymentDetailId;

    List<CustomerCouponEntity> customercoupons;

    public String getMechantName() {
        return mechantName;
    }

    public void setMechantName(String mechantName) {
        this.mechantName = mechantName;
    }

    public List<CustomerCouponEntity> getCustomercoupons() {
        return customercoupons;
    }

    public void setCustomercoupons(List<CustomerCouponEntity> customercoupons) {
        this.customercoupons = customercoupons;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(String paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public String getPayReceipt() {
        return payReceipt;
    }

    public void setPayReceipt(String payReceipt) {
        this.payReceipt = payReceipt;
    }

    public String getPayResponse() {
        return payResponse;
    }

    public void setPayResponse(String payResponse) {
        this.payResponse = payResponse;
    }

    public String getPayToken() {
        return payToken;
    }

    public void setPayToken(String payToken) {
        this.payToken = payToken;
    }


    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

    public Double getPoTotal() {
        return poTotal;
    }

    public void setPoTotal(Double poTotal) {
        this.poTotal = poTotal;
    }

    public Double getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(Double rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

 }
