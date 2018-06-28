package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

@Entity
public class CampGrpEntity {
	@Column(name = "campgrpId")
	private String campgrpId;
	@Column(name = "merchantId")
	private String merchantId;
	@Column(name = "campgrpName")
	private String campgrpName;
	@Column(name = "campgrpImageurl")
	private String campgrpImageurl;
	@Column(name = "campgrpSeq")
	private Integer campgrpSeq;
	@Column(name = "campgrpStatus")
	private String campgrpStatus;
	@Column(name = "hashCode")
	private String hashCode;
	
	public String getCampgrpId() {
		return campgrpId;
	}
	public void setCampgrpId(String campgrpId) {
		this.campgrpId = campgrpId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getCampgrpName() {
		return campgrpName;
	}
	public void setCampgrpName(String campgrpName) {
		this.campgrpName = campgrpName;
	}
	public String getCampgrpImageurl() {
		return campgrpImageurl;
	}
	public void setCampgrpImageurl(String campgrpImageurl) {
		this.campgrpImageurl = campgrpImageurl;
	}
	public Integer getCampgrpSeq() {
		return campgrpSeq;
	}
	public void setCampgrpSeq(Integer campgrpSeq) {
		this.campgrpSeq = campgrpSeq;
	}
	public String getCampgrpStatus() {
		return campgrpStatus;
	}
	public void setCampgrpStatus(String campgrpStatus) {
		this.campgrpStatus = campgrpStatus;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
//	public Date getUpdatedOn() {
//		return updatedOn;
//	}
//	public void setUpdatedOn(Date updatedOn) {
//		this.updatedOn = updatedOn;
//	}

}