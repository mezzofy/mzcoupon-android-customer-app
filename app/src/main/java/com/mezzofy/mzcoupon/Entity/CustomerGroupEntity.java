package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 8/8/17.
 */

public class CustomerGroupEntity {
    private String customerGroupId;
    private String merchantId;
    private String groupName;
    private String groupStatus;
    private Double dollarSpending;
    private Double rewardPoint;
    private String hashCode;
    private String updatedOn;

    public String getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(String customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Double getDollarSpending() {
        return dollarSpending;
    }

    public void setDollarSpending(Double dollarSpending) {
        this.dollarSpending = dollarSpending;
    }

    public Double getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(Double rewardPoint) {
        this.rewardPoint = rewardPoint;
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
