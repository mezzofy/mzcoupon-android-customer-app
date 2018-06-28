package com.mezzofy.mzcoupon.pojo;

import java.util.ArrayList;

/**
 * Created by LENOVO on 25/08/2015.
 */
public class Productfilter {

    private ArrayList<CategoryRes> categoryList;
    private ArrayList<LocationRes> locationList;
    private ArrayList<Merchant> merchantList;
    private Integer companyId;
    private Integer offset;
    private Integer size;

    public ArrayList<CategoryRes> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryRes> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<LocationRes> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LocationRes> locationList) {
        this.locationList = locationList;
    }

    public ArrayList<Merchant> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(ArrayList<Merchant> merchantList) {
        this.merchantList = merchantList;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
