package com.mezzofy.mzcoupon.module;

import android.content.Context;

import com.mezzofy.MzCouponAPI.data.PaymentDetailDataModel;
import com.mezzofy.MzCouponAPI.data.PaymentListDataModel;
import com.mezzofy.MzCouponAPI.data.PoData;
import com.mezzofy.MzCouponAPI.data.PomData;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZPayment;
import com.mezzofy.MzCouponAPI.utills.APIServerException;

import com.mezzofy.mzcoupon.Entity.PaymentEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailEntity;
import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.Dao.Payment_Dao;

import com.mezzofy.mzcoupon.Entity.PaymentDetailmEntity;
import com.mezzofy.mzcoupon.Entity.PaymentListmEntity;
import com.mezzofy.mzcoupon.Entity.PaymentmEntity;
import com.mezzofy.mzcoupon.Entity.PomEntity;


import org.json.JSONException;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Aruna on 08/10/2015.
 */
public class Payment_Module {

   private MZPayment paymentModule;
    private MojodomoDB dbhelper;


    public Payment_Module(Context context) {
        dbhelper = new MojodomoDB(context);
        paymentModule=new MZPayment(context);
    }


    public List<PaymentDetailmEntity> getPaymentDetailAPI() {
        List<PaymentDetailmEntity> paymentDetailmEntities = null;
        PaymentListDataModel paymentListmData=paymentModule.GetPayments();
        if(paymentListmData!=null)
        {
            try {
                PaymentListmEntity paymentListmEntity=(PaymentListmEntity) JsonMapper.mapJsonToObj(paymentListmData, PaymentListmEntity.class);
                paymentDetailmEntities=paymentListmEntity.getPaymentdetails();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return paymentDetailmEntities;
    }

    public PoEntity DownloadChargeCouponFromServer(PomEntity pomEntity) throws APIServerException {

        PoEntity resp=null;

        try {
            PomData pomData=(PomData) JsonMapper.mapJsonToObj(pomEntity, PomData.class);
            PoData poData=paymentModule.RequestChargeCoupons(pomData);
            if(poData!=null) {
                PomEntity pomEntity1 = (PomEntity) JsonMapper.mapJsonToObj(poData, PomEntity.class);
                if (pomEntity1 != null)
                    resp=pomEntity1.getPo();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }

    public PoEntity DownloadFreeCouponFromServer(PomEntity pomEntity)throws APIServerException {

        PoEntity resp=null;

        try {
            PomData pomData=(PomData) JsonMapper.mapJsonToObj(pomEntity, PomData.class);
            PoData poData=paymentModule.RequestFreeCoupon(pomData);
            if(poData!=null) {
                PomEntity pomEntity1 = (PomEntity) JsonMapper.mapJsonToObj(poData, PomEntity.class);
                if (pomEntity1 != null)
                    resp=pomEntity1.getPo();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }



//    public PaymentDetailmEntity updatePaymentApi(PaymentDetailmEntity paymentDetailmEntity)throws APIServerException {
//
//        PaymentDetailmEntity resp = null;
//
//        try {
//            PaymentDetailDataModel paymentDetailmData=(PaymentDetailDataModel) JsonMapper.mapJsonToObj(paymentDetailmEntity, PaymentDetailDataModel.class);
//            PaymentDetailDataModel retpaymentdetailmdata=paymentModule.PaymentUpdate(paymentDetailmData);
//            if(retpaymentdetailmdata!=null)
//                resp=(PaymentDetailmEntity) JsonMapper.mapJsonToObj(retpaymentdetailmdata, PaymentDetailmEntity.class);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return resp;
//    }


    public Boolean addPayment(PaymentEntity paymentData) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.addpayment(paymentData);
            if(res)
                con.commit();
            else
                con.rollback();

        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }


    public Boolean UpdatePaymentDetail(PaymentDetailEntity paymentDetailData) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.UpdatePaymentDetail(paymentDetailData);

                if(res)
                    con.commit();
                else
                    con.rollback();

        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean deletepayment() throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.Deletepayment();

            if(res)
                con.commit();
            else
                con.rollback();

        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean addPayment( List<PaymentmEntity> paymentListModel) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);

            for(PaymentmEntity paymentModel:paymentListModel)
            {
                res = daopayment.addpayment(paymentModel.getPayment());
                if(res)
                    con.commit();
                else
                    con.rollback();
            }
        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean addPaymentdetail(List<PaymentDetailmEntity> list) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);

            res=daopayment.Deletepaymentdetail();
            if(res) {
                for (PaymentDetailmEntity paymentDetailModel : list) {
                    res = daopayment.addpaymentdetail(paymentDetailModel.getPaymentdetail());
                    if (res)
                        con.commit();
                    else
                        con.rollback();
                }
            }

        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean addPaymentdetail(PaymentDetailmEntity paymentDetailModel) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);

                res = daopayment.addpaymentdetail(paymentDetailModel.getPaymentdetail());
                if(res)
                    con.commit();
                else
                    con.rollback();

        }catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public List<PaymentDetailEntity> getpaymentdetail()throws Exception{

        List<PaymentDetailEntity> res = new ArrayList<>();
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.getpaymentdetail();

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public PaymentDetailEntity getPaymentPaypal(String Paymentid) throws Exception{

        PaymentDetailEntity res=new PaymentDetailEntity();;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.getPaymentPaypal(Paymentid);

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public PaymentEntity getPayment(String paymentEnviornment) throws Exception{

        PaymentEntity res=new PaymentEntity();;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Payment_Dao daopayment = dbhelper.getPaymentDao(con);
            res = daopayment.getPayment(paymentEnviornment);

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

}
