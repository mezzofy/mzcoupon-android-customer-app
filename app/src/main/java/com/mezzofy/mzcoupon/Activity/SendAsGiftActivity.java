package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.mezzofy.mzcoupon.Entity.GiftcouponEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.Entity.GiftcouponmEntity;
import com.mezzofy.mzcoupon.module.Coupon_Module;

import mezzofy.com.libmzcoupon.utills.APIServerException;


/**
 * Created by udhayinforios on 19/10/15.
 */
public class SendAsGiftActivity extends Activity {

    EditText email, gift_msg;

    String cupid;

    String staffId;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.send_as_gift);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            Bundle extras = getIntent().getExtras();
            cupid = extras.getString("couponId");

            email = (EditText) findViewById(R.id.email);
            gift_msg = (EditText) findViewById(R.id.name);

            Button send = (Button) findViewById(R.id.signinbutton);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils common = new CommonUtils();

                    if (email.getText().toString().length() == 0 || email.getText().toString().equals("")) {
                        common.Snackbar(v, getString(R.string.enter_email_alert));
                    } else if (gift_msg.getText().toString().length() == 0 || gift_msg.getText().toString().equals("")) {
                        common.Snackbar(v, getString(R.string.enter_gift_msg_alert));
                    } else {
                        if (staffId != null) {

                            Coupon_Module couponModule=new Coupon_Module(getApplicationContext());

                            GiftcouponmEntity giftcouponModel = new GiftcouponmEntity();
                            GiftcouponEntity giftcouponData=new GiftcouponEntity();
                            giftcouponData.setCouponId(cupid);
                            giftcouponData.setCustomerId(staffId);
                            giftcouponData.setEmail(email.getText().toString());
                            giftcouponData.setNotes(gift_msg.getText().toString());

                            giftcouponModel.setSharecoupon(giftcouponData);

                            GiftcouponmEntity getres = null;
                            try {
                                getres = couponModule.PostGiftCouponToServerAPI(giftcouponModel);
                            } catch (APIServerException e) {
                                common.Snackbar(v,e.getMessage());
                            }

                            if (getres != null && getres.getSharecoupon()!=null && getres.getSharecoupon().getCustomerId() != null) {

                                Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("flag", "coupon");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                SendAsGiftActivity.this.finish();

                            } else {
                                common.Snackbar(v,getString(R.string.sendasgift_faliure_alert));
                            }

                        } else {
                            common.Snackbar(v,getString(R.string.invalid_mail));
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
