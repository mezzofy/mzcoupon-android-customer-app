package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

public class Redeem_StatusActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;
    String couponId, stk, store, exp, expday;
    String campaignId;
    SharedPreferences settings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {

            Bundle extras = getIntent().getExtras();
            expday = extras.getString("expday");
            stk = extras.getString("stk");
            store = extras.getString("store");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);

            if (stk != null)
                if (stk.equals("successful")) {
                    setContentView(R.layout.redeem_successful);
                    TextView redstatu = (TextView) findViewById(R.id.textView1);
                    redstatu.setVisibility(View.VISIBLE);
                    redstatu.setText(R.string.redemption);

                } else if (stk.equals("successredeem")) {
                    setContentView(R.layout.redeem_successful);
                    TextView redstatu = (TextView) findViewById(R.id.textView1);
                    redstatu.setVisibility(View.VISIBLE);
                    redstatu.setText(R.string.redemption);
                } else if (stk.equals("already")) {
                    setContentView(R.layout.redeem_successful);
                    TextView redstatu = (TextView) findViewById(R.id.textView1);
                    redstatu.setVisibility(View.VISIBLE);
                    redstatu.setText(R.string.Coupon_Redeemed_already);
                } else {
                    setContentView(R.layout.redeem_failed);
                    TextView redstatu = (TextView) findViewById(R.id.textView1);
                    redstatu.setVisibility(View.VISIBLE);
                    redstatu.setText(R.string.Redeemed_Failed_qr);
                }

            if (store != null)
                if (store.equals("successful")) {
                    setContentView(R.layout.redeem_successful);
                    TextView redstatu = (TextView) findViewById(R.id.textView1);
                    redstatu.setVisibility(View.VISIBLE);
                }

            TextView cancelredeem = (TextView) findViewById(R.id.TextViewCal);
            cancelredeem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();

        Intent intent = new Intent(context, TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("flag", "coupon");
        startActivity(intent);
        overridePendingTransition(0, 0);
        Redeem_StatusActivity.this.finish();
    }

}
