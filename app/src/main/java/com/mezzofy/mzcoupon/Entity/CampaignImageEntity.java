package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

import java.io.Serializable;


@Entity
public class CampaignImageEntity implements Serializable{

	@Column(name = "pimageId")
	private String pimageId;
	@Column(name = "campaignId")
	private String campaignId;
	@Column(name = "campaignImage")
	private String campaignImage;
	@Column(name = "imageSeq")
	private Integer imageSeq;
	@Column(name = "imageStatus")
	private String imageStatus;


	public Integer getImageSeq() {
		return imageSeq;
	}

	public void setImageSeq(Integer imageSeq) {
		this.imageSeq = imageSeq;
	}

	public String getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(String imageStatus) {
		this.imageStatus = imageStatus;
	}


	public String getPimageId() {
		return pimageId;
	}
	public void setPimageId(String pimageId) {
		this.pimageId = pimageId;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getCampaignImage() {
		return campaignImage;
	}
	public void setCampaignImage(String campaignImage) {
		this.campaignImage = campaignImage;
	}

}
