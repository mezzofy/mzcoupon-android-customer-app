package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Dao.Cart_Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by aruna on 9/25/17.
 */

public class Cart_Module {

    private CouponDB dbhelper;
    SharedPreferences settings;

    public Cart_Module(Context context) {
        dbhelper = new CouponDB(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public boolean addCart(CartEntity cartData) throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Cart_Dao daocart = dbhelper.getCartDao(con);

            res = daocart.addCart(cartData);
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

    public Boolean updateQty(String campaignId,String qty,double total)throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Cart_Dao daocart = dbhelper.getCartDao(con);

            res = daocart.updateQty(campaignId,qty,total);
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

    public CartEntity getCartData(String ProductId)throws Exception{
        CartEntity res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Cart_Dao daocart = dbhelper.getCartDao(con);
            res = daocart.getCartData(ProductId);

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

    public List<CartEntity> getCartList() throws Exception{
        List<CartEntity> res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Cart_Dao daocart = dbhelper.getCartDao(con);
            res = daocart.getCartList();

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

    public Integer getItemcart() throws Exception{
        Integer res = 0;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Cart_Dao daocart = dbhelper.getCartDao(con);

            res = daocart.getItemcart();

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

    public String getTotalcart() throws Exception{
        String res = null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            Cart_Dao daocart = dbhelper.getCartDao(con);
            res = daocart.getTotalcart().toString();

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

    public Boolean deletedchart(String CampaignId) throws Exception{
        Boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Cart_Dao daocart = dbhelper.getCartDao(con);

            res = daocart.deletedchart(CampaignId);

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

    public Boolean deletechart() throws Exception{
        Boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Cart_Dao daocart = dbhelper.getCartDao(con);

            res = daocart.deletechart();

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
