package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.mezzofy.MzCouponAPI.data.WalletDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZWallet;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Dao.Wallet_Dao;

import com.mezzofy.mzcoupon.Entity.WalletmEntity;

import org.json.JSONException;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Created by aruna on 9/29/17.
 */

public class Wallet_Module {

    private CouponDB dbhelper;
    SharedPreferences settings;
    private MZWallet walletModule;

    public Wallet_Module(Context context) {
        dbhelper = new CouponDB(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        walletModule=new MZWallet(context);
    }


    public WalletmEntity getWalletlistfromserver(String customerId) {
        WalletmEntity walletmEntity = null;
        WalletDataModel walletmData= walletModule.GetCustomerWallet(customerId);
        if(walletmData!=null)
        {
            try {
                walletmEntity=(WalletmEntity) JsonMapper.mapJsonToObj(walletmData, WalletmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return walletmEntity;
    }

    public WalletmEntity getwalletQRfromserver(String customerId) {
        WalletmEntity walletmEntity = null;

        WalletDataModel walletmData= walletModule.GetCustomerWalletQR(customerId);
        if(walletmData!=null)
        {
            try {
                walletmEntity=(WalletmEntity) JsonMapper.mapJsonToObj(walletmData, WalletmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return walletmEntity;
    }


    public Boolean addWallet(WalletEntity walletData)throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Wallet_Dao walletDao = dbhelper.getWalletDao(con);

            res = walletDao.addWallet(walletData);
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

    public Boolean updateQr(WalletEntity walletData)throws Exception{
        boolean res = false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Wallet_Dao walletDao = dbhelper.getWalletDao(con);

            res = walletDao.updateQr(walletData);
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

    public WalletEntity getwalletdetail(String CustomerId)throws Exception {

        WalletEntity walletData = new WalletEntity();
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Wallet_Dao walletDao = dbhelper.getWalletDao(con);
            walletData = walletDao.getwalletdetail(CustomerId);
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
        return walletData;
    }

}
