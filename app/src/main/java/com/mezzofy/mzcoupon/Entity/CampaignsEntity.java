package com.mezzofy.mzcoupon.Entity;


import java.util.List;

/**
 * Created by inforios on 31/01/17.
 */

public class CampaignsEntity {

    private List<CampaignmEntity> campaigns;
    private List<CampGrpEntity> campaigngroups;
    private Size size;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<CampGrpEntity> getCampaigngroups() {
        return campaigngroups;
    }

    public void setCampaigngroups(List<CampGrpEntity> campaigngroups) {
        this.campaigngroups = campaigngroups;
    }

    public List<CampaignmEntity> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<CampaignmEntity> campaigns) {
        this.campaigns = campaigns;
    }
}
