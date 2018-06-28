package com.mezzofy.mzcoupon.Entity;



import java.util.List;

public class CampaignmEntity {

    private CampaignEntity campaign;
    private List<CampGrpDetailEntity> groups;

    public CampaignEntity getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignEntity merchant) {
        this.campaign = merchant;
    }

    public List<CampGrpDetailEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<CampGrpDetailEntity> groups) {
        this.groups = groups;
    }


}
