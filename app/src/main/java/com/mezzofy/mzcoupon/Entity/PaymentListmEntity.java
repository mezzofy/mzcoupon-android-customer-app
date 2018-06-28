package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 6/29/17.
 */

public class PaymentListmEntity {
    private List<PaymentmEntity> payments;
    private List<PaymentDetailmEntity> paymentdetails;
    private SizemEnity size;

    public List<PaymentmEntity> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentmEntity> payments) {
        this.payments = payments;
    }

    public List<PaymentDetailmEntity> getPaymentdetails() {
        return paymentdetails;
    }

    public void setPaymentdetails(List<PaymentDetailmEntity> paymentdetails) {
        this.paymentdetails = paymentdetails;
    }

    public SizemEnity getSize() {
        return size;
    }

    public void setSize(SizemEnity size) {
        this.size = size;
    }
}
