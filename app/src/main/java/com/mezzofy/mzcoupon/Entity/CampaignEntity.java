package com.mezzofy.mzcoupon.Entity;



import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

import java.util.List;

@Entity
public class CampaignEntity {
	@Column(name = "campaignId")
	private String campaignId;
	@Column(name = "merchantId")
	private String merchantId;
	@Column(name = "productId")
	private String productId;
	@Column(name = "productmerchantId")
	private String productmerchantId;
	@Column(name = "campaignCode")
	private String campaignCode;
	@Column(name = "campaignType")
	private String campaignType;
	@Column(name = "brand")
	private String brand;
	@Column(name = "campaignName")
	private String campaignName;
	@Column(name = "campaignDesc")
	private String campaignDesc;
	@Column(name = "sellingPrice")
	private Float sellingPrice;
	@Column(name = "orginalPrice")
	private Float orginalPrice;
	@Column(name = "campaignTc")
	private String campaignTc;
	@Column(name = "campaignStatus")
	private String campaignStatus;
	@Column(name = "dailyLimit")
	private Integer dailyLimit;
	@Column(name = "packQty")
	private Integer packQty;
	@Column(name = "emailStaff")
	private String emailStaff;
	@Column(name = "couponOver")
	private String couponOver;
	@Column(name = "dayFilter")
	private String dayFilter;
	@Column(name = "expiryDays")
	private Integer expiryDays;
	@Column(name = "expirydue")
	private String expirydue;
	@Column(name = "expiryname")
	private String expiryname;
	@Column(name = "totalRedeem")
	private int totalRedeem;
	@Column(name = "allocationCount")
	private Integer allocationCount;
	@Column(name = "fromDate")
	private String startDate;
	@Column(name = "toDate")
	private String endDate;
	@Column(name = "couponUrl")
	private String couponUrl;
	@Column(name = "passbookUrl")
	private String passbookUrl;

	@Column(name = "issuedcoupon")
	private Integer issuedcoupon;
	@Column(name = "redeemcoupon")
	private Integer redeemcoupon;
	@Column(name = "allocationcoupon")
	private Integer allocationcoupon;


	@Column(name = "Topten")
	private String Topten;
	@Column(name = "Favourite")
	private String Favourite;

	@Column(name = "hashCode")
	private String hashCode;


	@Column(name = "campaignNote1")
	private String campaignNote1;
	@Column(name = "campaignNote2")
	private String campaignNote2;
	@Column(name = "campaignNote3")
	private String campaignNote3;

	@Column(name = "distance")
	private Integer distance;
	@Column(name = "campaignUuid")
	private String campaignUuid;
	@Column(name = "typeService")
	private String typeService;
	@Column(name = "reviewUrl")
	private String reviewUrl;
	@Column(name = "videoUrl")
	private String videoUrl;


	@Column(name = "txBrand")
	private String txBrand;
	@Column(name = "campaignRemark")
	private String campaignRemark;
	@Column(name = "pickup")
	private String pickup;
	@Column(name = "delivery")
	private String delivery;
	@Column(name = "booking")
	private String booking;
	@Column(name = "outcall")
	private String outcall;


	@Column(name = "dailyLimitType")
	private String dailyLimitType;


	//local
	@Column(name = "size")
	private String size;

	private List<CampaignImagemEntity> campaignimages;
	private List<SitemEnity> sites;

	private Integer qtycount;

	public Integer getQtycount() {
		return qtycount;
	}

	public void setQtycount(Integer qtycount) {
		this.qtycount = qtycount;
	}

	public String getDailyLimitType() {
		return dailyLimitType;
	}

	public void setDailyLimitType(String dailyLimitType) {
		this.dailyLimitType = dailyLimitType;
	}

	public String getTxBrand() {
		return txBrand;
	}

	public void setTxBrand(String txBrand) {
		this.txBrand = txBrand;
	}

	public String getCampaignRemark() {
		return campaignRemark;
	}

	public void setCampaignRemark(String campaignRemark) {
		this.campaignRemark = campaignRemark;
	}

	public String getPickup() {
		return pickup;
	}

	public void setPickup(String pickup) {
		this.pickup = pickup;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

	public String getOutcall() {
		return outcall;
	}

	public void setOutcall(String outcall) {
		this.outcall = outcall;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getCampaignUuid() {
		return campaignUuid;
	}

	public void setCampaignUuid(String campaignUuid) {
		this.campaignUuid = campaignUuid;
	}

	public String getTypeService() {
		return typeService;
	}

	public void setTypeService(String typeService) {
		this.typeService = typeService;
	}

	public String getReviewUrl() {
		return reviewUrl;
	}

	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getCampaignNote1() {
		return campaignNote1;
	}

	public void setCampaignNote1(String campaignNote1) {
		this.campaignNote1 = campaignNote1;
	}

	public String getCampaignNote2() {
		return campaignNote2;
	}

	public void setCampaignNote2(String campaignNote2) {
		this.campaignNote2 = campaignNote2;
	}

	public String getCampaignNote3() {
		return campaignNote3;
	}

	public void setCampaignNote3(String campaignNote3) {
		this.campaignNote3 = campaignNote3;
	}



	public String getTopten() {
		return Topten;
	}

	public void setTopten(String topten) {
		Topten = topten;
	}

	public String getFavourite() {
		return Favourite;
	}

	public void setFavourite(String favourite) {
		Favourite = favourite;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Float getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Float getOrginalPrice() {
		return orginalPrice;
	}

	public void setOrginalPrice(Float orginalPrice) {
		this.orginalPrice = orginalPrice;
	}

	public void setTotalRedeem(int totalRedeem) {
		this.totalRedeem = totalRedeem;
	}

	public Integer getAllocationcoupon() {
		return allocationcoupon;
	}

	public void setAllocationcoupon(Integer allocationcoupon) {
		this.allocationcoupon = allocationcoupon;
	}

	public Integer getIssuedcoupon() {
		return issuedcoupon;
	}

	public void setIssuedcoupon(Integer issuedcoupon) {
		this.issuedcoupon = issuedcoupon;
	}


	public Integer getRedeemcoupon() {
		return redeemcoupon;
	}

	public void setRedeemcoupon(Integer redeemcoupon) {
		this.redeemcoupon = redeemcoupon;
	}

	public String getCouponUrl() {
		return couponUrl;
	}

	public void setCouponUrl(String couponUrl) {
		this.couponUrl = couponUrl;
	}

	public String getPassbookUrl() {
		return passbookUrl;
	}

	public void setPassbookUrl(String passbookUrl) {
		this.passbookUrl = passbookUrl;
	}




	public Integer getAllocationCount() {
		return allocationCount;
	}

	public void setAllocationCount(Integer allocationCount) {
		this.allocationCount = allocationCount;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignDesc() {
		return campaignDesc;
	}

	public void setCampaignDesc(String campaignDesc) {
		this.campaignDesc = campaignDesc;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public List<CampaignImagemEntity> getCampaignimages() {
		return campaignimages;
	}

	public void setCampaignimages(List<CampaignImagemEntity> campaignimages) {
		this.campaignimages = campaignimages;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public String getCampaignTc() {
		return campaignTc;
	}

	public void setCampaignTc(String campaignTc) {
		this.campaignTc = campaignTc;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCouponOver() {
		return couponOver;
	}

	public void setCouponOver(String couponOver) {
		this.couponOver = couponOver;
	}

	public Integer getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(Integer dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public String getDayFilter() {
		return dayFilter;
	}

	public void setDayFilter(String dayFilter) {
		this.dayFilter = dayFilter;
	}

	public String getEmailStaff() {
		return emailStaff;
	}

	public void setEmailStaff(String emailStaff) {
		this.emailStaff = emailStaff;
	}

	public Integer getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(Integer expiryDays) {
		this.expiryDays = expiryDays;
	}

	public String getExpirydue() {
		return expirydue;
	}

	public void setExpirydue(String expirydue) {
		this.expirydue = expirydue;
	}

	public String getExpiryname() {
		return expiryname;
	}

	public void setExpiryname(String expiryname) {
		this.expiryname = expiryname;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getPackQty() {
		return packQty;
	}

	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductmerchantId() {
		return productmerchantId;
	}

	public void setProductmerchantId(String productmerchantId) {
		this.productmerchantId = productmerchantId;
	}

	public List<SitemEnity> getSites() {
		return sites;
	}

	public void setSites(List<SitemEnity> sites) {
		this.sites = sites;
	}

	public Integer getTotalRedeem() {
		return totalRedeem;
	}

	public void setTotalRedeem(Integer totalRedeem) {
		this.totalRedeem = totalRedeem;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}
