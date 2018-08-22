package com.mezzofy.mzcoupon.module;

import android.content.Context;


import com.google.gson.Gson;



import com.mezzofy.mzcoupon.Entity.MassCouponmEntity;


import org.json.JSONException;

import java.io.IOException;

import mezzofy.com.libmzcoupon.data.MassCouponDataModel;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZMassRedeem;
import mezzofy.com.libmzcoupon.utills.APIServerException;


/**
 * Created by aruna on 10/24/17.
 */

public class MassRedeem_Module {
    Gson gson = new Gson();
    private MZMassRedeem massRedeemModule;


    public MassRedeem_Module(Context context) {
        massRedeemModule=new MZMassRedeem(context);
    }

    public MassCouponmEntity postMassRedeem(MassCouponmEntity massCouponmEntity) throws APIServerException {

        MassCouponmEntity resp=null;

        try {
            MassCouponDataModel massCouponmData=(MassCouponDataModel) JsonMapper.mapJsonToObj(massCouponmEntity, MassCouponDataModel.class);

                MassCouponDataModel ret=massRedeemModule.MassRedeemCreate(massCouponmData);
                if(ret!=null)
                    resp=(MassCouponmEntity) JsonMapper.mapJsonToObj(ret, MassCouponmEntity.class);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return resp;
    }

}
