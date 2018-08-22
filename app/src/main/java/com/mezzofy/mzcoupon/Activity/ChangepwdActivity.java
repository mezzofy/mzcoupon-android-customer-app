package com.mezzofy.mzcoupon.Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import mezzofy.com.libmzcoupon.utills.APIServerException;

public class ChangepwdActivity extends Activity {

    ImageView savebtn;
    EditText pass, newpwd, cpwd;
    final Context context = this;
    private Customer_Module customerModule;
    CustomerEntity customerData,CustomerRes;
    RelativeLayout layout;
    CommonUtils common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.user_changepwd);

            customerModule = new Customer_Module(ChangepwdActivity.this);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            pass = (EditText) findViewById(R.id.editText3);
            newpwd = (EditText) findViewById(R.id.editText4);
            cpwd = (EditText) findViewById(R.id.editText5);
            savebtn = (ImageView) findViewById(R.id.save);

            customerData = customerModule.getUser();

            savebtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (DetectConnection.checkInternetConnection(getApplicationContext())) {
                        if (!pass.getText().toString().equals("") && !newpwd.getText().toString().equals("") && !cpwd.getText().toString().equals("")) {
                            if (newpwd.getText().toString().equals(cpwd.getText().toString())) {
                                if (!newpwd.getText().toString().equals(customerData.getCustomerPassword())) {
                                    CustomermEntity custServer = new CustomermEntity();
                                    CustomerEntity customerData1=new CustomerEntity();
                                    customerData1.setCustomerId(customerData.getCustomerId());
                                    customerData1.setCustomerPassword(newpwd.getText().toString());
                                    custServer.setCustomer(customerData1);

                                    try {
                                        custServer  = customerModule.Changepassward(custServer);
                                    } catch (APIServerException e) {
                                        common.Snackbar(layout, e.getMessage());
                                        return;
                                    }
                                    Log.d("custser---",custServer.getCustomer().toString());

                                    if (custServer != null && custServer.getCustomer() != null && custServer.getCustomer().getCustomerId()!=null) {

                                        try {
                                            customerModule.ChangePassword(custServer);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        common.Snackbar(layout,getString(R.string.Password_Changed));

                                        Intent intent = new Intent(context, TabViewActivtiy.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("tabName", "");
                                        intent.putExtra("currTab", 4);
                                        startActivity(intent);
                                        overridePendingTransition(0, 0);
                                        ChangepwdActivity.this.finish();
                                    } else {
                                        common.Snackbar(layout, getString(R.string.Failure));
                                    }

                                } else {
                                    common.Snackbar(layout, getString(R.string.User_Password_Wrong));
                                }

                            } else {
                                common.Snackbar(layout, getString(R.string.Password_And_Confirm_not_equal));
                            }
                        } else {
                            common.Snackbar(layout, getString(R.string.pl_enterfeilds));
                        }
                    } else {
                        common.Snackbar(layout, getString(R.string.No_Internet));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validate(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
//	        Intent intent = new Intent(context, TabMainActivity.class);
//	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        intent.putExtra("tabName", "");
//	        intent.putExtra("currTab", 4);
//
//		    startActivity(intent);
//		    overridePendingTransition(0, 0);
        ChangepwdActivity.this.finish();
    }
}
