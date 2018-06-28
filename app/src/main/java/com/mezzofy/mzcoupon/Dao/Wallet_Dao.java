package com.mezzofy.mzcoupon.Dao;

import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by aruna on 9/29/17.
 */

public class Wallet_Dao {
    private Connection connection;

    public Wallet_Dao(Connection con) {
        connection = con;
    }

    public Boolean addWallet(WalletEntity walletData)throws SQLException {
        Boolean res = false;

        Deletewalletdetail(walletData.getCustomerId());

        String  sql = "insert into tbl_wallet_balance (walletId,customerId,walletCredit,qrCode,qr_time,rewardPoint,updatedOn)values(?,?,?,?,?,?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{walletData.getWalletId(), walletData.getCustomerId(), walletData.getWalletCredit(),
                    walletData.getQrCode(), walletData.getQrTime(), walletData.getRewardPoint(), walletData.getUpdatedOn()});

        return res;
    }

    public Boolean Deletewalletdetail(String CustomerId)throws SQLException{
        Boolean res=false;
        String  sql = "delete from tbl_wallet_balance where customerId=?";
        DatabaseUtils.update(connection, sql,new Object[]{CustomerId});
        return res;
    }

    public WalletEntity getwalletdetail(String CustomerId) throws SQLException{

        WalletEntity walletData = new WalletEntity();
        String sql = "select * from tbl_wallet_balance where customerId=?";
        ResultSetMapper<WalletEntity> resultSetMapper = new ResultSetMapper<WalletEntity>();
        walletData=DatabaseUtils.queryForObj(connection,sql,resultSetMapper, WalletEntity.class,new Object[]{CustomerId});
        return walletData;

    }


    public Boolean updateQr(WalletEntity walletData)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_wallet_balance set qrCode=?,qr_time=? where customerId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{walletData.getQrCode(),walletData.getQrTime(),walletData.getCustomerId()});
        return res;
    }
}
