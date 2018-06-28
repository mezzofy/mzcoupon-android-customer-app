package com.mezzofy.mzcoupon.Dao;


import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Aruna on 06/06/17.
 */

public class Merchant_Dao {

    private Connection connection;

    public Merchant_Dao(Connection con) {
        connection = con;
    }

    public Boolean addMerchant(MerchantEntity merchantData)throws SQLException{
        Boolean res=false;
        ResultSetMapper<MerchantEntity> resultSetMapper=new ResultSetMapper<MerchantEntity>();
        String sql="SELECT * FROM tbl_merchant WHERE merchantId=?";
        MerchantEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,MerchantEntity.class,new Object[]{merchantData.getMerchantId()});
        if(res1!=null)
            DeleteMerchant(merchantData.getMerchantId());

            sql = "insert into tbl_merchant (countryCode,merchantId,merchantDesc,merchantCode,merchantType,merchantName,merchantLogourl," +
                    "merchantImageurl,merchantHotline,merchantTc,profileStatus,merchantStatus,merchantEmail,CountryName,hashCode,channelCode,currency) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{merchantData.getCountryCode(), merchantData.getMerchantId(), merchantData.getMerchantDesc(), merchantData.getMerchantCode(), merchantData.getMerchantType(),
                    merchantData.getMerchantName(), merchantData.getMerchantLogourl(), merchantData.getMerchantImageurl(), merchantData.getMerchantHotline(), merchantData.getMerchantTc(), merchantData.getProfileStatus(),
                    merchantData.getMerchantStatus(), merchantData.getMerchantEmail(), merchantData.getCountryName(), merchantData.getHashCode(),merchantData.getChannelCode(),merchantData.getCurrency()});
            return res;
    }


    public boolean DeleteMerchant(String Id) throws SQLException {
        boolean res = false;
        String sql = "DELETE FROM tbl_merchant where merchantId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{Id});
        return res;
    }

    public MerchantEntity getMerchantList(String merchantId)throws SQLException {
        String selectQuery="SELECT * FROM tbl_merchant WHERE merchantId=?";
        ResultSetMapper<MerchantEntity> resultSetMapper=new ResultSetMapper<MerchantEntity>();
        MerchantEntity merchantData= DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,MerchantEntity.class,new Object[]{merchantId});
        return merchantData;
    }

    public MerchantEntity getMerchantList()throws SQLException {
        String selectQuery="SELECT * FROM tbl_merchant";
        ResultSetMapper<MerchantEntity> resultSetMapper=new ResultSetMapper<MerchantEntity>();
        MerchantEntity merchantData= DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,MerchantEntity.class,null);
        return merchantData;
    }

    public boolean updateMerchantProfile1(MerchantEntity merchantData)throws SQLException{
        boolean res = false;
        String sql = "update tbl_merchant SET merchantLogourl=?,merchantHotline=?,merchantDesc=?,channelCode=? where merchantId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{merchantData.getMerchantLogourl(),merchantData.getMerchantHotline(),merchantData.getMerchantDesc(),
                merchantData.getChannelCode(),merchantData.getMerchantId()});
        return res;
    }

    public boolean updateMerchantSetting(MerchantEntity merchantData)throws SQLException{
        boolean res = false;
        String sql = "update tbl_merchant SET merchantLogourl=?,merchantImageurl=?,merchantDesc=?,merchantName=?,merchantHotline=?,merchantCode=?,profileStatus=? where merchantId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{merchantData.getMerchantLogourl(),merchantData.getMerchantImageurl(),merchantData.getMerchantDesc(),merchantData.getMerchantName(),
                merchantData.getMerchantHotline(),merchantData.getMerchantCode(),merchantData.getProfileStatus(),merchantData.getMerchantId()});
        return res;
    }


    public boolean DeleteChartTable() throws SQLException {
        boolean res = false;
        String sql = "DELETE FROM tbl_Chart_values";
        res = DatabaseUtils.update(connection, sql, null);
        return res;
    }

}
