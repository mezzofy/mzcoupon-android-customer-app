package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.CustomerDeviceEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.GCM_Register;
import com.mezzofy.mzcoupon.Entity.CustomerDevicemEntity;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;

/**
 * Created by udhayinforios on 24/6/16.
 */
public class SigninImpl {

    private static Customer_Module userModule;
    private static Setting_Module settingModule;

    public static void signin(View view, Context context, String useremail, String password, Activity activity){


        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;


        if (DetectConnection.checkInternetConnection(context)) {

            if (useremail.length() > 0) {
                if (password.length() > 0) {
                    String regId = null;
                    GCM_Register objgcm = new GCM_Register();

                    if (TextUtils.isEmpty(regId)) {
                        regId = objgcm.registerGCM(useremail, password,"S",null,"");
                    }

                    if (regId != null && !regId.equals("")) {

                        objgcm.storeRegistrationId(context, regId);

                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        } else {
                            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                            String uuid = tManager.getDeviceId();

                            CustomerDevicemEntity customerDeviceModel=new CustomerDevicemEntity();

                            CustomerEntity customerData=new CustomerEntity();
                            customerData.setCustomerEmail(useremail);
                            customerData.setCustomerPassword(password);

                            CustomerDeviceEntity customerDeviceData=new CustomerDeviceEntity();
                            customerDeviceData.setDeviceToken(regId);
                            customerDeviceData.setDeviceName("A");
                            customerDeviceData.setDeviceUuid(uuid);

                            customerDeviceModel.setCustomer(customerData);
                            customerDeviceModel.setDevice(customerDeviceData);

                            Customer_Module customer_module=new Customer_Module(context);

                            CustomermEntity staffRes= null;
                            try {
                                staffRes = customer_module.getlogin(customerDeviceModel);
                            } catch (APIServerException e) {
                                CommonUtils.Snackbar(view,e.getMessage());
                            }

                            userModule=new Customer_Module(context);
                            settingModule=new Setting_Module(context);
                            if (staffRes != null) {
                                if (staffRes.getCustomer().getCustomerId() != null) {

                                    try {
                                        userModule.addStaff(staffRes.getCustomer());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        settingModule.CheckSetting(staffRes.getCustomer().getCustomerEmail(),staffRes.getCustomer().getCustomerPassword(),"C");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("staff_id", staffRes.getCustomer().getCustomerId());
                                    editor.putString("user_name", staffRes.getCustomer().getCustomerFirstName());
                                    editor.putString("company_Id", staffRes.getCustomer().getMerchantId());
                                    editor.putString("mobile", staffRes.getCustomer().getCustomerMobile());
                                    editor.putString("profill", staffRes.getCustomer().getCustomerStatus());
                                    editor.putString("agent", staffRes.getCustomer().getUserType());
                                    editor.putString("tokendevice", regId);
                                    editor.apply();

                                    Intent intent = new Intent(context, ProgressActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);


                                } else {
                                    CommonUtils.Snackbar(view, context.getString(R.string.Try_Again));
                                }
                            } else {
                                CommonUtils.Snackbar(view, context.getString(R.string.Try_Again));
                            }
                        }
                    }
                } else {
                    CommonUtils.Snackbar(view, context.getString(R.string.do_not_leave_emty));
                }
            } else {
                CommonUtils.Snackbar(view, context.getString(R.string.do_not_leave_emty));
            }
        } else {
            CommonUtils.Snackbar(view, context.getString(R.string.No_Internet));
        }

    }
}
