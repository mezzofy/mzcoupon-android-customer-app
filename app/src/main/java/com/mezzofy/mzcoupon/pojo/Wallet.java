package com.mezzofy.mzcoupon.pojo;

public class Wallet {

    private Integer customer_id;
    private double wallet_credit;
    private String updated_at;
    private String qr_code;
    private String qr_time;

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public double getWallet_credit() {
        return wallet_credit;
    }

    public void setWallet_credit(double wallet_credit) {
        this.wallet_credit = wallet_credit;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getQr_time() {
        return qr_time;
    }

    public void setQr_time(String qr_time) {
        this.qr_time = qr_time;
    }

}