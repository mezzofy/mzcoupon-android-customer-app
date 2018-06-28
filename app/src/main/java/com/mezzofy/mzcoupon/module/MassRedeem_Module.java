package com.mezzofy.mzcoupon.module;

import android.content.Context;


import com.google.gson.Gson;

import com.mezzofy.MzCouponAPI.data.MassCouponDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZMassRedeem;
import com.mezzofy.MzCouponAPI.utills.APIServerException;

import com.mezzofy.mzcoupon.Entity.MassCouponmEntity;


import org.json.JSONException;

import java.io.IOException;


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
