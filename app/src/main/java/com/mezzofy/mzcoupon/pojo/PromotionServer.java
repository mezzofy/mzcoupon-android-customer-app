package com.mezzofy.mzcoupon.pojo;

import com.google.android.gms.analytics.ecommerce.Promotion;

public class PromotionServer {

    private Promotion promotion;
    private ResultRes response;

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public ResultRes getResponse() {
        return response;
    }

    public void setResponse(ResultRes response) {
        this.response = response;
    }

}