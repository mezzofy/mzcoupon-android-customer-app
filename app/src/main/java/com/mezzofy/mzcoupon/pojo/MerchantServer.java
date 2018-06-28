package com.mezzofy.mzcoupon.pojo;

import com.mezzofy.mzcoupon.Entity.MechantSiteEntity;

import java.util.ArrayList;

/**
 * Created by udhayinforios on 16/3/16.
 */
public class MerchantServer {

    private ResultRes response;
    private Merchant merchant;
    private ArrayList<Merchant> merchants;
    private ArrayList<MapSiteRes> maps;
    private MechantSiteEntity outlet;

    public ResultRes getResponse() {
        return response;
    }

    public void setResponse(ResultRes response) {
        this.response = response;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public ArrayList<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(ArrayList<Merchant> merchants) {
        this.merchants = merchants;
    }

    public ArrayList<MapSiteRes> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<MapSiteRes> maps) {
        this.maps = maps;
    }

    public MechantSiteEntity getOutlet() {
        return outlet;
    }

    public void setOutlet(MechantSiteEntity outlet) {
        this.outlet = outlet;
    }
}
