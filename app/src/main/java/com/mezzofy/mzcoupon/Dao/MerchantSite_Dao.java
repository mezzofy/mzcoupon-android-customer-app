package com.mezzofy.mzcoupon.Dao;



import android.util.Log;

import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aruna on 05/06/17.
 */

public class MerchantSite_Dao {
    private Connection connection;

    public MerchantSite_Dao(Connection con) {
        connection = con;
    }

    public Boolean addMerchant_Site(SiteEntity siteData)throws SQLException
    {
        boolean res = false;
        String selectQuery = "SELECT * FROM tbl_site  WHERE siteId=?";
        ResultSetMapper<SiteEntity> resultSetMapper=new ResultSetMapper<SiteEntity>();
        SiteEntity siteData1= DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,SiteEntity.class,new Object[]{siteData.getSiteId()});
        if(siteData1!=null) {
            if(siteData!=null)
            deletesite(siteData);
        }

            String sql = "insert into tbl_site (siteId,merchantId,siteName,siteAddress,siteLatitude,siteLongitude,siteLocation," +
                    "siteContact,siteDesc,siteEmailId,siteDisplayWallet,siteSeqNo,siteImageurl,siteOnlineStatus,siteStatus,siteRedeemPass,hashCode," +
                    "locationId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{siteData.getSiteId(), siteData.getMerchantId(), siteData.getSiteName(), siteData.getSiteAddress(), siteData.getSiteLatitude(),
                    siteData.getSiteLongitude(), siteData.getSiteLocation(), siteData.getSiteContact(), siteData.getSiteDesc(), siteData.getSiteEmailId(), siteData.getSiteDisplayWallet(),
                    siteData.getSiteSeqNo(), siteData.getSiteImageurl(), siteData.getSiteOnlineStatus(), siteData.getSiteStatus(), siteData.getSiteRedeemPass(), siteData.getHashCode(), siteData.getLocationId()});
        return res;
    }

    public boolean deletesite(SiteEntity siteData) throws SQLException {
        boolean res = false;
        String sql = "DELETE FROM tbl_site WHERE merchantId=? and siteId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{siteData.getMerchantId(),siteData.getSiteId()});
        return res;
    }

    public boolean deletesitelist() throws SQLException {
        boolean res = false;
        String sql = "DELETE FROM tbl_site";
        res = DatabaseUtils.update(connection, sql,null);
        return res;
    }

    public SiteEntity getMerchantSite(String SiteId)throws SQLException {
        String selectQuery="SELECT * FROM tbl_site WHERE siteId=?";
        ResultSetMapper<SiteEntity>resultSetMapper=new ResultSetMapper<SiteEntity>();
        SiteEntity mechantSite=DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,SiteEntity.class,new Object[]{SiteId});
        return mechantSite;
    }
    public SiteEntity getMerchantSiteData(String SiteId)throws SQLException{
        String selectQuery="SELECT * FROM tbl_site WHERE siteId=?";
        ResultSetMapper<SiteEntity>resultSetMapper=new ResultSetMapper<SiteEntity>();
        SiteEntity siteData=DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,SiteEntity.class,new Object[]{SiteId});
        return siteData;

    }

    public  List<SiteEntity> getcampaignSiteList(String CmpID)throws SQLException{

        List<SiteEntity> sitelistdata = null;
        Log.d("CmpID-----",CmpID);
        String sql = "select * from tbl_site as s inner join tbl_campaign_site as cs  where cs.siteId=s.siteId AND cs.campaignId= ? GROUP by s.siteId";
        Log.d("sql-----",sql);
        ResultSetMapper<SiteEntity>resultSetMapper=new ResultSetMapper<SiteEntity>();
        sitelistdata=DatabaseUtils.queryForList(connection,sql,resultSetMapper, SiteEntity.class,new Object[]{CmpID});

        return sitelistdata;

    }

    public List<SiteEntity> getSitelist()throws SQLException{
        List<SiteEntity> siteList = new ArrayList<SiteEntity>();
        ResultSetMapper<SiteEntity> resultSetMapper = new ResultSetMapper<SiteEntity>();
        String sql = "select * from tbl_site" ;
        siteList=DatabaseUtils.queryForList(connection,sql,resultSetMapper, SiteEntity.class,null);
        return siteList;

    }
    public List<SiteEntity> getSiteActivelist()throws SQLException{
        List<SiteEntity> siteList =null;
        ResultSetMapper<SiteEntity> resultSetMapper = new ResultSetMapper<SiteEntity>();
        String sql = "select * from tbl_site where siteStatus=?" ;
        siteList=DatabaseUtils.queryForList(connection,sql,resultSetMapper, SiteEntity.class,new Object[]{"A"});

        if(siteList!=null)
            return siteList;
        else
            return siteList = new ArrayList<SiteEntity>();
    }

    String trimstringvalues(String temp)
    {
        String  result = temp.replaceAll("[^\\p{L}\\p{Z}]","");
        return result;
    }


    public boolean updateMerchantProfile2(SiteEntity siteData)throws SQLException{
        boolean res = false;

        String sql = "update tbl_site SET siteName=?,siteAddress=?,siteLatitude=?,siteLongitude=?,siteLocation=?,siteContact=?,siteEmailId=?,siteStatus=?," +
                "siteRedeemPass=?,siteImageurl=?,siteDesc=?,merchantId=?,siteSeqNo=?,siteOnlineStatus=?,hashCode=?,locationId=? where siteId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{siteData.getSiteName(),siteData.getSiteAddress(),siteData.getSiteLatitude(),siteData.getSiteLongitude(),
                siteData.getSiteLocation(),siteData.getSiteContact(),siteData.getSiteEmailId(),siteData.getSiteStatus(),siteData.getSiteRedeemPass(),siteData.getSiteImageurl(),
                siteData.getSiteDesc(),siteData.getMerchantId(),siteData.getSiteOnlineStatus(),siteData.hashCode(),siteData.getLocationId(),siteData.getSiteId()});
        return res;
    }


    public boolean updateMerchantSiteProfile2(SiteEntity siteData)throws SQLException{
        boolean res = false;
        String sql = "update tbl_site SET siteName=?,siteAddress=?,siteLatitude=?,siteLongitude=?,siteLocation=?,siteContact=?,siteEmailId=? where siteId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{siteData.getSiteName(),siteData.getSiteAddress(),siteData.getSiteLatitude(),siteData.getSiteLongitude(),
                siteData.getSiteLocation(),siteData.getSiteContact(),siteData.getSiteEmailId(),siteData.getSiteId()});
        return res;
    }

}
