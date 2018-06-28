package com.mezzofy.mzcoupon.Entity;



import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

import java.util.Comparator;

@Entity
public class MechantSiteEntity {
    @Column(name = "siteId")
    private String siteId;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "siteName")
    private String siteName;
    @Column(name = "siteAddress")
    private String siteAddress;
    @Column(name = "siteLatitude")
    private Float siteLatitude;
    @Column(name = "siteLongitude")
    private Float siteLongitude;
    @Column(name = "siteLocation")
    private String siteLocation;
    @Column(name = "siteContact")
    private String siteContact;
    @Column(name = "siteStatus")
    private String siteStatus;
    @Column(name = "redeemPass")
    private String redeemPass;
    @Column(name = "currency")
    private String currency;
    @Column(name = "siteEmailId")
    private String siteEmailId;
    @Column(name = "siteOnlineStatus")
    private String siteOnlineStatus;
    @Column(name = "siteRedeemPass")
    private String siteRedeemPass;
    @Column(name = "hashCode")
    private String hashCode;
    @Column(name = "locationId")
    private String locationId;

    public Float getSiteLatitude() {
        return siteLatitude;
    }

    public void setSiteLatitude(Float siteLatitude) {
        this.siteLatitude = siteLatitude;
    }

    public Float getSiteLongitude() {
        return siteLongitude;
    }

    public void setSiteLongitude(Float siteLongitude) {
        this.siteLongitude = siteLongitude;
    }

    public String getSiteContact() {
        return siteContact;
    }

    public void setSiteContact(String siteContact) {
        this.siteContact = siteContact;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    public String getRedeemPass() {
        return redeemPass;
    }

    public void setRedeemPass(String redeemPass) {
        this.redeemPass = redeemPass;
    }

    public String getSiteEmailId() {
        return siteEmailId;
    }

    public void setSiteEmailId(String siteEmailId) {
        this.siteEmailId = siteEmailId;
    }

    public String getSiteOnlineStatus() {
        return siteOnlineStatus;
    }

    public void setSiteOnlineStatus(String siteOnlineStatus) {
        this.siteOnlineStatus = siteOnlineStatus;
    }

    public String getSiteRedeemPass() {
        return siteRedeemPass;
    }

    public void setSiteRedeemPass(String siteRedeemPass) {
        this.siteRedeemPass = siteRedeemPass;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static Comparator<MechantSiteEntity> getSiteNamecmp() {
        return siteNamecmp;
    }

    public static void setSiteNamecmp(Comparator<MechantSiteEntity> siteNamecmp) {
        MechantSiteEntity.siteNamecmp = siteNamecmp;
    }


    public static Comparator<MechantSiteEntity> siteNamecmp = new Comparator<MechantSiteEntity>() {

        @Override
        public int compare(MechantSiteEntity arg0, MechantSiteEntity arg1) {
            String plsname1 = arg0.getSiteName().toUpperCase();
            String plsname2 = arg1.getSiteName().toUpperCase();

            return plsname1.compareTo(plsname2);
        }
    };

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
