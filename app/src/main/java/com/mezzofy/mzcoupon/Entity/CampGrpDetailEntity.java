package com.mezzofy.mzcoupon.Entity;


import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

@Entity
public class CampGrpDetailEntity {
	@Column(name = "campaignId")
	private String campaignId;
	@Column(name = "campgrpId")
	private String campgrpId;

	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getCampgrpId() {
		return campgrpId;
	}
	public void setCampgrpId(String campgrpId) {
		this.campgrpId = campgrpId;
	}
	
	
	
}