package com.mezzofy.mzcoupon.Dao;

import android.util.Log;

import com.mezzofy.mzcoupon.Entity.WalletTxnEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aruna on 11/8/17.
 */

public class WalletTransaction_Dao {
    private Connection connection;

    public WalletTransaction_Dao(Connection con) {
        connection = con;
    }

    public Boolean addWalletTransaction(WalletTxnEntity walletTxnData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<WalletTxnEntity> resultSetMapper=new ResultSetMapper<WalletTxnEntity>();

        String sql = "SELECT * FROM tbl_wallet_transcation  where transactionReference=?";
        WalletTxnEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,WalletTxnEntity.class, new Object[]{walletTxnData.getTransactionReference()});

        if(res1!=null)
        {
            if(walletTxnData.getHashCode()==res1.getHashCode()) {
                sql = "update tbl_wallet_transcation SET deleteflag=? where transactionReference=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{"N",walletTxnData.getTransactionReference()});
                return true;
            }
            else
            {

                sql = "update tbl_wallet_transcation SET userId=?,merchantId=?,siteId=?,customerId=?,walletId=?,transactionType=?,transactionAmount=?,transactionPayRef=?,paidOn=?," +
                        "processBy=?,status=?,transactionNotes=?,hashCode=?,transactionDate=?,paymentResponse=?," +
                        "rewardPoint=?,updatedOn=?,approvalCode=?,deleteflag=? where transactionReference=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{walletTxnData.getUserId(), walletTxnData.getMerchantId(), walletTxnData.getSiteId(), walletTxnData.getCustomerId(),
                        walletTxnData.getWalletId(),walletTxnData.getTransactionType(), walletTxnData.getTransactionAmount(),walletTxnData.getTransactionPayRef(),walletTxnData.getPaidOn(),
                        walletTxnData.getProcessBy(),walletTxnData.getStatus(),walletTxnData.getTransactionNotes(),walletTxnData.getHashCode(),walletTxnData.getTransactionDate(),walletTxnData.getPaymentResponse(),
                        walletTxnData.getRewardPoint(), walletTxnData.getUpdatedOn(), walletTxnData.getApprovalCode(),"N",walletTxnData.getTransactionReference()});
            }
        }
        else {

            sql = "insert into tbl_wallet_transcation (userId,merchantId,siteId,customerId,walletId,transactionType,transactionAmount,transactionPayRef,paidOn" +
                    "processBy,status,transactionNotes,hashCode,transactionDate,paymentResponse,rewardPoint,updatedOn,approvalCode,deleteflag) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{walletTxnData.getUserId(), walletTxnData.getMerchantId(), walletTxnData.getSiteId(), walletTxnData.getCustomerId(),
                    walletTxnData.getWalletId(),walletTxnData.getTransactionType(), walletTxnData.getTransactionAmount(),walletTxnData.getTransactionPayRef(),walletTxnData.getPaidOn(),
                    walletTxnData.getProcessBy(),walletTxnData.getStatus(),walletTxnData.getTransactionNotes(),walletTxnData.getHashCode(),walletTxnData.getTransactionDate(),
                    walletTxnData.getPaymentResponse(), walletTxnData.getRewardPoint(), walletTxnData.getUpdatedOn(), walletTxnData.getApprovalCode()});
        }

        return res;
    }


    public List<WalletTxnEntity> getPandingTransList(int Limit, int offset) throws SQLException{

        List<WalletTxnEntity> walletTxnDatas=new ArrayList<WalletTxnEntity>();
        String Sql="SELECT * FROM tbl_wallet_transcation WHERE  deleteflag='N' ORDER BY transactionDate  ASC LIMIT ?,?";
        Log.d("limite",String.valueOf(Limit));
        Log.d("offset",String.valueOf(offset));

        ResultSetMapper<WalletTxnEntity>resultSetMapper=new ResultSetMapper<WalletTxnEntity>();
        walletTxnDatas=DatabaseUtils.queryForList(connection,Sql,resultSetMapper, WalletTxnEntity.class,new Object[]{Limit,offset});

        return walletTxnDatas;
    }


    public Boolean Deletewallettrans(String transactionReference)throws SQLException {
        Boolean res=false;

        String sql = "delete from tbl_wallet_transcation where transactionReference=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{transactionReference});
        return res;
    }

}
