
package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

@Entity
public class SiteEntity {
	@Column(name = "siteId")
	private String siteId;
	@Column (name = "merchantId")
	private String merchantId;
	@Column (name = "siteName")
	private String siteName;
	@Column (name = "siteAddress")
	private String siteAddress;
	@Column (name = "siteLatitude")
	private Float siteLatitude;
	@Column (name = "siteLongitude")
	private Float siteLongitude;
	@Column (name = "siteLocation")
	private String siteLocation;
	@Column (name = "siteContact")
	private String siteContact;
	@Column (name = "siteDesc")
	private String siteDesc;
	@Column (name = "siteEmailId")
	private String siteEmailId;
	@Column (name = "siteDisplayWallet")
	private String siteDisplayWallet;
	@Column (name = "siteSeqNo")
	private Integer siteSeqNo;
	@Column (name = "siteImageurl")
	private String siteImageurl;
	@Column (name = "siteOnlineStatus")
	private String siteOnlineStatus;
	@Column (name = "siteStatus")
	private String siteStatus;
	@Column (name = "siteRedeemPass")
	private String siteRedeemPass;
	@Column (name = "hashCode")
	private String hashCode;
	@Column (name = "locationId")
	private String locationId;

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

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
	public String getSiteContact() {
		return siteContact;
	}
	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}
	public String getSiteDesc() {
		return siteDesc;
	}
	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}
	public String getSiteEmailId() {
		return siteEmailId;
	}
	public void setSiteEmailId(String siteEmailId) {
		this.siteEmailId = siteEmailId;
	}
	public String getSiteDisplayWallet() {
		return siteDisplayWallet;
	}
	public void setSiteDisplayWallet(String siteDisplayWallet) {
		this.siteDisplayWallet = siteDisplayWallet;
	}
	public Integer getSiteSeqNo() {
		return siteSeqNo;
	}
	public void setSiteSeqNo(Integer siteSeqNo) {
		this.siteSeqNo = siteSeqNo;
	}
	public String getSiteImageurl() {
		return siteImageurl;
	}
	public void setSiteImageurl(String siteImageurl) {
		this.siteImageurl = siteImageurl;
	}
	
	public String getSiteOnlineStatus() {
		return siteOnlineStatus;
	}
	public void setSiteOnlineStatus(String siteOnlineStatus) {
		this.siteOnlineStatus = siteOnlineStatus;
	}
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
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

	
}