package com.mezzofy.mzcoupon.Entity;


import java.util.List;

/**
 * Created by aruna on 8/18/17.
 */

public class CustomerCouponListmEntity {

    private List<CustomerCouponmEntity> customercoupons;

    private SizemEnity size;

    public List<CustomerCouponmEntity> getCustomercoupons() {
        return customercoupons;
    }

    public void setCustomercoupons(List<CustomerCouponmEntity> customercoupons) {
        this.customercoupons = customercoupons;
    }

    public SizemEnity getSize() {
        return size;
    }

    public void setSize(SizemEnity size) {
        this.size = size;
    }
}
