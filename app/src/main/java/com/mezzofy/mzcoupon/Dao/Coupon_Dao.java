package com.mezzofy.mzcoupon.Dao;

import android.util.Log;

import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aruna on 8/18/17.
 */

public class Coupon_Dao {
    private Connection connection;

    public Coupon_Dao(Connection con) {
        connection = con;
    }

    public Boolean addCoupon(CouponEntity couponData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<CouponEntity> resultSetMapper=new ResultSetMapper<CouponEntity>();

        Log.d("CouponEntity",couponData.toString());
        String sql = "SELECT * FROM tbl_coupon  where couponId=?";
        CouponEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,CouponEntity.class, new Object[]{couponData.getCouponId()});

        if(res1!=null)
        {
            if(couponData.getHashCode()==res1.getHashCode()) {
                sql = "update tbl_coupon SET deleteflag=? where couponId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{"N",couponData.getCouponId()});
                return true;
            }
            else
            {
                sql = "update tbl_coupon SET productId=?,allocationId=?,campaignId=?,customerId=?,campaignCode=?,couponNo=?,purchaseDate=?,redeemDate=?,startDate=?," +
                        "endDate=?,couponName=?,couponStatus=?,hashCode=?,createdOn=?,updatedOn=?," +
                        "productNote1=?,productNote2=?,productNote3=?,sellingPrice=?,orginalPrice=?,productDesc=?,productImageurl=?,deleteflag=? where couponId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{couponData.getProductId(), couponData.getAllocationId(), couponData.getCampaignId(), couponData.getCustomerId(),
                        couponData.getCampaignCode(),couponData.getCouponNo(), couponData.getPurchaseDate(),couponData.getRedeemDate(),couponData.getStartDate(),couponData.getEndDate(),
                        couponData.getCouponName(),couponData.getCouponStatus(),couponData.hashCode(),couponData.getCreatedOn(),couponData.getUpdatedOn(),couponData.getProductNote1(),
                        couponData.getProductNote2(), couponData.getProductNote3(), couponData.getSellingPrice(), couponData.getOrginalPrice(),
                        couponData.getProductDesc(),couponData.getProductImageurl(),"N",couponData.getCouponId()});
            }
        }
        else {

            sql = "insert into tbl_coupon (productId,allocationId,campaignId,customerId,campaignCode,couponNo,purchaseDate,redeemDate,startDate,endDate,couponName," +
                    "couponStatus,hashCode,createdOn,updatedOn,productNote1,productNote2,productNote3,sellingPrice,orginalPrice,productDesc,productImageurl,couponId,deleteflag) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{couponData.getProductId(), couponData.getAllocationId(), couponData.getCampaignId(), couponData.getCustomerId(),
                    couponData.getCampaignCode(),couponData.getCouponNo(), couponData.getPurchaseDate(),couponData.getRedeemDate(),couponData.getStartDate(),couponData.getEndDate(),
                    couponData.getCouponName(),couponData.getCouponStatus(),couponData.hashCode(),couponData.getCreatedOn(),couponData.getUpdatedOn(),couponData.getProductNote1(),
                    couponData.getProductNote2(), couponData.getProductNote3(), couponData.getSellingPrice(), couponData.getOrginalPrice(),
                    couponData.getProductDesc(),couponData.getProductImageurl(), couponData.getCouponId()});
        }

        return res;
    }

    public ArrayList<CustomerCouponmEntity> getCouponList(String couponstatus,int Limit,int offset) throws SQLException{

        ArrayList<CustomerCouponmEntity> couponModelArrayList=new ArrayList<CustomerCouponmEntity>();
        String Sql="SELECT * FROM tbl_coupon WHERE deleteflag=? and couponStatus=? ORDER BY couponName  ASC LIMIT ?,?";

        Log.d("getCouponList-----",Sql);
        Log.d("couponStatus-----",couponstatus);

        List<CouponEntity> couponlist=new ArrayList<CouponEntity>();
        ResultSetMapper<CouponEntity>resultSetMapper=new ResultSetMapper<CouponEntity>();
        couponlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CouponEntity.class,new Object[]{"N",couponstatus,Limit,offset} );

        if(couponlist!=null) {
            for (CouponEntity ret : couponlist) {
                CustomerCouponmEntity customerCouponModel = new CustomerCouponmEntity();

                if (ret.getCouponId() != null) {
                    customerCouponModel.setCoupon(ret);
                    couponModelArrayList.add(customerCouponModel);
                }
            }
        }
        return couponModelArrayList;
    }

    public Boolean DeleteInActiveRecord() throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_coupon where deleteflag=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{"Y"});
        return res;

    }

    public ArrayList<CustomerCouponmEntity> getCampaignCouponList(String campaignID) throws SQLException{

        ArrayList<CustomerCouponmEntity> couponModelArrayList=new ArrayList<CustomerCouponmEntity>();
        String Sql="SELECT * FROM tbl_coupon WHERE deleteflag=? and campaignId=? and couponStatus=?";
        Log.d("getCouponList-----",Sql);
        Log.d("couponStatus-----",campaignID);

        List<CouponEntity> couponlist=new ArrayList<CouponEntity>();
        ResultSetMapper<CouponEntity>resultSetMapper=new ResultSetMapper<CouponEntity>();
        couponlist=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, CouponEntity.class,new Object[]{"N",campaignID,"A"} );

        if(couponlist!=null) {
            for (CouponEntity ret : couponlist) {
                CustomerCouponmEntity customerCouponModel = new CustomerCouponmEntity();

                if (ret.getCouponId() != null) {
                    customerCouponModel.setCoupon(ret);
                    couponModelArrayList.add(customerCouponModel);
                }
            }
        }
        return couponModelArrayList;
    }


    public CouponEntity getCoupon(String couponId) throws SQLException{
        CouponEntity couponData=new CouponEntity();
        String Sql="SELECT * FROM tbl_coupon WHERE couponId=?";
        ResultSetMapper<CouponEntity>resultSetMapper=new ResultSetMapper<CouponEntity>();
        couponData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, CouponEntity.class,new Object[]{couponId});
        return couponData;
    }


    public Boolean addCouponCount(String couponId,String size)throws SQLException {
        Boolean res = false;

        String sql = "insert into tbl_couponcount (couponId,size)values(?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{couponId,size});
        return res;
    }



    public boolean updatecouponflage(String couponStatus)throws SQLException{
        boolean res = false;
        String sql = "update tbl_coupon SET deleteflag = 'Y' where couponStatus = ?";
        res = DatabaseUtils.update(connection, sql, new Object[]{couponStatus});
        return res;
    }


    public CampaignmEntity getCouponById(String CmpId)  throws SQLException{

        CampaignmEntity campaignModel=new CampaignmEntity();
        String Sql="SELECT * FROM tbl_campaign WHERE campaignId=?";
        ResultSetMapper<CampaignEntity>resultSetMapper=new ResultSetMapper<CampaignEntity>();
        CampaignEntity campaignData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, CampaignEntity.class,new Object[]{CmpId});
        campaignModel.setCampaign(campaignData);
        return campaignModel;
    }


}
