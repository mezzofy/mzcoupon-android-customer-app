package com.mezzofy.mzcoupon.module;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mezzofy.MzCouponAPI.data.SiteDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZSite;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Dao.MerchantSite_Dao;
import com.mezzofy.mzcoupon.Entity.SitemEnity;


import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Merchantsite_Module {
    Gson gson = new Gson();

    private CouponDB dbhelper;
    private MZSite sitemodule;

    public Merchantsite_Module(Context context) {
        dbhelper = new CouponDB(context);
        sitemodule = new MZSite(context);
    }

    public List<SitemEnity> getMerchantsites() {
        List<SitemEnity> sitelst = new ArrayList<SitemEnity>();

        List<SiteDataModel> sitemDataList = sitemodule.GetMerchantSites();

        try {
            Type inputType = new TypeToken<ArrayList<SiteDataModel>>() {
            }.getType();
            Type outputType = new TypeToken<ArrayList<SitemEnity>>() {
            }.getType();
            Gson gson = new Gson();
            String json = gson.toJson(sitemDataList, inputType);
            sitelst = JsonMapper.mapJsonToObjList(json, outputType);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (sitelst.size() > 0)
            return sitelst;
        else
            return sitelst = null;

    }

    public List<SiteEntity> getSiteActivelist() throws Exception {

        List<SiteEntity> sitelist = new ArrayList<>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            sitelist = daosite.getSiteActivelist();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return sitelist;
    }


    public Boolean addMerchantSiteList(List<SitemEnity> siteModelList) throws Exception {

        Boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);

            res = daosite.deletesitelist();

            for (SitemEnity siteModel : siteModelList) {
                SiteEntity siteData = siteModel.getSite();
                res = daosite.addMerchant_Site(siteData);

                if (res)
                    con.commit();
                else
                    con.rollback();

            }

        } catch (SQLException e) {
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

    public Boolean addMerchantSite(SiteEntity siteData) throws Exception {

        Boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);

            daosite.addMerchant_Site(siteData);
            if (res)
                con.commit();
            else
                con.rollback();


        } catch (SQLException e) {
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


    public List<SiteEntity> getSitelist() throws Exception {

        List<SiteEntity> sitelist = new ArrayList<>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            sitelist = daosite.getSitelist();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return sitelist;
    }


    public List<SiteEntity> getcampaignSiteList(String CmpID) throws Exception {

        List<SiteEntity> sitelist = new ArrayList<>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            sitelist = daosite.getcampaignSiteList(CmpID);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return sitelist;
    }


    public SiteEntity getMerchantSite(String siteId) throws Exception {

        SiteEntity mechantSite = new SiteEntity();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            mechantSite = daosite.getMerchantSite(siteId);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return mechantSite;
    }

    public Boolean updateMerchantProfile2(SiteEntity siteData) throws Exception {

        Boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            res = daosite.updateMerchantProfile2(siteData);

            if (res)
                con.commit();
            else
                con.rollback();

        } catch (SQLException e) {
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


    public Boolean updateMerchantSiteProfile2(SiteEntity siteData) throws Exception {

        Boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            res = daosite.updateMerchantSiteProfile2(siteData);

            if (res)
                con.commit();
            else
                con.rollback();

        } catch (SQLException e) {
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


    public SiteEntity getMerchantSiteData(String siteId) throws Exception {

        SiteEntity mechantSite = new SiteEntity();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            MerchantSite_Dao daosite = dbhelper.getMerchantSiteDao(con);
            mechantSite = daosite.getMerchantSiteData(siteId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return mechantSite;
    }


}
