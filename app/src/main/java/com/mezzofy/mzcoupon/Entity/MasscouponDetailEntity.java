package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 9/20/17.
 */

public class MasscouponDetailEntity {
    private String massId;
    private String campaignId;
    private Integer qty;

    public String getMassId() {
        return massId;
    }

    public void setMassId(String massId) {
        this.massId = massId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
