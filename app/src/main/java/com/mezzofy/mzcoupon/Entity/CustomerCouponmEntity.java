package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CouponEntity;


/**
 * Created by aruna on 8/18/17.
 */

public class CustomerCouponmEntity {



    private CampaignEntity campaign;
    private CouponEntity coupon;
    private String size;

    public CampaignEntity getCampaign() { return campaign; }

    public void setCampaign(CampaignEntity campaign) {  this.campaign = campaign;  }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }
}
