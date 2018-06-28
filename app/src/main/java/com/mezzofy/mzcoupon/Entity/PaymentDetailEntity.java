package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by aruna on 6/29/17.
 */

@Entity
public class PaymentDetailEntity {
    @Column(name = "paymentDetailId")
    private String paymentDetailId;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "paymentId")
    private String paymentId;
    @Column(name = "paymentName")
    private String paymentName;
    @Column(name = "paymentLogourl")
    private String paymentLogourl;
    @Column(name = "paymentMerchantId")
    private String paymentMerchantId;
    @Column(name = "paymentKey")
    private String paymentKey;
    @Column(name = "paymentToken")
    private String paymentToken;
    @Column(name = "paymentUrl")
    private String paymentUrl;
    @Column(name = "paymentStatus")
    private String paymentStatus;
    @Column(name = "hashCode")
    private String hashCode;
    @Column(name = "updatedOn")
    private String updatedOn;
    @Column(name = "paymentType")
    private String paymentType;
    @Column(name = "paymentEnviornment")
    private String paymentEnviornment;

    @Column(name = "currency")
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentEnviornment() {
        return paymentEnviornment;
    }

    public void setPaymentEnviornment(String paymentEnviornment) {
        this.paymentEnviornment = paymentEnviornment;
    }

    public String getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(String paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentLogourl() {
        return paymentLogourl;
    }

    public void setPaymentLogourl(String paymentLogourl) {
        this.paymentLogourl = paymentLogourl;
    }

    public String getPaymentMerchantId() {
        return paymentMerchantId;
    }

    public void setPaymentMerchantId(String paymentMerchantId) {
        this.paymentMerchantId = paymentMerchantId;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
