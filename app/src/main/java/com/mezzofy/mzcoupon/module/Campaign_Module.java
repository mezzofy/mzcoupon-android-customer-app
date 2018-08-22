package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.mezzofy.mzcoupon.Entity.CampGrpDetailEntity;
import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CampaignsEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.Dao.Campaign_Dao;
import com.mezzofy.mzcoupon.Entity.CampaignGroupDtlmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignGroupListmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignGroupmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignSearchmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;

import org.json.JSONException;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import mezzofy.com.libmzcoupon.data.CampaignDataModel;
import mezzofy.com.libmzcoupon.data.CampaignGroupListDataModel;
import mezzofy.com.libmzcoupon.data.CampaignSearchDataModel;
import mezzofy.com.libmzcoupon.data.CampaignsData;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZCampaign;
import mezzofy.com.libmzcoupon.utills.APIServerException;


/**
 * Created by aruna on 7/28/17.
 */

public class Campaign_Module {
    private CouponDB dbhelper;
    SharedPreferences settings;
    MZCampaign campaignModule;

    public Campaign_Module(Context context) {
        dbhelper = new CouponDB(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        campaignModule = new MZCampaign(context);
    }


    public List<CampaignmEntity> getCampaignsfromServerAPI(int pagesize, double latitude, double longitude) {

        List<CampaignmEntity> campaignsEntityList = null;
        try {
            CampaignsData campaigns = campaignModule.getCampaigns(pagesize, latitude, longitude);
            if (campaigns != null) {

                CampaignsEntity campaignsEntity = (CampaignsEntity) JsonMapper.mapJsonToObj(campaigns, CampaignsEntity.class);
                campaignsEntityList=campaignsEntity.getCampaigns();

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("size", ObjectSerializer.serialize(campaignsEntity.getSize()));
                editor.commit();


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return campaignsEntityList;
    }



    public List<CampaignmEntity> getCampaignsFilterfromServerAPI(CampaignSearchmEntity campaignSearchModel) throws APIServerException {
        List<CampaignmEntity> resp = null;


        try {
            CampaignSearchDataModel campaignSearchmData=(CampaignSearchDataModel) JsonMapper.mapJsonToObj(campaignSearchModel, CampaignSearchDataModel.class);
            List<CampaignDataModel> campaignmDatas=campaignModule.getCampaignsWithFilter(campaignSearchmData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (APIServerException e) {
            e.printStackTrace();
        }


        return resp;
    }

    public CampaignEntity getCampaignDetailfromServerAPI(String campaignId, double latitude, double longitude) {
        CampaignEntity campaignEntity = null;
        try {
            CampaignDataModel campaignsData = campaignModule.getCampaignById(campaignId, latitude, longitude);
            if (campaignsData != null) {
                CampaignmEntity campaignmEntity = (CampaignmEntity) JsonMapper.mapJsonToObj(campaignsData, CampaignmEntity.class);

                campaignEntity = campaignmEntity.getCampaign();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return campaignEntity;
    }

    public List<CampaignGroupmEntity> getCampaignsGroup() {
        List<CampaignGroupmEntity> campaignGroupModels = null;

        CampaignGroupListDataModel campaignGroupListmData=campaignModule.getCampaignGroups();
        if(campaignGroupListmData!=null)
        {
            try {
                CampaignGroupListmEntity campaignGroupListmEntity=(CampaignGroupListmEntity) JsonMapper.mapJsonToObj(campaignGroupListmData, CampaignGroupListmEntity.class);

                if(campaignGroupListmEntity!=null){
                    campaignGroupModels=campaignGroupListmEntity.getCampaigngroups();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return campaignGroupModels;
    }


    public int checkproduct(String campaignId, int qty) {

        int Qty=0;
        Qty=campaignModule.checkProductAvilability(campaignId,qty);

        return Qty;

    }


    public List<CampaignmEntity> gettopcoupon() {
        List<CampaignmEntity> campaignsDataList = null;

        CampaignsData campaignsData=campaignModule.getCampaignsTop();
        if(campaignsData!=null)
        {
            try {
                CampaignsEntity sCampaignss=(CampaignsEntity)JsonMapper.mapJsonToObj(campaignsData, CampaignsEntity.class);
                campaignsDataList=sCampaignss.getCampaigns();
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("size", ObjectSerializer.serialize(sCampaignss.getSize()));
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return campaignsDataList;
    }

//    public String getCampaignsCode() {
//
//        String code = campaignModule.getCampaignsCode();
//        return code;
//    }


    public boolean addCampaignGroupdata(List<CampGrpEntity> campaignGroupdata) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);


            for (CampGrpEntity rst : campaignGroupdata) {
                res = daocampagin.addcampaigngroup(rst);

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


    public boolean DeleteCampaign() throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.DeleteCampaign();

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

    public boolean DeleteInActiveRecord() throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.DeleteInActiveRecord();
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


    public boolean updateCampaignflage() throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.updatecampaignflage();
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

    public boolean addCampaignlist(List<CampaignmEntity> campaignModelList) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);

            for (CampaignmEntity campaignModel : campaignModelList) {
                res = daocampagin.addCampaign(campaignModel);
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


    public boolean addcampaignSize(String size, String CampaignId) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampaign = dbhelper.getcampaignDao(con);
            res = daocampaign.addCampignSize(size, CampaignId);

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


    public boolean addToptenCampaignlist(List<CampaignmEntity> campaignModelList, int offset) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);

            if (offset == 1)
                res = daocampagin.updatecampaignToptenflage();

            for (CampaignmEntity campaignModel : campaignModelList) {
                res = daocampagin.addToptenCampaign(campaignModel);
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


    public boolean addCampaignGroup(List<CampaignGroupmEntity> campaignGroupModels) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);


            for (CampaignGroupmEntity campGrpDetailData : campaignGroupModels) {
                res = daocampagin.DeleteCampaignGrorp(campGrpDetailData.getCampaigngroup().getCampgrpId());
                res = daocampagin.DeleteCampaignGroupDetail(campGrpDetailData.getCampaigngroup().getCampgrpId());
                res = daocampagin.addcampaigngroup(campGrpDetailData.getCampaigngroup());
                for (CampaignGroupDtlmEntity campaignGroupDtlModel : campGrpDetailData.getCampGrpDetails()) {
                    res = daocampagin.addcampaigngroupDetail(campaignGroupDtlModel.getCampGrpDetail());
                }

            }

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


    public boolean addCampaign(CampaignmEntity campaignModel) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);

            res = daocampagin.addCampaign(campaignModel);
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


    public boolean addCampaign(List<CustomerCouponmEntity> campaignGroupModels) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);

            for (CustomerCouponmEntity customerCouponModel : campaignGroupModels) {
                if (customerCouponModel != null && customerCouponModel.getCampaign() != null)
                    res = daocampagin.addCampaign(customerCouponModel.getCampaign());
                if (customerCouponModel.getSize() != null)
                    res = res && daocampagin.addCampignSize(customerCouponModel.getCampaign().getCampaignId(), customerCouponModel.getSize());

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


    public List<CampaignEntity> getCampaignSizeList() throws Exception {

        List<CampaignEntity> camaignModellist = new ArrayList<CampaignEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignSizeList();
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
        return camaignModellist;
    }


    public List<CampGrpDetailEntity> getCampaginGrpProductlist(String CamGrpId) throws Exception {

        List<CampGrpDetailEntity> campaignDataArrayList = new ArrayList<CampGrpDetailEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            campaignDataArrayList = daocampagin.getCampaginGrpProductlist(CamGrpId);
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
        return campaignDataArrayList;
    }


    public ArrayList<CampaignGroupDtlmEntity> getCampaginGrpDtllist(String CamGrpId) throws Exception {

        ArrayList<CampaignGroupDtlmEntity> campaignDataArrayList = new ArrayList<CampaignGroupDtlmEntity>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            campaignDataArrayList = daocampagin.getCampaginGrpDtllist(CamGrpId);
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
        return campaignDataArrayList;
    }


    public List<CampGrpEntity> getCampaginGrpDtllist() throws Exception {

        List<CampGrpEntity> CampGrpData = new ArrayList<CampGrpEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            CampGrpData = daocampagin.getCampaginGrpDtllist();
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
        return CampGrpData;
    }

    public ArrayList<CampaignmEntity> getCampaignList(ArrayList<CampaignGroupDtlmEntity> Filterres) throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignList(Filterres);

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
        return camaignModellist;
    }

    public ArrayList<CampaignmEntity> getCampaigngroupList(List<CampGrpDetailEntity> rest) throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignproductList(rest);

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
        return camaignModellist;
    }

    public CampaignmEntity getCampaign(String camId) throws Exception {
        CampaignmEntity campaignModel = new CampaignmEntity();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            campaignModel = daocampagin.getCampaign(camId);

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
        return campaignModel;
    }

    public Integer getCampaignCount(String CmpStatus) throws Exception {

        Integer camaignlist = 0;
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignlist = daocampagin.getCampaignCount(CmpStatus);
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
        return camaignlist;
    }


    public ArrayList<CampaignGroupmEntity> getCampaignGrouplist() throws Exception {
        ArrayList<CampaignGroupmEntity> campaignGroupModels = new ArrayList<CampaignGroupmEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            campaignGroupModels = daocampagin.getCampaignGrouplist();

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
        return campaignGroupModels;
    }

    public ArrayList<CampaignmEntity> getToptemCampaignList() throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getToptemCampaignList();

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
        return camaignModellist;
    }

    public ArrayList<CampaignmEntity> getCampaignList(int Limit, int Offset) throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        ;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignList(Limit, Offset);

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
        return camaignModellist;
    }

    public int getFavouriteCount() throws Exception {

        int favcount = 0;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            favcount = daocampagin.getFavouriteCount();

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
        return favcount;
    }


    public ArrayList<CampaignmEntity> getCampaignfavList() throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignfavList();

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
        return camaignModellist;
    }

    public ArrayList<CampaignmEntity> getCampaignList() throws Exception {

        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignList();

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
        return camaignModellist;
    }


    public CampaignmEntity DeleteCampaignimage(String cmpId, String pImageId) throws Exception {
        CampaignmEntity campaignModel = null;
        Connection con = null;

        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            campaignModel = daocampagin.DeleteCampaignimage(cmpId, pImageId);

            if (campaignModel != null)
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
        return campaignModel;
    }

    public ArrayList<CampaignmEntity> getCampaignListSearch(String CmpStatus, String SearchText,int Limit,int Offset) throws Exception {
        ArrayList<CampaignmEntity> camaignModellist = new ArrayList<CampaignmEntity>();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            camaignModellist = daocampagin.getCampaignListSearch(CmpStatus, SearchText,Limit,Offset);

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
        return camaignModellist;
    }

    public Boolean updateCampaignStatus(String campaignId, String campaignStatus) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.updateCampaignStatus(campaignId, campaignStatus);

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


    public Boolean updateCampaignTotalAllocation(String campaignId, int count) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.updateCampaignTotalAllocation(campaignId, count);

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

    public Boolean updateCampaignFavourite(String campaignId, String fav) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Campaign_Dao daocampagin = dbhelper.getcampaignDao(con);
            res = daocampagin.updateCampaignFavourite(campaignId, fav);

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

}
