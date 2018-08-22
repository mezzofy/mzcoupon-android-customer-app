package com.mezzofy.mzcoupon.module;

import android.content.Context;


import com.mezzofy.mzcoupon.Entity.WalletmEntity;

import org.json.JSONException;


import java.io.IOException;

import mezzofy.com.libmzcoupon.data.WalletDataModel;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZTransaction;

/**
 * Created by aruna on 10/20/17.
 */

public class Transcation_Module {

    private MZTransaction transactionModule;

    public Transcation_Module(Context context) {
        transactionModule=new MZTransaction(context);
    }

    public WalletmEntity getTransactionlistfromserver(String customerId) {
        WalletmEntity walletmEntity = null;

        WalletDataModel walletmData= transactionModule.GetCustomerTransactions(customerId);
        if(walletmData!=null)
            try {
                walletmEntity=(WalletmEntity) JsonMapper.mapJsonToObj(walletmData, WalletmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return walletmEntity;
    }
}
