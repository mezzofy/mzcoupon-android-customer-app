package com.mezzofy.mzcoupon.Entity;

import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

/**
 * Created by aruna on 9/29/17.
 */

@Entity
public class WalletEntity {
    @Column(name = "walletId")
    private String walletId;
    @Column(name = "customerId")
    private String customerId;
    @Column(name = "walletCredit")
    private String walletCredit;
    @Column(name = "qrCode")
    private String qrCode;
    @Column(name = "qrTime")
    private String qrTime;
    @Column(name = "rewardPoint")
    private String rewardPoint;
    @Column(name = "updatedOn")
    private String updatedOn;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getWalletCredit() {
        return walletCredit;
    }

    public void setWalletCredit(String walletCredit) {
        this.walletCredit = walletCredit;
    }

    public String getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(String rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrTime() {
        return qrTime;
    }

    public void setQrTime(String qrTime) {
        this.qrTime = qrTime;
    }



    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
