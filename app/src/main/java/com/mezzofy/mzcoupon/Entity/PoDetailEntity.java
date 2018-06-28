package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by Aruna on 28/03/17.
 */

@Entity
public class PoDetailEntity {
    @Column(name = "podtlId")
    private String podtlId;
    @Column(name = "productId")
    private String productId;
    @Column(name = "campaignId")
    private String campaignId;
    @Column(name = "poId")
    private String poId;
    @Column(name = "campaignName")
    private String campaignName;
    @Column(name = "campaignPrice")
    private String campaignPrice;
    @Column(name = "campaignQty")
    private String campaignQty;
    @Column(name = "campaignTotal")
    private String campaignTotal;
    @Column(name = "status")
    private String status;
    @Column(name = "updatedOn")
    private String updatedOn;

    public String getCampaignPrice() {
        return campaignPrice;
    }

    public void setCampaignPrice(String campaignPrice) {
        this.campaignPrice = campaignPrice;
    }

    public String getCampaignQty() {
        return campaignQty;
    }

    public void setCampaignQty(String campaignQty) {
        this.campaignQty = campaignQty;
    }

    public String getCampaignTotal() {
        return campaignTotal;
    }

    public void setCampaignTotal(String campaignTotal) {
        this.campaignTotal = campaignTotal;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
    public String getPodtlId() {
        return podtlId;
    }

    public void setPodtlId(String podtlId) {
        this.podtlId = podtlId;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
