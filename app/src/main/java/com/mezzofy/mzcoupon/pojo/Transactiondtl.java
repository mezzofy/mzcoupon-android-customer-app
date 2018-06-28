package com.mezzofy.mzcoupon.pojo;


public class Transactiondtl {

    private Integer transdtlId;
    private Integer prodId;
    private Double prodQty;
    private Double prodPrice;
    private Double prodTotal;
    private String prodName;
    private Integer transactionId;
    private String merchantName;
    private int merchantId;
//    private List<Transactioncoupon> couponList;

    public Integer getTransdtlId() {
        return transdtlId;
    }

    public void setTransdtlId(Integer transdtlId) {
        this.transdtlId = transdtlId;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Double getProdQty() {
        return prodQty;
    }

    public void setProdQty(Double prodQty) {
        this.prodQty = prodQty;
    }

    public Double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(Double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public Double getProdTotal() {
        return prodTotal;
    }

    public void setProdTotal(Double prodTotal) {
        this.prodTotal = prodTotal;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

//    public List<Transactioncoupon> getCouponList() {
//        return couponList;
//    }
//
//    public void setCouponList(List<Transactioncoupon> couponList) {
//        this.couponList = couponList;
//    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }
}
