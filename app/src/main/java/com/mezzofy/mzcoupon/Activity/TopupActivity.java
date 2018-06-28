package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.apputills.CommonUtils;

/**
 * Created by udhayinforios on 7/10/15.
 */
public class TopupActivity extends Activity {

    TextView value;
    TextView value1;
    TextView value2;
    TextView value3;

    EditText amount;
    EditText remark;

    Button topup;

    TextView text, text1, text2, text3;

    ProgressDialog progressDialog;
    String Amount;

    SharedPreferences settings;



    String staffid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        try {


            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffid = settings.getString("staff_id", null);

            text = (TextView) findViewById(R.id.textvalue);
            text1 = (TextView) findViewById(R.id.textvalue1);
            text2 = (TextView) findViewById(R.id.textvalue2);
            text3 = (TextView) findViewById(R.id.textvalue3);

            amount = (EditText) findViewById(R.id.amount);
            remark = (EditText) findViewById(R.id.remark);

//            CompanyRes res = compdao.getCompanyname();
//            if (res.getCurrency() != null) {
//                text.setText(res.getCurrency() + "100");
//                text1.setText(res.getCurrency() + "200");
//                text2.setText(res.getCurrency() + "500");
//                text3.setText(res.getCurrency() + "1000");
//            }

            value = (TextView) findViewById(R.id.textvalue);
            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value.setBackgroundColor(getResources().getColor(R.color.white));
                    value1.setBackgroundColor(getResources().getColor(R.color.black));
                    value2.setBackgroundColor(getResources().getColor(R.color.black));
                    value3.setBackgroundColor(getResources().getColor(R.color.black));
                    value.setTextColor(getResources().getColor(R.color.black));
                    value1.setTextColor(getResources().getColor(R.color.white));
                    value2.setTextColor(getResources().getColor(R.color.white));
                    value3.setTextColor(getResources().getColor(R.color.white));

                    Amount = String.valueOf(100);
                    amount.setText((Amount));
                    amount.setSelection(amount.getText().length());

                }
            });

            value1 = (TextView) findViewById(R.id.textvalue1);
            value1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value1.setBackgroundColor(getResources().getColor(R.color.white));
                    value.setBackgroundColor(getResources().getColor(R.color.black));
                    value2.setBackgroundColor(getResources().getColor(R.color.black));
                    value3.setBackgroundColor(getResources().getColor(R.color.black));
                    value.setTextColor(getResources().getColor(R.color.white));
                    value1.setTextColor(getResources().getColor(R.color.black));
                    value2.setTextColor(getResources().getColor(R.color.white));
                    value3.setTextColor(getResources().getColor(R.color.white));

                    Amount = String.valueOf(200);
                    amount.setText((Amount));
                    amount.setSelection(amount.getText().length());

                }
            });

            value2 = (TextView) findViewById(R.id.textvalue2);
            value2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value2.setBackgroundColor(getResources().getColor(R.color.white));
                    value.setBackgroundColor(getResources().getColor(R.color.black));
                    value1.setBackgroundColor(getResources().getColor(R.color.black));
                    value3.setBackgroundColor(getResources().getColor(R.color.black));
                    value.setTextColor(getResources().getColor(R.color.white));
                    value2.setTextColor(getResources().getColor(R.color.black));
                    value1.setTextColor(getResources().getColor(R.color.white));
                    value3.setTextColor(getResources().getColor(R.color.white));

                    Amount = String.valueOf(500);
                    amount.setText(Amount);
                    amount.setSelection(amount.getText().length());
                }
            });


            value3 = (TextView) findViewById(R.id.textvalue3);
            value3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value3.setBackgroundColor(getResources().getColor(R.color.white));
                    value.setBackgroundColor(getResources().getColor(R.color.black));
                    value1.setBackgroundColor(getResources().getColor(R.color.black));
                    value2.setBackgroundColor(getResources().getColor(R.color.black));
                    value.setTextColor(getResources().getColor(R.color.white));
                    value3.setTextColor(getResources().getColor(R.color.black));
                    value1.setTextColor(getResources().getColor(R.color.white));
                    value2.setTextColor(getResources().getColor(R.color.white));

                    Amount = "1000";
                    amount.setText(Amount);
                    amount.setSelection(amount.getText().length());
                }
            });

            topup = (Button) findViewById(R.id.top_up);
            topup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (amount.getText().toString().equals(null) || amount.getText().toString().equals("") || amount.getText().toString().length() == 0) {
                            CommonUtils.Snackbar(v, getString(R.string.amount_alert));
                        } else {
                            if (staffid != null) {
                                Intent intent = new Intent(getApplicationContext(), MyScanActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("total", Double.valueOf(amount.getText().toString()));
                                intent.putExtra("message", remark.getText().toString());
                                intent.putExtra("topupflag","topuppaypal");
                                startActivity(intent);
                                finish();
                            } else {
                                CommonUtils.Snackbar(v, getString(R.string.topup_failure_alert));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("flag", "wallet");
        intent.putExtra("currTab", 5);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }

}
