package com.mezzofy.mzcoupon.apputills;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.CustomerDeviceEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CustomerDevicemEntity;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;
import com.mezzofy.mzcoupon.Activity.ProgressActivity;
import com.mezzofy.mzcoupon.Activity.SignInActivity;


import java.io.IOException;

public class GCM_Register extends FragmentActivity {

    final Context context = this;
    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    String regId;
    static final String TAG = "GCM Register";
    String useremail, password;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private String flag,countrycode;
    private AccessToken accessToken;
    private CustomerDevicemEntity customerDeviceModel=new CustomerDevicemEntity();
    private Customer_Module userModule;
    private Setting_Module settingModule;

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);
        userModule=new Customer_Module(GCM_Register.this);
        settingModule=new Setting_Module(GCM_Register.this);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();


            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
//			Toast.makeText(getApplicationContext(),
//					"RegId already available. RegId: " + regId,
//					Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    public String registerGCM(String useremail, String password,String flag,AccessToken accessToken,String countryCode)  {

        try {
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(context);

            this.useremail = useremail;
            this.password = password;
            this.flag = flag;
            this.accessToken =accessToken;
            this.countrycode=countryCode;

            if (TextUtils.isEmpty(regId)) {

                registerInBackground();

                Log.d("RegisterActivity", "registerGCM - successfully registered with GCM server - regId: " + regId);
            } else {
//			Toast.makeText(getApplicationContext(),"RegId already available. RegId: " + regId,Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return regId;
    }

    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(SignInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: " + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);

                    if (regId.equals("")) {
                        Thread threadw = new Thread();
                        try {
                            threadw.sleep(2000);
                            doInBackground();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Thread thread = new Thread();
                    try {
                        thread.sleep(2000);
                        doInBackground();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//				Toast.makeText(getApplicationContext(),"Registered with GCM Server." + msg, Toast.LENGTH_LONG).show();

                if (!regId.equals("")) {

                    userModule=new Customer_Module(GCM_Register.this);
                    settingModule=new Setting_Module(GCM_Register.this);

                    try {

                        if (ContextCompat.checkSelfPermission(GCM_Register.this, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(GCM_Register.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        } else {

                            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String uuid = tManager.getDeviceId();
                           // userModule.deletelogout(uuid);
                            CustomermEntity staffRes = null;

                            CustomerEntity customerData1=new CustomerEntity();
                            customerData1.setCustomerEmail(useremail);
                            customerData1.setCustomerPassword(password);
                            customerData1.setCountryCode(countrycode);
                            customerData1.setUserType("C");

                            CustomerDeviceEntity customerDeviceData=new CustomerDeviceEntity();
                            customerDeviceData.setDeviceToken(regId);
                            customerDeviceData.setDeviceName("A");
                            customerDeviceData.setDeviceUuid(uuid);


                            customerDeviceModel.setCustomer(customerData1);
                            customerDeviceModel.setDevice(customerDeviceData);


                            if(flag.equals("F")){
                                staffRes = userModule.getfblogin(customerDeviceModel);
                            }else {

                                staffRes=userModule.getlogin(customerDeviceModel);
                            }

                            if (staffRes != null && staffRes.getCustomer().getCustomerId() != null) {
                                if (staffRes.getCustomer() != null) {

                                    CustomerEntity customerData=staffRes.getCustomer();

                                    userModule.addStaff(customerData);

                                    if(flag.equals("F"))
                                        settingModule.CheckSetting(staffRes.getCustomer().getCustomerFirstName(),staffRes.getCustomer().getCustomerPassword(),"F");
                                    else
                                        settingModule.CheckSetting(staffRes.getCustomer().getCustomerFirstName(),staffRes.getCustomer().getCustomerPassword(),"C");


                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("staff_id", staffRes.getCustomer().getCustomerId());
                                    editor.putString("user_name", staffRes.getCustomer().getCustomerUsername());
                                    editor.putString("company_Id", staffRes.getCustomer().getMerchantId());
                                    editor.putString("mobile", staffRes.getCustomer().getCustomerMobile());
                                    editor.putString("profill", staffRes.getCustomer().getCustomerStatus());
                                    editor.putString("agent", staffRes.getCustomer().getUserType());
                                    editor.putString("tokendevice", regId);
                                    editor.commit();

                                    Intent intent = new Intent(context, ProgressActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.Try_Again, Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Try_Again, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(null, null, null);
    }

    public void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(SignInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
