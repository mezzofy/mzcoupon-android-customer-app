package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 9/20/17.
 */

public class MassCouponmEntity {
    private MasscouponEntity masscoupon;
    private List<MassCouponDetailmEntity> masscoupondtls;


    public MasscouponEntity getMasscoupon() {
        return masscoupon;
    }

    public void setMasscoupon(MasscouponEntity masscoupon) {
        this.masscoupon = masscoupon;
    }

    public List<MassCouponDetailmEntity> getMasscoupondtls() {
        return masscoupondtls;
    }

    public void setMasscoupondtls(List<MassCouponDetailmEntity> masscoupondtls) {
        this.masscoupondtls = masscoupondtls;
    }
}
