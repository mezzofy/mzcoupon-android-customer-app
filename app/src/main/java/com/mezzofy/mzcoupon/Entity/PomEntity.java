package com.mezzofy.mzcoupon.Entity;



import java.util.List;

/**
 * Created by priya on 28/03/17.
 */

public class PomEntity {
    private PoEntity po;
    private List<PoDetailmEntity> podetails;

    public PoEntity getPo() {
        return po;
    }

    public void setPo(PoEntity po) {
        this.po = po;
    }

    public List<PoDetailmEntity> getPodetails() {
        return podetails;
    }

    public void setPodetails(List<PoDetailmEntity> podetails) {
        this.podetails = podetails;
    }
}
