package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.gson.Gson;


import com.mezzofy.mzcoupon.Entity.WalletTxnEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Dao.WalletTransaction_Dao;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.Entity.WalletTxnListmEntity;
import com.mezzofy.mzcoupon.Entity.WalletTxnmEntity;


import org.json.JSONException;


import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mezzofy.com.libmzcoupon.data.WalletTxnDataModel;
import mezzofy.com.libmzcoupon.data.WalletTxnListDataModel;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZWalletTransaction;
import mezzofy.com.libmzcoupon.utills.APIServerException;


/**
 * Created by aruna on 11/8/17.
 */

public class WalletTranscation_Module {
    private CouponDB dbhelper;
    SharedPreferences settings;
    Gson gson = new Gson();
    private MZWalletTransaction walletTransactionModule;

    public WalletTranscation_Module(Context context) {
        dbhelper = new CouponDB(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        walletTransactionModule = new MZWalletTransaction(context);
    }

    public WalletTxnListmEntity getwalletPendingTransc(String customerId, String status, int Pages) {
        WalletTxnListmEntity walletTxnListmEntity = null;
        SizemEnity size = null;

        WalletTxnListDataModel walletTxnListmData = walletTransactionModule.GetWalletTransactions(customerId, status, Pages);
        if (walletTxnListmData != null) {
            try {
                walletTxnListmEntity = (WalletTxnListmEntity) JsonMapper.mapJsonToObj(walletTxnListmData, WalletTxnListmEntity.class);
                if (walletTxnListmEntity != null) {
                    size = walletTxnListmEntity.getSize();
                    SharedPreferences.Editor editor = settings.edit();
                    try {
                        editor.putString("WalletTranssize", ObjectSerializer.serialize(size));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return walletTxnListmEntity;
    }


    public WalletTxnmEntity PostWallettxnToServer(WalletTxnmEntity walletTxnmEntity) throws APIServerException {
        WalletTxnmEntity resp = null;
        try {


            WalletTxnDataModel walletTxnmData = (WalletTxnDataModel) JsonMapper.mapJsonToObj(walletTxnmEntity, WalletTxnDataModel.class);
            WalletTxnDataModel retwalletTxnmData = walletTransactionModule.WalletTransactionCreate(walletTxnmData);
            if (retwalletTxnmData != null)
                resp = (WalletTxnmEntity) JsonMapper.mapJsonToObj(retwalletTxnmData, WalletTxnmEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return resp;
    }


    public Boolean addWalletTransaction(WalletTxnListmEntity walletTxnListModel) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            WalletTransaction_Dao walletDao = dbhelper.getWalletTransactionDao(con);

            for (WalletTxnmEntity walletTxnModel : walletTxnListModel.getWallettxns()) {
                res = walletDao.addWalletTransaction(walletTxnModel.getWallettxn());
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

    public List<WalletTxnEntity> getPandingTransList(int Limit, int offset) throws Exception {
        List<WalletTxnEntity> res = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            WalletTransaction_Dao walletDao = dbhelper.getWalletTransactionDao(con);

            res = walletDao.getPandingTransList(Limit, offset);

        } catch (SQLException e) {
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

    public Boolean Deletewallettrans(String transactionReference) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            WalletTransaction_Dao walletDao = dbhelper.getWalletTransactionDao(con);

            res = walletDao.Deletewallettrans(transactionReference);
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
