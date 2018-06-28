package com.mezzofy.mzcoupon.Dao;

import android.util.Log;

import com.mezzofy.mzcoupon.Entity.CampGrpDetailEntity;
import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.CampaginSiteEntity;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CampaignImageEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;
import com.mezzofy.mzcoupon.Entity.CampaignGroupDtlmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignGroupmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignImagemEntity;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.SitemEnity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aruna on 7/28/17.
 */

public class Campaign_Dao {
    private Connection connection;

    public Campaign_Dao(Connection con) {
        connection = con;
    }

    public Boolean addcampaignimages(CampaignImagemEntity campaignImageModel)throws SQLException {
        Boolean res=false;
        CampaignImageEntity campaignImageData1=campaignImageModel.getCampaignimage();
        String sql = "insert into tbl_campaign_image (pimageId,campaignId,campaignImage) values(?,?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{campaignImageData1.getPimageId(), campaignImageData1.getCampaignId(), campaignImageData1.getCampaignImage()});
        return res;
    }

    public Boolean addcampaigngroupDetail(CampGrpDetailEntity campGrpDetailData)throws SQLException{
        Boolean res=false;
        String sql = "insert into tbl_campaign_group_detail (campaignId,campgrpId) values(?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{campGrpDetailData.getCampaignId(), campGrpDetailData.getCampgrpId()});
        return res;

    }

    public Boolean addcampaignSite(String SiteId,String CmpID)throws SQLException{
        Boolean res=false;
        String sql = "insert into tbl_campaign_site (campaignId,siteId) values(?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{CmpID, SiteId});
        return res;

    }




    public Boolean updateCampaignStatus(String campaignId, String campaignStatus)throws SQLException{
        Boolean res=false;
        String sql = "update tbl_campaign set campaignStatus=? where campaignId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{campaignStatus, campaignId});
        return res;

    }

    public Boolean updateCampaignFavourite(String campaignId, String fav)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_campaign set Favourite=? where campaignId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{fav, campaignId});
        return res;
    }

    public Integer getFavouriteCount() throws SQLException {
        Integer favCount = 0;

        String sql = "SELECT COUNT(*) FROM tbl_campaign WHERE Favourite=?";
        favCount = DatabaseUtils.queryForGeneric(connection, sql, Integer.class, new Object[]{"Y"});


        if(favCount==null)
            favCount = 0;

        return favCount;
    }

    public Boolean updateCampaignTotalAllocation(String campaignId, int count)throws SQLException{
        Boolean res=false;
        String sql = "update tbl_campaign set allocationcoupon=? where campaignId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{count, campaignId});
        return res;

    }

    public Boolean DeleteInActiveRecord() throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_campaign where deleteflag=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{"Y"});
        return res;

    }

    public Boolean DeleteCampaignInID(String CmpID)throws SQLException{
        Boolean res=false;
        String sql;
        sql = "delete from tbl_campaign_image where campaignId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{CmpID});
        sql = "delete from tbl_campaign_site where campaignId=?";
        res = res && DatabaseUtils.update(connection, sql, new Object[]{CmpID});
        sql = "delete from tbl_campaign_group_detail where campaignId=?";
        res = res && DatabaseUtils.update(connection, sql, new Object[]{CmpID});
        sql = "delete from tbl_couponcount where campaignId=?";
        res = res && DatabaseUtils.update(connection, sql, new Object[]{CmpID});
        return res;

    }

    public Boolean DeleteCampaignGrorp(String camgrpid)throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_campaign_group where  campgrpId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{camgrpid});
        return res;

    }


    public Boolean DeleteCampaignGroupDetail(String camgrpid)throws SQLException{
        Boolean res=false;
        String  sql = "delete from tbl_campaign_group_detail where campgrpId=?";
        DatabaseUtils.update(connection, sql,new Object[]{camgrpid});
        return res;
    }

    public Boolean DeleteCampaign()throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_campaign";
        res = DatabaseUtils.update(connection, sql, null);
        if(res)
        {
            sql = "delete from tbl_campaign_image";
            DatabaseUtils.update(connection, sql, null);
            sql = "delete from tbl_campaign_site";
            DatabaseUtils.update(connection, sql, null);
            sql = "delete from tbl_campaign_group_detail";
            DatabaseUtils.update(connection, sql,null);
        }
        return res;

    }
    public Boolean  addCampaign(CampaignmEntity campaignModel)throws SQLException{
        Boolean res=false;
        CampaignEntity campaignData = campaignModel.getCampaign();
        List<CampGrpDetailEntity> groups = campaignModel.getGroups();
        List<CampaignImagemEntity> campaignimages = campaignData.getCampaignimages();
        List<SitemEnity>campignSite=campaignData.getSites();
        ResultSetMapper<CampaignEntity> resultSetMapper=new ResultSetMapper<CampaignEntity>();

        String sql = "SELECT * FROM tbl_campaign  where campaignId=?";
        CampaignEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,CampaignEntity.class, new Object[]{campaignData.getCampaignId()});

        if(res1!=null)
        {
                sql = "update tbl_campaign SET productId=?,merchantId=?,productmerchantId=?,campaignCode=?,campaignType=?,brand=?,campaignNote1=?,campaignNote2=?,campaignNote3=?," +
                        "distance=?,campaignUuid=?,typeService=?,reviewUrl=?,videoUrl=?,dailyLimitType=?," +
                        "campaignName=?,campaignDesc=?,campaignTc=?,orginalPrice=?,sellingPrice=?,campaignStatus=?,dayFilter=?,dailyLimit=?,packQty=?,emailStaff=?," +
                        "couponOver=?,expiryDays=?,expirydue=?,expiryname=?,totalRedeem=?,allocationCount=?,fromDate=?,toDate=?,couponUrl=?,passbookUrl=?,issuedcoupon=?," +
                        "txBrand=?,campaignRemark=?,pickup=?,delivery=?,booking=?,outcall=?,"+
                        "redeemcoupon=?,allocationcoupon=?,hashCode=?,deleteflag='N' where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                        campaignData.getCampaignType(), campaignData.getBrand(),campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                        campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                        campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                        campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                        campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                        campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                        campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                        campaignData.getOutcall(),campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),campaignData.getCampaignId()});

                if(res)
                    DeleteCampaignInID(campaignData.getCampaignId());
        }
        else {

            sql = "insert into tbl_campaign (campaignId,productId,merchantId,productmerchantId,campaignCode,campaignType,brand,campaignNote1,campaignNote2,campaignNote3," +
                    "distance,campaignUuid,typeService,reviewUrl,videoUrl,dailyLimitType,campaignName,campaignDesc,campaignTc,orginalPrice,sellingPrice,campaignStatus,dayFilter,dailyLimit,packQty,emailStaff," +
                    "couponOver,expiryDays,expirydue,expiryname,totalRedeem,allocationCount,fromDate,toDate,couponUrl,passbookUrl,issuedcoupon," +
                    "txBrand,campaignRemark,pickup,delivery,booking,outcall,"+
                    "redeemcoupon,allocationcoupon,hashCode,deleteflag,Topten,Favourite) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getCampaignId(), campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                    campaignData.getCampaignType(), campaignData.getBrand(), campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                    campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                    campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                    campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                    campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                    campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                    campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                    campaignData.getOutcall(), campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),"N","N","N"});
        }

        if(res)
        {
            if (campaignimages != null && campaignimages.size() > 0) {
                for (CampaignImagemEntity campaignImageData : campaignimages) {
                    addcampaignimages(campaignImageData);
                }
            }

            if(campignSite!=null && campignSite.size()>0)
            {
                for(SitemEnity siteModel:campignSite)
                {
                    SiteEntity siteData=siteModel.getSite();
                    addcampaignSite(siteData.getSiteId(),campaignData.getCampaignId());
                }
            }
        }
        return res;
    }

    public Boolean  addCampaign(CampaignEntity campaignData)throws SQLException{
        Boolean res=false;
        ResultSetMapper<CampaignEntity> resultSetMapper=new ResultSetMapper<CampaignEntity>();

        String sql = "SELECT * FROM tbl_campaign  where campaignId=?";
        CampaignEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,CampaignEntity.class, new Object[]{campaignData.getCampaignId()});

        if(res1!=null)
        {
            if(campaignData.getHashCode()==res1.getHashCode()) {
                sql = "update tbl_campaign SET deleteflag=? where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{"N",campaignData.getCampaignId()});
                return true;
            }
            else
            {
                sql = "update tbl_campaign SET productId=?,merchantId=?,productmerchantId=?,campaignCode=?,campaignType=?,brand=?,campaignNote1=?,campaignNote2=?,campaignNote3=?," +
                        "distance=?,campaignUuid=?,typeService=?,reviewUrl=?,videoUrl=?,dailyLimitType=?," +
                        "campaignName=?,campaignDesc=?,campaignTc=?,orginalPrice=?,sellingPrice=?,campaignStatus=?,dayFilter=?,dailyLimit=?,packQty=?,emailStaff=?," +
                        "couponOver=?,expiryDays=?,expirydue=?,expiryname=?,totalRedeem=?,allocationCount=?,fromDate=?,toDate=?,couponUrl=?,passbookUrl=?,issuedcoupon=?," +
                        "txBrand=?,campaignRemark=?,pickup=?,delivery=?,booking=?,outcall=?,"+
                        "redeemcoupon=?,allocationcoupon=?,hashCode=?,deleteflag='N' where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                        campaignData.getCampaignType(), campaignData.getBrand(),campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                        campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                        campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                        campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                        campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                        campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                        campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                        campaignData.getOutcall(),campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),campaignData.getCampaignId()});

            }
        }
        else {

            sql = "insert into tbl_campaign (campaignId,productId,merchantId,productmerchantId,campaignCode,campaignType,brand,campaignNote1,campaignNote2,campaignNote3," +
                    "distance,campaignUuid,typeService,reviewUrl,videoUrl,dailyLimitType,campaignName,campaignDesc,campaignTc,orginalPrice,sellingPrice,campaignStatus,dayFilter,dailyLimit,packQty,emailStaff," +
                    "couponOver,expiryDays,expirydue,expiryname,totalRedeem,allocationCount,fromDate,toDate,couponUrl,passbookUrl,issuedcoupon," +
                    "txBrand,campaignRemark,pickup,delivery,booking,outcall,"+
                    "redeemcoupon,allocationcoupon,hashCode,deleteflag,Topten,Favourite) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getCampaignId(), campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                    campaignData.getCampaignType(), campaignData.getBrand(), campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                    campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                    campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                    campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                    campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                    campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                    campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                    campaignData.getOutcall(), campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),"N","N","N"});
        }

        return res;
    }


    public Boolean addToptenCampaign(CampaignmEntity campaignModel)throws SQLException{
        Boolean res=false;
        CampaignEntity campaignData = campaignModel.getCampaign();
        List<CampGrpDetailEntity> groups = campaignModel.getGroups();
        List<CampaignImagemEntity> campaignimages = campaignData.getCampaignimages();
        List<SitemEnity>campignSite=campaignData.getSites();
        ResultSetMapper<CampaignEntity> resultSetMapper=new ResultSetMapper<CampaignEntity>();

        String sql = "SELECT * FROM tbl_campaign  where campaignId=?";
        CampaignEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,CampaignEntity.class, new Object[]{campaignData.getCampaignId()});

        if(res1!=null)
        {
            if(campaignData.getHashCode()==res1.getHashCode()) {
                sql = "update tbl_campaign SET Topten=? where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{"Y",campaignData.getCampaignId()});
                return true;
            }
            else
            {
                sql = "update tbl_campaign SET productId=?,merchantId=?,productmerchantId=?,campaignCode=?,campaignType=?,brand=?,campaignNote1=?,campaignNote2=?,campaignNote3=?," +
                        "distance=?,campaignUuid=?,typeService=?,reviewUrl=?,videoUrl=?,dailyLimitType=?," +
                        "campaignName=?,campaignDesc=?,campaignTc=?,orginalPrice=?,sellingPrice=?,campaignStatus=?,dayFilter=?,dailyLimit=?,packQty=?,emailStaff=?," +
                        "couponOver=?,expiryDays=?,expirydue=?,expiryname=?,totalRedeem=?,allocationCount=?,fromDate=?,toDate=?,couponUrl=?,passbookUrl=?,issuedcoupon=?," +
                        "txBrand=?,campaignRemark=?,pickup=?,delivery=?,booking=?,outcall=?,"+
                        "redeemcoupon=?,allocationcoupon=?,hashCode=?,deleteflag='N',Topten='Y' where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                        campaignData.getCampaignType(), campaignData.getBrand(),campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                        campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                        campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                        campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                        campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                        campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                        campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                        campaignData.getOutcall(),campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),campaignData.getCampaignId()});

                if(res)
                    DeleteCampaignInID(campaignData.getCampaignId());
            }
        }
        else {

            sql = "insert into tbl_campaign (campaignId,productId,merchantId,productmerchantId,campaignCode,campaignType,brand,campaignNote1,campaignNote2,campaignNote3," +
                    "distance,campaignUuid,typeService,reviewUrl,videoUrl,dailyLimitType,campaignName,campaignDesc,campaignTc,orginalPrice,sellingPrice,campaignStatus,dayFilter," +
                    "dailyLimit,packQty,emailStaff," +
                    "couponOver,expiryDays,expirydue,expiryname,totalRedeem,allocationCount,fromDate,toDate,couponUrl,passbookUrl,issuedcoupon," +
                    "txBrand,campaignRemark,pickup,delivery,booking,outcall,"+
                    "redeemcoupon,allocationcoupon,hashCode,deleteflag,Topten,Favourite) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{campaignData.getCampaignId(), campaignData.getProductId(), campaignData.getMerchantId(), campaignData.getProductmerchantId(), campaignData.getCampaignCode(),
                    campaignData.getCampaignType(), campaignData.getBrand(), campaignData.getCampaignNote1(),campaignData.getCampaignNote2(),campaignData.getCampaignNote3(),
                    campaignData.getDistance(),campaignData.getCampaignUuid(),campaignData.getTypeService(),campaignData.getReviewUrl(),campaignData.getVideoUrl(),campaignData.getDailyLimitType(),
                    campaignData.getCampaignName(), campaignData.getCampaignDesc(), campaignData.getCampaignTc(), campaignData.getOrginalPrice(),
                    campaignData.getSellingPrice(), campaignData.getCampaignStatus(), campaignData.getDayFilter(), campaignData.getDailyLimit(), campaignData.getPackQty(),
                    campaignData.getEmailStaff(), campaignData.getCouponOver(), campaignData.getExpiryDays(), campaignData.getExpirydue(), campaignData.getExpiryname(),
                    campaignData.getTotalRedeem(), campaignData.getAllocationCount(), campaignData.getStartDate(), campaignData.getEndDate(), campaignData.getCouponUrl(), campaignData.getPassbookUrl(),
                    campaignData.getIssuedcoupon(),campaignData.getTxBrand(),campaignData.getCampaignRemark(),campaignData.getPickup(),campaignData.getDelivery(),campaignData.getBooking(),
                    campaignData.getOutcall(), campaignData.getRedeemcoupon(), campaignData.getAllocationcoupon(),campaignData.getHashCode(),"N","Y","N"});
        }

        if(res)
        {
            if (campaignimages != null && campaignimages.size() > 0) {
                for (CampaignImagemEntity campaignImageData : campaignimages) {
                    addcampaignimages(campaignImageData);
                }
            }

            if(campignSite!=null && campignSite.size()>0)
            {
                for(SitemEnity siteModel:campignSite)
                {
                    SiteEntity siteData=siteModel.getSite();
                    addcampaignSite(siteData.getSiteId(),campaignData.getCampaignId());
                }
            }
        }
        return res;
    }


    public Boolean addcampaigngroup(CampGrpEntity campGrpData)throws SQLException{
        Boolean res=false;
        String sql = "insert into tbl_campaign_group (campgrpId,merchantId,campgrpName,campgrpSeq,hashCode,campgrpImageurl) values(?,?,?,?,?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{ campGrpData.getCampgrpId(),campGrpData.getMerchantId(),
                campGrpData.getCampgrpName(), campGrpData.getCampgrpSeq(),campGrpData.getHashCode(),campGrpData.getCampgrpImageurl()});
        return res;

    }

    public List<CampaignEntity> getCampaignSizeList() throws SQLException{
        List<CampaignEntity> TempcampaignDataArrayList=new ArrayList<CampaignEntity>();
        List<CampaignEntity> campaignDataArrayList=new ArrayList<CampaignEntity>();

            String Sql = "SELECT * FROM tbl_campaign c inner join tbl_couponcount cc  on c.campaignId=cc.campaignId where c.deleteflag='N'  ORDER BY c.campaignName ASC";

            ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        TempcampaignDataArrayList=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,null);
        for(CampaignEntity res:TempcampaignDataArrayList){
            ArrayList<CampaignImagemEntity> imageList=getCamImageList(res.getCampaignId());
            if(imageList!=null && imageList.size()>0)
                res.setCampaignimages(imageList);

            campaignDataArrayList.add(res);
        }

        return campaignDataArrayList;
    }


    public Boolean addCampignSize(String campaignid,String size)throws SQLException{
        Boolean res=false;
        String count=null;
        String sql = "SELECT size FROM tbl_couponcount  where campaignId=?";
        count = DatabaseUtils.queryForGeneric(connection, sql, String.class, new Object[]{campaignid});
        if(count==null) {
            sql = "insert into tbl_couponcount (campaignId,size,deleteflag) values(?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{campaignid, size});
        }else
        {
            sql = "update tbl_couponcount SET size=?,deleteflag='N' where campaignId=?";
            res = DatabaseUtils.update(connection, sql, new Object[]{size,campaignid});
        }
        return res;

    }

    public boolean updatecampaignToptenflage()throws SQLException{
        boolean res = false;
        String sql = "update tbl_campaign SET Topten=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{"N"});
        return res;
    }

    public boolean updatecampaignflage()throws SQLException{
        boolean res = false;
        String sql = "update tbl_campaign SET deleteflag=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{"Y"});
        return res;
    }

    public ArrayList<SitemEnity> getSiteList(String CamId)throws SQLException {
        ArrayList<SitemEnity> sitelist = new ArrayList<SitemEnity>();

        Log.d("camId", "----" + CamId + "----");
        List<CampaginSiteEntity> sitelistdata = new ArrayList<CampaginSiteEntity>();
        String sql = "select * from tbl_campaign_site where campaignId=?";
        ResultSetMapper<CampaginSiteEntity> resultSetMapper = new ResultSetMapper<CampaginSiteEntity>();
        sitelistdata=DatabaseUtils.queryForList(connection,sql,resultSetMapper, CampaginSiteEntity.class,new Object[]{CamId});

        if(sitelistdata!=null)
        {
            for(CampaginSiteEntity ret:sitelistdata)
            {
                SitemEnity siteModel=new SitemEnity();
                SiteEntity siteData=new SiteEntity();
                siteData.setSiteId(ret.getSiteId());
                siteModel.setSite(siteData);
                sitelist.add(siteModel);
            }
        }
        return sitelist;
    }

    public ArrayList<CampaignImagemEntity> getCamImageList(String CamId) throws SQLException{
        ArrayList<CampaignImagemEntity> CamImageList = new ArrayList<CampaignImagemEntity>();

        List<CampaignImageEntity> imageDatas = null;

        String sql = "select * from tbl_campaign_image where campaignId=?";
        ResultSetMapper<CampaignImageEntity> resultSetMapper = new ResultSetMapper<CampaignImageEntity>();
        imageDatas=DatabaseUtils.queryForList(connection,sql,resultSetMapper, CampaignImageEntity.class,new Object[]{CamId});


        if(imageDatas!=null) {
            if (imageDatas.size() > 0) {
                for (CampaignImageEntity ret : imageDatas) {
                    CampaignImagemEntity cimg = new CampaignImagemEntity();
                    cimg.setCampaignimage(ret);
                    CamImageList.add(cimg);
                }
            }
        }
        return CamImageList;

    }

    public ArrayList<CampGrpDetailEntity> getGroupList(String CamId) throws SQLException{
        ArrayList<CampGrpDetailEntity> groupslist = new ArrayList<CampGrpDetailEntity>();

        List<CampGrpDetailEntity> campGrpDetailDatas = null;

        String sql = "select * from tbl_campaign_group_detail where campaignId=?";
        ResultSetMapper<CampGrpDetailEntity> resultSetMapper = new ResultSetMapper<CampGrpDetailEntity>();
        campGrpDetailDatas=DatabaseUtils.queryForList(connection,sql,resultSetMapper, CampGrpDetailEntity.class,new Object[]{CamId});


        if(campGrpDetailDatas!=null) {
            if (campGrpDetailDatas.size() > 0) {
                for (CampGrpDetailEntity ret : campGrpDetailDatas) {
                    groupslist.add(ret);
                }
            }
        }
        return groupslist;
    }


    public CampaignmEntity DeleteCampaignimage(String cmpId, String pImageId)throws SQLException {
        CampaignmEntity campaignModel = null;
        Boolean res=false;

        String sql = "delete from tbl_campaign_image where campaignId=? and pimageId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{cmpId,pImageId});
        if(res)
            campaignModel=getCampaign(cmpId);
        return campaignModel;
    }

    public Integer getCampaignCount(String CmpStatus) throws SQLException {
        Integer listLookup = 0;

        String sql = "SELECT COUNT(*) FROM tbl_campaign where campaignId=?";
        listLookup = DatabaseUtils.queryForGeneric(connection, sql, Integer.class, new Object[]{CmpStatus});

        return listLookup;
    }


    public ArrayList<CampaignmEntity> getCampaignList(ArrayList<CampaignGroupDtlmEntity> Filterres) throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();

            for (CampaignGroupDtlmEntity campaignGroupDtlModel : Filterres) {
                String cmpid=campaignGroupDtlModel.getCampGrpDetail().getCampaignId();
                Log.d("Filterres----------",""+cmpid);

                CampaignmEntity campaignModel=new CampaignmEntity();

                String Sql = "SELECT * FROM tbl_campaign WHERE campaignId=?";


                ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
                CampaignEntity campaignData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{cmpid});
                if(campaignData!=null)
                {
                    List<SitemEnity> tempCamSite = getSiteList(campaignData.getCampaignId());

                    if (tempCamSite.size() != 0)
                        campaignData.setSites(tempCamSite);

                    campaignModel.setCampaign(campaignData);

                    List<CampaignImagemEntity> tempCamImagelist = getCamImageList(campaignData.getCampaignId());


                    if (tempCamImagelist.size() != 0)
                        campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

                    camaignModellist.add(campaignModel);
                }
            }
        return camaignModellist;
    }


    public ArrayList<CampaignmEntity> getCampaignproductList(List<CampGrpDetailEntity> Filterres) throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();

        for (CampGrpDetailEntity campaignGroupDtlModel : Filterres) {
            String cmpid=campaignGroupDtlModel.getCampaignId();
            Log.d("Filterres----------",""+cmpid);

            CampaignmEntity campaignModel=new CampaignmEntity();

            String Sql = "SELECT * FROM tbl_campaign WHERE campaignId=?";


            ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
            CampaignEntity campaignData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{cmpid});
            if(campaignData!=null)
            {
                List<SitemEnity> tempCamSite = getSiteList(campaignData.getCampaignId());

                if (tempCamSite.size() != 0)
                    campaignData.setSites(tempCamSite);

                campaignModel.setCampaign(campaignData);

                List<CampaignImagemEntity> tempCamImagelist = getCamImageList(campaignData.getCampaignId());


                if (tempCamImagelist.size() != 0)
                    campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

                camaignModellist.add(campaignModel);
            }
        }
        return camaignModellist;
    }



    public ArrayList<CampaignmEntity> getCampaignfavList() throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();
        String Sql="SELECT * FROM tbl_campaign WHERE deleteflag='N' and Favourite='Y' ORDER BY campaignName  ASC";



        List<CampaignEntity> camaignlist=new ArrayList<CampaignEntity>();
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        camaignlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{});

        if(camaignlist!=null) {
            for (CampaignEntity ret : camaignlist) {
                CampaignmEntity campaignModel = new CampaignmEntity();

                if (ret.getCampaignId() == null)
                    continue;

                List<SitemEnity> tempCamSite = getSiteList(ret.getCampaignId());

                if (tempCamSite.size() != 0)
                    ret.setSites(tempCamSite);

                campaignModel.setCampaign(ret);

                List<CampaignImagemEntity> tempCamImagelist = getCamImageList(ret.getCampaignId());


                if (tempCamImagelist.size() != 0)
                    campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

                camaignModellist.add(campaignModel);


            }
        }
        return camaignModellist;
    }




    public ArrayList<CampaignmEntity> getCampaignList() throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();
        String Sql="SELECT * FROM tbl_campaign WHERE deleteflag='N' ORDER BY campaignName  ASC";



        List<CampaignEntity> camaignlist=new ArrayList<CampaignEntity>();
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        camaignlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{});

        if(camaignlist!=null) {
            for (CampaignEntity ret : camaignlist) {
                CampaignmEntity campaignModel = new CampaignmEntity();

                if (ret.getCampaignId() == null)
                    continue;

                List<SitemEnity> tempCamSite = getSiteList(ret.getCampaignId());

                if (tempCamSite.size() != 0)
                    ret.setSites(tempCamSite);

                campaignModel.setCampaign(ret);

                List<CampaignImagemEntity> tempCamImagelist = getCamImageList(ret.getCampaignId());


                if (tempCamImagelist.size() != 0)
                    campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

//                ArrayList<CampGrpDetailEntity> groupslist = getGroupList(ret.getCampaignId());
//
//                if (groupslist.size() != 0)
//                    campaignModel.setGroups(groupslist);


                camaignModellist.add(campaignModel);


            }
        }
        return camaignModellist;
    }



    public ArrayList<CampaignGroupmEntity> getCampaignGrouplist() throws SQLException{

        ArrayList<CampaignGroupmEntity> campaignGroupModels=new ArrayList<CampaignGroupmEntity>();
        String Sql="SELECT * FROM tbl_campaign_group order by campgrpSeq ASC";

        List<CampGrpEntity> CampGrpData=new ArrayList<CampGrpEntity>();
        ResultSetMapper<CampGrpEntity>resultSetMapper=new ResultSetMapper<CampGrpEntity>();
        CampGrpData=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampGrpEntity.class,null);

        if(CampGrpData!=null) {
            for (CampGrpEntity ret : CampGrpData) {
                CampaignGroupmEntity campaignGroupModel = new CampaignGroupmEntity();

                campaignGroupModel.setCampaigngroup(ret);
                campaignGroupModel.setCampGrpDetails(getCampaginGrpDtllist(ret.getCampgrpId()));

                campaignGroupModels.add(campaignGroupModel);

            }
        }
        return campaignGroupModels;
    }

    public List<CampGrpDetailEntity> getCampaginGrpProductlist(String CampaignGrpId) throws SQLException{

        String Sql="SELECT * FROM tbl_campaign_group_detail where campgrpId=?";

        List<CampGrpDetailEntity> CampGrpDetailData=new ArrayList<CampGrpDetailEntity>();
        ResultSetMapper<CampGrpDetailEntity>resultSetMapper=new ResultSetMapper<CampGrpDetailEntity>();
        CampGrpDetailData=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampGrpDetailEntity.class,new Object[]{CampaignGrpId});

        return CampGrpDetailData;
    }


    public ArrayList<CampaignGroupDtlmEntity> getCampaginGrpDtllist(String CampaignGrpId) throws SQLException{

        ArrayList<CampaignGroupDtlmEntity> campaignGroupDtlModels=new ArrayList<CampaignGroupDtlmEntity>();
        String Sql="SELECT * FROM tbl_campaign_group_detail where campgrpId=?";

        List<CampGrpDetailEntity> CampGrpDetailData=new ArrayList<CampGrpDetailEntity>();
        ResultSetMapper<CampGrpDetailEntity>resultSetMapper=new ResultSetMapper<CampGrpDetailEntity>();
        CampGrpDetailData=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampGrpDetailEntity.class,new Object[]{CampaignGrpId});

        if(CampGrpDetailData!=null) {
            for (CampGrpDetailEntity ret : CampGrpDetailData) {
                CampaignGroupDtlmEntity campaignGroupDtlModel = new CampaignGroupDtlmEntity();

                campaignGroupDtlModel.setCampGrpDetail(ret);
                campaignGroupDtlModels.add(campaignGroupDtlModel);
            }
        }
        return campaignGroupDtlModels;
    }

    public List<CampGrpEntity> getCampaginGrpDtllist() throws SQLException{


        String Sql="SELECT * FROM tbl_campaign_group";

        List<CampGrpEntity> CampGrpDetailData=new ArrayList<CampGrpEntity>();
        ResultSetMapper<CampGrpEntity>resultSetMapper=new ResultSetMapper<CampGrpEntity>();
        CampGrpDetailData=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampGrpEntity.class,null);
        return CampGrpDetailData;
    }


    public ArrayList<CampaignmEntity> getToptemCampaignList() throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();
        String Sql="SELECT * FROM tbl_campaign WHERE  deleteflag='N' and Topten='Y' ORDER BY campaignName  ASC";

        List<CampaignEntity> camaignlist=new ArrayList<CampaignEntity>();
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        camaignlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,null);

        if(camaignlist!=null) {
            for (CampaignEntity ret : camaignlist) {
                CampaignmEntity campaignModel = new CampaignmEntity();

                if (ret.getCampaignId() == null)
                    continue;

                List<SitemEnity> tempCamSite = getSiteList(ret.getCampaignId());

                if (tempCamSite.size() != 0)
                    ret.setSites(tempCamSite);

                campaignModel.setCampaign(ret);

                List<CampaignImagemEntity> tempCamImagelist = getCamImageList(ret.getCampaignId());


                if (tempCamImagelist.size() != 0)
                    campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

//                ArrayList<CampGrpDetailEntity> groupslist = getGroupList(ret.getCampaignId());
//
//                if (groupslist.size() != 0)
//                    campaignModel.setGroups(groupslist);


                camaignModellist.add(campaignModel);


            }
        }
        return camaignModellist;
    }

    public ArrayList<CampaignmEntity> getCampaignList(int Limit, int offset) throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();
        String Sql="SELECT * FROM tbl_campaign WHERE  deleteflag='N' ORDER BY campaignName  ASC LIMIT ?,?";
        Log.d("limite",String.valueOf(Limit));
        Log.d("offset",String.valueOf(offset));


        List<CampaignEntity> camaignlist=new ArrayList<CampaignEntity>();
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        camaignlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{Limit,offset});

        if(camaignlist!=null) {
            for (CampaignEntity ret : camaignlist) {
                CampaignmEntity campaignModel = new CampaignmEntity();

                if (ret.getCampaignId() != null) {

                    ArrayList<SitemEnity> tempCamSite = getSiteList(ret.getCampaignId());

                    if (tempCamSite.size() != 0)
                        ret.setSites(tempCamSite);

                    campaignModel.setCampaign(ret);

                    List<CampaignImagemEntity> tempCamImagelist = getCamImageList(ret.getCampaignId());


                    if (tempCamImagelist.size() != 0)
                        campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

//                ArrayList<CampGrpDetailEntity> groupslist = getGroupList(ret.getCampaignId());
//
//                if (groupslist.size() != 0)
//                    campaignModel.setGroups(groupslist);
                }


                camaignModellist.add(campaignModel);


            }
        }
        return camaignModellist;
    }



    public ArrayList<CampaignmEntity> getCampaignListSearch(String CmpStatus, String SearchText,int Limit,int Offset) throws SQLException{

        ArrayList<CampaignmEntity> camaignModellist=new ArrayList<CampaignmEntity>();
        String Sql="SELECT * FROM tbl_campaign WHERE  deleteflag='N' and campaignName like '%" + SearchText + "%' order by campaignName ASC LIMIT ?,?";

        List<CampaignEntity> camaignlist=new ArrayList<CampaignEntity>();
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        camaignlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{CmpStatus,Limit,Offset});

        for(CampaignEntity ret:camaignlist)
        {
            CampaignmEntity campaignModel=new CampaignmEntity();

            List<SitemEnity>tempCamSite=getSiteList(ret.getCampaignId());

            if(tempCamSite.size()!=0)
                ret.setSites(tempCamSite);

            campaignModel.setCampaign(ret);

            List<CampaignImagemEntity> tempCamImagelist=getCamImageList(ret.getCampaignId());

            if(tempCamImagelist.size()!=0)
                campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

//            ArrayList<CampGrpDetailEntity> groupslist =getGroupList(ret.getCampaignId());
//
//            if(groupslist.size()!=0)
//                campaignModel.setGroups(groupslist);


            camaignModellist.add(campaignModel);


        }
        return camaignModellist;
    }


    public CampaignmEntity getCampaign(String CmpId)  throws SQLException{

        CampaignmEntity campaignModel=new CampaignmEntity();
        String Sql="SELECT * FROM tbl_campaign WHERE campaignId=?";
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        CampaignEntity campaignData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{CmpId});
        if(campaignData!=null)
        {
            List<SitemEnity>tempCamSite=getSiteList(campaignData.getCampaignId());

            if(tempCamSite.size()!=0)
                campaignData.setSites(tempCamSite);

            campaignModel.setCampaign(campaignData);

            List<CampaignImagemEntity> tempCamImagelist=getCamImageList(campaignData.getCampaignId());

            if(tempCamImagelist.size()!=0)
                campaignModel.getCampaign().setCampaignimages(tempCamImagelist);

//            ArrayList<CampGrpDetailEntity> groupslist =getGroupList(campaignData.getCampaignId());
//
//            if(groupslist.size()!=0)
//                campaignModel.setGroups(groupslist);

        }
        return campaignModel;
    }
}
