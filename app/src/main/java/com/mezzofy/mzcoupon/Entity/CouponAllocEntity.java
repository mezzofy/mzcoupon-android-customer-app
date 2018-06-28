package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 17/04/17.
 */

public class CouponAllocEntity {

    private String allocationId;
    private String campaignId;
    private Integer allocationCount;
    private String allocationRefer;
    private String allocationRemark;
    private String updatedOn;

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

    public Integer getAllocationCount() {
        return allocationCount;
    }

    public void setAllocationCount(Integer allocationCount) {
        this.allocationCount = allocationCount;
    }

    public String getAllocationRefer() {
        return allocationRefer;
    }

    public void setAllocationRefer(String allocationRefer) {
        this.allocationRefer = allocationRefer;
    }

    public String getAllocationRemark() {
        return allocationRemark;
    }

    public void setAllocationRemark(String allocationRemark) {
        this.allocationRemark = allocationRemark;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
