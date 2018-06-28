package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;


import java.util.List;

public class Payment_SucceActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;

    int prodid;

    String cupid, exp;
    String pay_fail, pay_succe, tabbar, topup;
    String Msg, RespNo, RefNo, Respcode, msg_text, remark;

    int trans_id, transfer_id;
    String userId;

    ProgressDialog progressDialog;

    List<CartEntity> cartList = null;

    private Cart_Module cartModule;
    private Customer_Module userModule;

    Double total;


    TextView message;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            cartModule = new Cart_Module(context);
            userModule = new Customer_Module(context);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userId = settings.getString("staff_id", null);
            Msg = settings.getString("Msg", "");
            RespNo = settings.getString("RespNo", "");
            RefNo = settings.getString("RefNo", "");
            Respcode = settings.getString("Respcode", "");
            trans_id = settings.getInt("trans_id", 0);

            Bundle extras = getIntent().getExtras();
            pay_succe = extras.getString("pay_succe");
            pay_fail = extras.getString("pay_fail");
            msg_text = extras.getString("msg_text", null);
            transfer_id = extras.getInt("transfer_id", 0);
            tabbar = extras.getString("tabview");
            total = extras.getDouble("total");
            remark = extras.getString("remark");
            topup = extras.getString("topup");

            progressDialog = new ProgressDialog(Payment_SucceActivity.this);

            if (pay_succe != null && pay_succe.equals("pay_succe")) {
                setContentView(R.layout.thank_payment);
            }

            if (pay_fail != null && pay_fail.equals("pay_fail")) {
                setContentView(R.layout.payment_fail);
            }

            message = (TextView) findViewById(R.id.textView1);

            if (topup != null && topup.equals("topup")) {
                if (pay_succe != null && pay_succe.equals("pay_succe")) {
                    message.setText(getString(R.string.topup_success_alert));
                }

                if (pay_fail != null && pay_fail.equals("pay_fail")) {
                    message.setText(getString(R.string.topup_failure_alert));
                }
            }

            cartModule.deletechart();


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
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("flag", "wallet");
        startActivity(intent);
        overridePendingTransition(0, 0);
        Payment_SucceActivity.this.finish();
        super.onBackPressed();

    }

}
