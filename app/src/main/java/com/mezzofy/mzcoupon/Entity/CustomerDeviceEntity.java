package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by aruna on 7/25/17.
 */

@Entity
public class CustomerDeviceEntity {
    @Column(name = "custDeviceId")
    private String custDeviceId;
    @Column(name = "customerId")
    private String customerId;
    @Column(name = "deviceToken")
    private String deviceToken;
    @Column(name = "deviceName")
    private String deviceName;
    @Column(name = "deviceUuid")
    private String deviceUuid;
    @Column(name = "updatedOn")
    private String updatedOn;

    public String getCustDeviceId() {
        return custDeviceId;
    }

    public void setCustDeviceId(String custDeviceId) {
        this.custDeviceId = custDeviceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
