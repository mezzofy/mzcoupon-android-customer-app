package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.module.Cart_Module;

import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.util.List;

/**
 * Created by udhayinforios on 24/11/15.
 */
public class AgentCashActivtiy extends Activity implements View.OnClickListener {

    TextView email, skip;
    TextView msg;

    String flag, url, paykey, merhid;


    ProgressDialog progressDialog;

    List<CartEntity> cartList = null;

    private Cart_Module cartModule;

    String productionsandbox, productionkey, sandboxkey;

    Gson gson = new Gson();
    String email_text, msg_text, topup, remark;

    int transfer_id;

    RelativeLayout layout;
    CommonUtils common;

    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.agent_cash);

            cartModule = new Cart_Module(this);

            cartList = cartModule.getCartList();

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            email = (TextView) findViewById(R.id.email);
            msg = (TextView) findViewById(R.id.name);
            msg.setText("Remark");

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                flag = extras.getString("flag");
                productionsandbox = extras.getString("Productionsandbox");
                productionkey = extras.getString("Sandboxkey");
                sandboxkey = extras.getString("Productionkey");
                total = extras.getDouble("total");
                url = extras.getString("url");
                paykey = extras.getString("paykey");
                merhid = extras.getString("merhid");
                topup = extras.getString("topup");
                remark = extras.getString("remark");
            }

            Button transfer = (Button) findViewById(R.id.signinbutton);
            transfer.setOnClickListener(this);

            skip = (TextView) findViewById(R.id.skip);
            skip.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signinbutton:
                email_text = email.getText().toString();
                msg_text = msg.getText().toString();

                if (email.getText().toString().length() == 0 || email.getText().toString().equals("")) {
                    common.Snackbar(layout,getString(R.string.enter_email_alert));
                } else {
//                    CustomerRes res = modpayment.getcustomer(email_text);
//
//                    if (res != null && res.getCustomer_id() > 0) {
//                        transfer_id = res.getCustomer_id();
//
//                        Intent intent = new Intent(getApplicationContext(), MyScanActivity.class);
//                        intent.putExtra("email", res.getCustomer_email());
//                        intent.putExtra("transfer_id", res.getCustomer_id());
//                        intent.putExtra("staff_name", res.getCustomer_first_name());
//                        intent.putExtra("mobile", res.getCustomer_mobile());
//                        intent.putExtra("total", total);
//                        intent.putExtra("message", msg_text);
//                        intent.putExtra("total", cartDao.getTotalcart());
//                        intent.putExtra("topup", topup);
//                        startActivity(intent);
//                    }
                }
                break;

            case R.id.skip:
                Intent intent = new Intent(getApplicationContext(), MyScanActivity.class);
                intent.putExtra("message", msg_text);
                intent.putExtra("skip", "skip");
                try {
                    intent.putExtra("total", cartModule.getTotalcart());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("topup", topup);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}