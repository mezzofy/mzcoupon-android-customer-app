package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;

import java.util.List;

/**
 * Created by aruna on 11/2/17.
 */

public class CampaignSearchmEntity {
    private int page;
    private double latitude;
    private double longitude;
    private List<SiteEntity> sites;
    private List<CampGrpEntity> groups;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<SiteEntity> getSites() {
        return sites;
    }

    public void setSites(List<SiteEntity> sites) {
        this.sites = sites;
    }

    public List<CampGrpEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<CampGrpEntity> groups) {
        this.groups = groups;
    }
}
