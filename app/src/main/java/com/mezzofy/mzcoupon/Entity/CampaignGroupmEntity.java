package com.mezzofy.mzcoupon.Entity;


import java.util.List;

public class CampaignGroupmEntity {
	private CampGrpEntity campaigngroup;
	private List<CampaignGroupDtlmEntity>  campGrpDetails;
	
	public CampGrpEntity getCampaigngroup() {
		return campaigngroup;
	}

	public void setCampaigngroup(CampGrpEntity campaigngroup) {
		this.campaigngroup = campaigngroup;
	}

	public List<CampaignGroupDtlmEntity> getCampGrpDetails() {
		return campGrpDetails;
	}

	public void setCampGrpDetails(List<CampaignGroupDtlmEntity> campGrpDetails) {
		this.campGrpDetails = campGrpDetails;
	}

}