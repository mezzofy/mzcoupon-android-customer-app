package com.mezzofy.mzcoupon.pojo;

import java.util.List;


public class TransactionRes {

    private Integer transactionId;
    private String transactionNo;
    private Double transactionTotal;
    private String customerId;
    private String placeId;
    private String transactionDate;
    private String hashCode;
    private String createdOn;
    private String status;
    private String payReceipt;
    private String payResponse;
    private String couponType;
    private String transferId;
    private String transferRemark;
    private List<Transactiondtl> transdtls = null;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Double getTransactionTotal() {
        return transactionTotal;
    }

    public void setTransactionTotal(Double transactionTotal) {
        this.transactionTotal = transactionTotal;
    }


    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
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

    public List<Transactiondtl> getTransdtls() {
        return transdtls;
    }

    public void setTransdtls(List<Transactiondtl> transdtls) {
        this.transdtls = transdtls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferRemark() {
        return transferRemark;
    }

    public void setTransferRemark(String transferRemark) {
        this.transferRemark = transferRemark;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
