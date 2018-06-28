package com.mezzofy.mzcoupon.Dao;


import com.mezzofy.mzcoupon.Entity.PaymentEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by aruna on 6/29/17.
 */

public class Payment_Dao {

    private Connection connection;

    public Payment_Dao(Connection con) {
        connection = con;
    }

    public Boolean addpayment(PaymentEntity paymentData) throws SQLException {
        Boolean res = false;
        String sql = "insert into tbl_payment (paymentId,paymentType,paymentEnviornment,paymentStatus) values(?,?,?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{paymentData.getPaymentId(), paymentData.getPaymentType(), paymentData.getPaymentEnviornment(), paymentData.getPaymentStatus()});
        return res;
    }


    public Boolean addpaymentdetail(PaymentDetailEntity paymentDetailData) throws SQLException {
        Boolean res = false;

        String sql = "insert into tbl_payment_dtl (paymentDetailId,merchantId,paymentId,paymentName,paymentLogourl,paymentMerchantId,paymentKey,paymentToken,paymentUrl,paymentStatus," +
                "hashCode,updatedOn,paymentType,paymentEnviornment,currency) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        res = DatabaseUtils.update(connection, sql, new Object[]{paymentDetailData.getPaymentDetailId(), paymentDetailData.getMerchantId(), paymentDetailData.getPaymentId(),
                paymentDetailData.getPaymentName(), paymentDetailData.getPaymentLogourl(), paymentDetailData.getPaymentMerchantId(), paymentDetailData.getPaymentKey(),
                paymentDetailData.getPaymentToken(), paymentDetailData.getPaymentUrl(), paymentDetailData.getPaymentStatus(), paymentDetailData.getHashCode(),
                paymentDetailData.getUpdatedOn(), paymentDetailData.getPaymentType(), paymentDetailData.getPaymentEnviornment(),paymentDetailData.getCurrency()});

        return res;
    }

    public Boolean Deletepaymentdetail() throws SQLException {
        Boolean res = false;
        String   sql = "delete from tbl_payment_dtl";
            res = DatabaseUtils.update(connection, sql, null);
        return res;
    }

    public Boolean Deletepayment() throws SQLException {
        Boolean res = false;
        String sql = "delete from tbl_payment";
        res = DatabaseUtils.update(connection, sql, null);

        return res;
    }

    public List<PaymentDetailEntity> getpaymentdetail() throws SQLException {
        List<PaymentDetailEntity> paymentDatas = null;
        ResultSetMapper<PaymentDetailEntity> resultSetMapper = new ResultSetMapper<PaymentDetailEntity>();
        String sql = "select * from tbl_payment_dtl";
        paymentDatas = DatabaseUtils.queryForList(connection, sql, resultSetMapper, PaymentDetailEntity.class, null);
        return paymentDatas;
    }


    public PaymentDetailEntity getPaymentPaypal(String paymentid) throws SQLException {
        PaymentDetailEntity paymentDatas = null;
        ResultSetMapper<PaymentDetailEntity> resultSetMapper = new ResultSetMapper<PaymentDetailEntity>();
        String sql = "select * from tbl_payment_dtl where paymentId=?";
        paymentDatas = DatabaseUtils.queryForObj(connection, sql, resultSetMapper, PaymentDetailEntity.class, new Object[]{paymentid});
        return paymentDatas;
    }

    public PaymentEntity getPayment(String paymentEnviornment) throws SQLException {
        PaymentEntity paymentDatas = null;
        ResultSetMapper<PaymentEntity> resultSetMapper = new ResultSetMapper<PaymentEntity>();
        String sql = "select * from tbl_payment where paymentEnviornment=?";
        paymentDatas = DatabaseUtils.queryForObj(connection, sql, resultSetMapper, PaymentEntity.class, new Object[]{paymentEnviornment});

        return paymentDatas;
    }

    public Boolean UpdatePaymentDetail(PaymentDetailEntity paymentDetailData) throws SQLException {
        Boolean res = false;

        String sql = "update tbl_payment_dtl SET paymentKey=?,paymentToken=?,paymentMerchantId=?,currency=? where paymentDetailId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{paymentDetailData.getPaymentKey(), paymentDetailData.getPaymentToken(), paymentDetailData.getPaymentMerchantId(),
                paymentDetailData.getCurrency(),paymentDetailData.getPaymentDetailId()});

        return res;
    }
}