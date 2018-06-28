package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mezzofy.MzCouponAPI.data.CustomerCouponListDataModel;
import com.mezzofy.MzCouponAPI.data.CustomerCouponDataModel;
import com.mezzofy.MzCouponAPI.data.GiftcouponDataModel;
import com.mezzofy.MzCouponAPI.data.MassCouponDataModel;
import com.mezzofy.MzCouponAPI.data.SiteDataModel;
import com.mezzofy.MzCouponAPI.data.SizeDataModel;
import com.mezzofy.MzCouponAPI.data.StockData;
import com.mezzofy.MzCouponAPI.data.StockDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZCoupon;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CouponEntity;

import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.Dao.Coupon_Dao;

import com.mezzofy.mzcoupon.Entity.CustomerCouponListmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.Entity.GiftcouponmEntity;
import com.mezzofy.mzcoupon.Entity.MassCouponmEntity;
import com.mezzofy.mzcoupon.Entity.SitemEnity;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.Entity.StockEntity;
import com.mezzofy.mzcoupon.Entity.StockmEntity;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;

import org.json.JSONException;


import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by aruna on 8/18/17.
 */

public class Coupon_Module {

    private MojodomoDB dbhelper;
    SharedPreferences settings;
    MZCoupon couponModule;

    public Coupon_Module(Context context) {
        dbhelper = new MojodomoDB(context);
        couponModule = new MZCoupon(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public List<CustomerCouponmEntity> getcouponlistfromserver(String customerId, String couponstatus, int page) {
        List<CustomerCouponmEntity> customerCouponModelList = null;

        CustomerCouponListDataModel customerCouponListmData = couponModule.getCoupons(customerId, couponstatus, page);
        if (customerCouponListmData != null) {
            CustomerCouponListmEntity customerCouponListModel = null;

            try {
                customerCouponListModel = (CustomerCouponListmEntity) JsonMapper.mapJsonToObj(customerCouponListmData, CustomerCouponListmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customerCouponModelList = customerCouponListModel.getCustomercoupons();

                SharedPreferences.Editor editor = settings.edit();
            try {
                editor.putString("size", ObjectSerializer.serialize(customerCouponListModel.getSize()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.commit();



        }

        return customerCouponModelList;
    }


    public int ReleaseCouponStockByCampaignId(String CampaignId, String CouponId) {
        int stockqty = 0;
        StockDataModel stockDataModel = couponModule.ReleaseCouponStockByCampaignId(CampaignId, CouponId);

        if (stockDataModel != null) {
            stockqty = stockDataModel.getStock().getStockqty();
        }
        return stockqty;
    }

    public MassCouponmEntity getMasscoupondetailfromserver(String siteId, String refId) {


        MassCouponDataModel massCouponmData = couponModule.getMassCouponDetail(siteId, refId);
        MassCouponmEntity massCouponModel = null;
        if (massCouponmData != null) {
            try {
                massCouponModel = (MassCouponmEntity) JsonMapper.mapJsonToObj(massCouponmData, MassCouponmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return massCouponModel;
    }

    public CustomerCouponmEntity getcoupondetailfromserver(String CouponId) {
        CustomerCouponmEntity customerCouponModel = null;

        CustomerCouponDataModel customerCouponmData = couponModule.getCouponById(CouponId);
        if (customerCouponmData != null) {
            try {
                customerCouponModel = (CustomerCouponmEntity) JsonMapper.mapJsonToObj(customerCouponmData, CustomerCouponmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return customerCouponModel;
    }

    public SitemEnity sitepassCheck(String sitepass) {
        SitemEnity siteModel = null;

        SiteDataModel sitemData = couponModule.checkSitePass(sitepass);
        if (sitemData != null) {
            try {
                siteModel = (SitemEnity) JsonMapper.mapJsonToObj(sitemData, SitemEnity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return siteModel;
    }


    public int getredeamstockfromserver(String CouponId, String CamId) {
        int stockqty = 0;

        stockqty = couponModule.getredeamstockfromserver(CouponId, CamId);

        return stockqty;
    }


    public List<CustomerCouponmEntity> getCustomercoupon(String customerId, int page) throws IOException {
        List<CustomerCouponmEntity> customerCouponModelList = null;

        SizemEnity size=null;

        CustomerCouponListDataModel customerCouponListmData = couponModule.getCouponsByStatus(customerId, page);

        if (customerCouponListmData != null) {

            CustomerCouponListmEntity customerCouponListModel = null;
            try {
                customerCouponListModel = (CustomerCouponListmEntity) JsonMapper.mapJsonToObj(customerCouponListmData, CustomerCouponListmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customerCouponModelList = customerCouponListModel.getCustomercoupons();


            size = customerCouponListModel.getSize();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("couponsize", ObjectSerializer.serialize(size));
            editor.commit();
        }

        return customerCouponModelList;
    }

    public List<CustomerCouponmEntity> getCampiagncoupon(String Campaignid, String CustomerId, String couponstatus, int page) {
        List<CustomerCouponmEntity> customerCouponModelList = null;

        CustomerCouponListDataModel customerCouponListmData=couponModule.getCouponsByCampiagn(Campaignid,CustomerId,couponstatus,page);
        if(customerCouponListmData!=null){
            try {
                CustomerCouponListmEntity customerCouponListmEntity = (CustomerCouponListmEntity) JsonMapper.mapJsonToObj(customerCouponListmData, CustomerCouponListmEntity.class);
                customerCouponModelList=customerCouponListmEntity.getCustomercoupons();

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("size", ObjectSerializer.serialize(customerCouponListmEntity.getSize()));
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return customerCouponModelList;
    }


  /*  public List<CustomerCouponmEntity> getActivecoupon(String customerId) {
        List<CustomerCouponmEntity> customerCouponModelList = null;

        CustomerCouponListDataModel customerCouponListmData= couponModule.getCouponsByStatus(customerId);
        if(customerCouponListmData!=null){
            try {
                CustomerCouponListmEntity customerCouponListmEntity = (CustomerCouponListmEntity) JsonMapper.mapJsonToObj(customerCouponListmData, CustomerCouponListmEntity.class);
                customerCouponModelList=customerCouponListmEntity.getCustomercoupons();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return customerCouponModelList;
    }*/


    public GiftcouponmEntity PostGiftCouponToServerAPI(GiftcouponmEntity giftcouponModel) throws APIServerException {
        GiftcouponmEntity resp = null;

        try {
            GiftcouponDataModel giftcouponmData=(GiftcouponDataModel)JsonMapper.mapJsonToObj(giftcouponModel, GiftcouponDataModel.class);
            GiftcouponDataModel retgiftcoupnmdate=couponModule.SendGiftCoupon(giftcouponmData);

            if(retgiftcoupnmdate!=null) {
                resp = (GiftcouponmEntity) JsonMapper.mapJsonToObj(retgiftcoupnmdate, GiftcouponmEntity.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;
    }




    public ArrayList<CustomerCouponmEntity> getCouponList(String couponstatus,int Limit,int offset)throws Exception{
        ArrayList<CustomerCouponmEntity> res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);
            res = daocoupon.getCouponList(couponstatus,Limit,offset);
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


    public CouponEntity getCoupon(String couponId)throws Exception{
        CouponEntity res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);
            res = daocoupon.getCoupon(couponId);
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

    public ArrayList<CustomerCouponmEntity> getCampaignCouponList(String campaignId)throws Exception{
        ArrayList<CustomerCouponmEntity> res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);
            res = daocoupon.getCampaignCouponList(campaignId);
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

    public boolean addCoupon(CustomerCouponmEntity campaignModelList) throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);

                if(campaignModelList!=null &&campaignModelList.getCoupon()!=null) {
                    res = daocoupon.addCoupon(campaignModelList.getCoupon());
                    if (res)
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


    public boolean DeleteInActiveRecord() throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);

            res = daocoupon.DeleteInActiveRecord();
                if (res)
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

    public boolean updateCouponflage(String couponstatus) throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);

            if(couponstatus!=null && !couponstatus.equals("")) {
                res = daocoupon.updatecouponflage(couponstatus);
                if (res)
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


    public boolean addCoupon(List<CustomerCouponmEntity> campaignModelList) throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Coupon_Dao daocoupon = dbhelper.getCouponDao(con);

            for (CustomerCouponmEntity customerCouponModel : campaignModelList) {
                CouponEntity CouponData=customerCouponModel.getCoupon();
                if(CouponData!=null && CouponData.getCouponId()!=null) {
                    res = daocoupon.addCoupon(CouponData);
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


}
