package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by Aruna on 07/02/17.
 */

@Entity
public class MerchantEntity {
    @Column(name = "countryCode")
    private String countryCode;
    @Column(name = "CountryName")
    private String countryName;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "merchantCode")
    private String merchantCode;
    @Column(name = "merchantType")
    private String merchantType;
    @Column(name = "merchantName")
    private String merchantName;
    @Column(name = "merchantDesc")
    private String merchantDesc;
    @Column(name = "merchantLogourl")
    private String merchantLogourl;
    @Column(name = "merchantImageurl")
    private String merchantImageurl;
    @Column(name = "merchantEmail")
    private String merchantEmail;
    @Column(name = "merchantHotline")
    private String merchantHotline;
    @Column(name = "merchantTc")
    private String merchantTc;
    @Column(name = "merchantStatus")
    private String merchantStatus;
    @Column(name = "merchantTimezone")
    private String merchantTimezone;
    @Column(name = "hashCode")
    private String hashCode;
    @Column(name = "profileStatus")
    private String profileStatus;
    @Column(name = "channelCode")
    private String channelCode;

    @Column(name = "currency")
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }


    public String getMerchantHotline() {
        return merchantHotline;
    }

    public void setMerchantHotline(String merchantHotline) {
        this.merchantHotline = merchantHotline;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantDesc() {
        return merchantDesc;
    }

    public void setMerchantDesc(String merchantDesc) {
        this.merchantDesc = merchantDesc;
    }

    public String getMerchantLogourl() {
        return merchantLogourl;
    }

    public void setMerchantLogourl(String merchantLogourl) {
        this.merchantLogourl = merchantLogourl;
    }

    public String getMerchantImageurl() {
        return merchantImageurl;
    }

    public void setMerchantImageurl(String merchantImageurl) {
        this.merchantImageurl = merchantImageurl;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantTc() {
        return merchantTc;
    }

    public void setMerchantTc(String merchantTc) {
        this.merchantTc = merchantTc;
    }

    public String getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(String merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public String getMerchantTimezone() {
        return merchantTimezone;
    }

    public void setMerchantTimezone(String merchantTimezone) {
        this.merchantTimezone = merchantTimezone;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }




}
