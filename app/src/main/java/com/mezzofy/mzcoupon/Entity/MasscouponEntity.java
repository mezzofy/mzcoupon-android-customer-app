package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 9/20/17.
 */

public class MasscouponEntity {
    private String massId;
    private String merchantId;
    private String siteId;
    private String customerId;
    private String referenceNo;
    private String massStatus;
    private String updatedOn;

    public String getMassId() {
        return massId;
    }

    public void setMassId(String massId) {
        this.massId = massId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getMassStatus() {
        return massStatus;
    }

    public void setMassStatus(String massStatus) {
        this.massStatus = massStatus;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
