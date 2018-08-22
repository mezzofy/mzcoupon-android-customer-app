package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mezzofy.com.libmzcoupon.utills.APIServerException;

/**
 * Created by udhayinforios on 4/11/16.
 */
public class EditorActivity extends Activity {

    ImageView savebtn;
    EditText name, mobileno;//, email;
    final Context context = this;

    private Customer_Module userModule;
    CustomerEntity customerRes,staffRes;
    RelativeLayout layout;
    CommonUtils common;
    String regId;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.editpwd);

            userModule = new Customer_Module(EditorActivity.this);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            name = (EditText) findViewById(R.id.editname);
            mobileno = (EditText) findViewById(R.id.editmobile);
//            email = (EditText) findViewById(R.id.editText5);
            savebtn = (ImageView) findViewById(R.id.save);

            staffRes = userModule.getUser();

            name.setText(staffRes.getCustomerFirstName());
            mobileno.setText(staffRes.getCustomerMobile());
           // email.setText(staffRes.getCustomer_email());

            savebtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (DetectConnection.checkInternetConnection(getApplicationContext())) {
                        if (!name.getText().toString().equals("") || !mobileno.getText().toString().equals("")) {

                            customerRes = new CustomerEntity();

                            //Name checking
                            if (!name.getText().toString().equals("")){
                                customerRes.setCustomerFirstName(name.getText().toString());
                            }

//                            //EMail checking
//                            if (emailValidator(email.getText().toString().trim())) {
//                                customerRes.setCustomer_email(email.getText().toString().trim());
//                            } else {
//                                CommonUtils.Snackbar(v, getString(R.string.Please_check_email));
//                            }

                            //Mobile no checking
                            if (mobileno.getText().toString().length() > 0) {
                                customerRes.setCustomerMobile(mobileno.getText().toString());
                            } else {
                                CommonUtils.Snackbar(v, getString(R.string.please_check_mobile));
                            }

                            customerRes.setCustomerId(staffRes.getCustomerId());

                            CustomermEntity custServer = new CustomermEntity()   ;
                            custServer.setCustomer(customerRes);
                            CustomermEntity server = null;
                            try {
                                server = userModule.customer_update(custServer);
                            } catch (APIServerException e) {
                                CommonUtils.Snackbar(v, e.getMessage());
                                return;
                            }

                            if (server != null && server.getCustomer() != null) {
                                try {
                                    userModule.updateUser(server.getCustomer().getCustomerId(),
                                            server.getCustomer().getCustomerFirstName(),
                                            server.getCustomer().getCustomerMobile());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                EditorActivity.this.finish();

                            } else {
                                CommonUtils.Snackbar(v,Applaunch.ErrorMessage);
                            }

                        } else {
                            CommonUtils.Snackbar(v, getString(R.string.do_not_leave_emty));
                        }
                    } else {
                        CommonUtils.Snackbar(v, getString(R.string.No_Internet));
                    }
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }
}