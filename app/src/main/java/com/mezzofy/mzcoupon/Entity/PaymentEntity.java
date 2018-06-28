package com.mezzofy.mzcoupon.Entity;



import com.mezzofy.mzcoupon.mapper.Column;
import com.mezzofy.mzcoupon.mapper.Entity;

import java.util.List;

/**
 * Created by aruna on 6/29/17.
 */

@Entity
public class PaymentEntity {
    @Column(name = "paymentId")
    private String paymentId;
    @Column(name = "paymentType")
    private String paymentType;
    @Column(name = "paymentEnviornment")
    private String paymentEnviornment;
    @Column(name = "paymentStatus")
    private String paymentStatus;

    List<PaymentDetailmEntity> paymentdetails;


    public List<PaymentDetailmEntity> getPaymentdetails() {
        return paymentdetails;
    }

    public void setPaymentdetails(List<PaymentDetailmEntity> paymentdetails) {
        this.paymentdetails = paymentdetails;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentEnviornment() {
        return paymentEnviornment;
    }

    public void setPaymentEnviornment(String paymentEnviornment) {
        this.paymentEnviornment = paymentEnviornment;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
