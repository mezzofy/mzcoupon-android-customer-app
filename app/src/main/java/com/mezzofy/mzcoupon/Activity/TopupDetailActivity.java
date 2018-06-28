package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Customer_Module;


import java.text.DecimalFormat;

/**
 * Created by udhayinforios on 7/10/15.
 */
public class TopupDetailActivity extends Activity {

    TextView customer_name, value, ref_no;

    String cus_id;
    String siteid;
    String user_id;

    String amount;
    String remark;
    String refno, cus_name, flag;

    Customer_Module customerModule;

    AlertDialog alert1;
    private ProgressBar progress;
    Double total;

    Button conform;
    CustomerEntity userres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_topup_detail);

            customerModule = new Customer_Module(getApplicationContext());
        userres = customerModule.getUser();

        progress = (ProgressBar) findViewById(R.id.progress1);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = settings.getString("staff_id", null);

        customer_name = (TextView) findViewById(R.id.customer_name);
        value = (TextView) findViewById(R.id.value);
        ref_no = (TextView) findViewById(R.id.ref_no);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            amount = data.getString("value");
            cus_id = (data.getString("cus_id"));
            refno = (data.getString("ref_no"));
            cus_name = (data.getString("cus_name"));
            remark = (data.getString("remark"));
            total = (data.getDouble("total"));
            flag = (data.getString("flag"));

            DecimalFormat df = new DecimalFormat("##0.00");

            value.setText(String.valueOf(total));
            customer_name.setText(userres.getCustomerFirstName());
            ref_no.setText(remark);
        }

        conform = (Button) findViewById(R.id.top_up);
        conform.setText(getResources().getText(R.string.con_topup));
        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TopupPaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("total", total);
                intent.putExtra("remark", remark);
                startActivity(intent);
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

