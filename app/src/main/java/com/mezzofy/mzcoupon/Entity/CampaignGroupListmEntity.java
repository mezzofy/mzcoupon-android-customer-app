package com.mezzofy.mzcoupon.Entity;

import java.util.List;

public class CampaignGroupListmEntity {
	private List<CampaignGroupmEntity> campaigngroups;
	private SitemEnity size;
	public List<CampaignGroupmEntity> getCampaigngroups() {
		return campaigngroups;
	}
	public void setCampaigngroups(List<CampaignGroupmEntity> campaigngroups) {
		this.campaigngroups = campaigngroups;
	}


	public SitemEnity getSize() {
		return size;
	}

	public void setSize(SitemEnity size) {
		this.size = size;
	}
}