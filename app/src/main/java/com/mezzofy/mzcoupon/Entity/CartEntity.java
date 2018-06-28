package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by aruna on 9/25/17.
 */
@Entity
public class CartEntity {
    @Column(name = "campaignId")
    private String campaignId;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "orginalPrice")
    private String orginalPrice;
    @Column(name = "campaignCode")
    private String campaignCode;
    @Column(name = "campaignName")
    private String campaignName;
    @Column(name = "campaignDesc")
    private String campaignDesc;
    @Column(name = "sellingPrice")
    private String sellingPrice;
    @Column(name = "campaignTc")
    private String campaignTc;
    @Column(name = "productQty")
    private String productQty;
    @Column(name = "campaignImage")
    private String campaignImage;
    @Column(name = "totalPrice")
    private String totalPrice;
    @Column(name = "status")
    private String status;
    @Column(name = "flag")
    private String flag;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrginalPrice() {
        return orginalPrice;
    }

    public void setOrginalPrice(String orginalPrice) {
        this.orginalPrice = orginalPrice;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDesc() {
        return campaignDesc;
    }

    public void setCampaignDesc(String campaignDesc) {
        this.campaignDesc = campaignDesc;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getCampaignTc() {
        return campaignTc;
    }

    public void setCampaignTc(String campaignTc) {
        this.campaignTc = campaignTc;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getCampaignImage() {
        return campaignImage;
    }

    public void setCampaignImage(String campaignImage) {
        this.campaignImage = campaignImage;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
