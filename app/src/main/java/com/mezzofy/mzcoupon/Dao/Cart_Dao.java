package com.mezzofy.mzcoupon.Dao;

import android.util.Log;

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aruna on 9/25/17.
 */

public class Cart_Dao {
    private Connection connection;

    public Cart_Dao(Connection con) {
        connection = con;
    }

    public Boolean addCart(CartEntity cartData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<CartEntity> resultSetMapper=new ResultSetMapper<CartEntity>();

        Log.d("CartEntity",cartData.toString());
        String sql = "SELECT * FROM tbl_cart  where campaignId=?";
        CartEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,CartEntity.class, new Object[]{cartData.getCampaignId()});

        if(res1!=null)
        {
            int total=Integer.valueOf(res1.getProductQty())+Integer.valueOf(cartData.getProductQty());
            double sellingprice=Double.valueOf(cartData.getSellingPrice());
            double price=total*sellingprice;
                sql = "update tbl_cart SET productQty=?,totalPrice=? where campaignId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{String.valueOf(total),String.valueOf(price),cartData.getCampaignId()});
        }
        else {

            sql = "insert into tbl_cart (campaignId,merchantId,orginalPrice,campaignCode,campaignName,campaignDesc,sellingPrice," +
                    "campaignTc,productQty,campaignImage,totalPrice,status,flag) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            res = DatabaseUtils.update(connection, sql, new Object[]{cartData.getCampaignId(), cartData.getMerchantId(), cartData.getOrginalPrice(),
                    cartData.getCampaignCode(),cartData.getCampaignName(), cartData.getCampaignDesc(),cartData.getSellingPrice(),
                    cartData.getCampaignTc(),cartData.getProductQty(),cartData.getCampaignImage(),cartData.getTotalPrice(),
                    cartData.getStatus(),"N"});
        }

        return res;
    }

    public Integer getItemcart() throws SQLException {
        Integer listLookup = 0;

        String sql = "SELECT COUNT(*) FROM tbl_cart";
        listLookup = DatabaseUtils.queryForGeneric(connection, sql, Integer.class, null);

        return listLookup;
    }

    public List<CartEntity> getCartList() throws SQLException{

        List<CartEntity> cartDataList = null;
        String sql = "select * from tbl_cart where status=?";
        ResultSetMapper<CartEntity> resultSetMapper = new ResultSetMapper<CartEntity>();
        cartDataList=DatabaseUtils.queryForList(connection,sql,resultSetMapper, CartEntity.class,new Object[]{"A"});
        return cartDataList;

    }


    public CartEntity getCartData(String ProductId) throws SQLException{

        CartEntity cartDataList = null;
        String sql = "select * from tbl_cart where campaignId=?";
        ResultSetMapper<CartEntity> resultSetMapper = new ResultSetMapper<CartEntity>();
        cartDataList=DatabaseUtils.queryForObj(connection,sql,resultSetMapper, CartEntity.class,new Object[]{ProductId});
        return cartDataList;

    }

    public Boolean updateQty(String campaignId,String qty,double total)throws SQLException{
        Boolean res=false;
        String sql = "update tbl_cart SET productQty=?,totalPrice=? where campaignId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{qty,String.valueOf(total),campaignId});
        return res;

    }

    public Boolean deletedchart(String campaignId)throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_cart where  campaignId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{campaignId});
        return res;

    }

    public Float getTotalcart() throws SQLException {
        float listLookup = 0f;

        String sql = "select sum(totalPrice) from tbl_cart";
        listLookup = DatabaseUtils.queryForGeneric(connection, sql, Float.class, null);

        return listLookup;
    }

    public Boolean deletechart()throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_cart";
        res = DatabaseUtils.update(connection, sql,null);
        return res;

    }
}
