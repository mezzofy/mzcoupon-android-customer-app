package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.CustomerDeviceEntity;

/**
 * Created by aruna on 7/25/17.
 */

public class CustomerDevicemEntity {
    private CustomerDeviceEntity device;
    private CustomerEntity customer;

    public CustomerDeviceEntity getDevice() {
        return device;
    }

    public void setDevice(CustomerDeviceEntity device) {
        this.device = device;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
