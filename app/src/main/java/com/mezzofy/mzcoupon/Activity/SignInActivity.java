package com.mezzofy.mzcoupon.Activity;

import com.estimote.sdk.SystemRequirementsChecker;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.CustomerDeviceEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.GCM_Register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.mezzofy.mzcoupon.Entity.CustomerDevicemEntity;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import org.json.JSONObject;


import java.util.Arrays;


@SuppressLint("NewApi")
public class SignInActivity extends GCM_Register implements OnClickListener{

    private final Context context = this;
    private EditText Uemail, Upwd;
    private Customer_Module userModule;
    private Setting_Module settingmodule;
    private String regId;

    private AlertDialog Dialog;

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_APP_SETTINGS = 168;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken accessToken;
    private LoginButton loginButton;

    private CustomermEntity customerModel=new CustomermEntity();
    private CustomerDevicemEntity customerDeviceModel=new CustomerDevicemEntity();
    private CustomerEntity customerData=new CustomerEntity();
    private CustomerDeviceEntity customerDeviceData=new CustomerDeviceEntity();


    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    private CustomerEntity userres;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            FacebookSdk.sdkInitialize(SignInActivity.this);
            FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
            setContentView(R.layout.signin);


            userModule = new Customer_Module(SignInActivity.this);
            settingmodule=new Setting_Module(SignInActivity.this);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            TextView register = (TextView) findViewById(R.id.new_register);
            register.setOnClickListener(this);

            RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.signin_page);

            layout.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent ev) {
                    hideKeyboard(view);
                    return true;
                }
            });


            this.findViewById(R.id.scrollView1).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    findPostion(event);
                    return true;
                }
            });

            Button signin = (Button) findViewById(R.id.signinbutton);
            signin.setOnClickListener(this);
            TextView fp = (TextView) findViewById(R.id.forgotpassword);
            fp.setOnClickListener(this);

            Uemail = (EditText) findViewById(R.id.email);
            Upwd = (EditText) findViewById(R.id.name);

            userres = userModule.getUser();

            if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SignInActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String uuid = tManager.getDeviceId();
                //userModule.deletelogout(uuid);

            }

            Upwd.setOnEditorActionListener(new OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        signin(arg0);
                    }
                    return false;
                }
            });

            callbackManager = CallbackManager.Factory.create();

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

                }
            };
            accessTokenTracker.startTracking();
            Button fbbutton = (Button) findViewById(R.id.login_button);
            loginButton = new LoginButton(this);
            loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    if (response.getError() != null) {
                                        // handle error
                                    } else {

                                        Bundle bFacebookData = CommonUtils.getFacebookData(me);

                                        if (TextUtils.isEmpty(regId)) {
                                            regId = registerGCM(bFacebookData.get("email").toString(), bFacebookData.get("first_name").toString(),"F",accessToken,"");
                                        }

                                        if (!regId.equals("")) {

                                            if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.READ_PHONE_STATE)
                                                    != PackageManager.PERMISSION_GRANTED) {

                                                ActivityCompat.requestPermissions(SignInActivity.this,
                                                        new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                                            } else {
                                                TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                                String uuid = tManager.getDeviceId();


                                                CustomerEntity customerData=new CustomerEntity();
                                                customerData.setCustomerEmail(bFacebookData.get("email").toString());
                                                customerData.setCustomerPassword(bFacebookData.get("first_name").toString());
                                                customerData.setUserType("C");

                                                CustomerDeviceEntity customerDeviceData=new CustomerDeviceEntity();
                                                customerDeviceData.setDeviceToken(regId);
                                                customerDeviceData.setDeviceName("A");
                                                customerDeviceData.setDeviceUuid(uuid);

                                                customerDeviceModel.setCustomer(customerData);
                                                customerDeviceModel.setDevice(customerDeviceData);

                                                CustomermEntity staffRes= null;
                                                try {
                                                    staffRes = userModule.getfblogin(customerDeviceModel);
                                                } catch (APIServerException e) {
                                                    CommonUtils.Snackbar(Uemail, e.getMessage());
                                                    return;
                                                }

                                                if (staffRes != null) {
                                                    if (staffRes.getCustomer().getCustomerId() != null) {

                                                        try {
                                                            userModule.addStaff(staffRes.getCustomer());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        try {
                                                            settingmodule.CheckSetting(staffRes.getCustomer().getCustomerEmail(),staffRes.getCustomer().getCustomerPassword(),"F");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
                                                        startActivity(intent);
                                                        overridePendingTransition(0, 0);
                                                        SignInActivity.this.finish();

                                                    } else {
                                                        CommonUtils.Snackbar(Uemail, getString(R.string.Try_Again));
                                                    }
                                                }
                                            }
                                        } else {
                                            CommonUtils.Snackbar(Uemail, getString(R.string.Try_Again));
                                        }
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            fbbutton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginButton.performClick();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// Permission Granted


                } else { // Permission Denied
                    Toast.makeText(getApplicationContext(), "Please grant Phone & Storage permissions", Toast.LENGTH_LONG).show();
                    goToSettings();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
            case R.id.signinbutton:
                signin(v);
                break;
            case R.id.forgotpassword:
                intent = new Intent(getApplicationContext(), ForgetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SignInActivity.this.finish();
                break;
            case R.id.new_register:
                intent = new Intent(context, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                SignInActivity.this.finish();
                break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    public class web_updateCallWS extends AsyncTask<String, Void, Void> {
        Integer version;
        String new_version = "0";

        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPostExecute");
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            Log.i("TAG", "doInBackground");
            if (DetectConnection.checkInternetConnection(getApplicationContext())) {
//                new_version = userModule.VersionCheck();
            }
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            int retval = Integer.compare(Integer.valueOf(version), Integer.valueOf(new_version.trim()));
            if (retval < 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignInActivity.this);
                if (Dialog != null && Dialog.isShowing()) {
                } else {
                    alertDialogBuilder.setTitle(context.getString(R.string.you_are_not_updated_title));
                    alertDialogBuilder.setMessage(context.getString(R.string.you_are_not_updated_message));
                    alertDialogBuilder.setIcon(R.drawable.appicon);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton(getString(R.string.Update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SignInActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.mezzofy.mzcoupon")));
                            dialog.cancel();
                        }
                    });
                    Dialog = alertDialogBuilder.create();
                    Dialog.show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public void signin(View view) {

        String useremail = Uemail.getText().toString();
        String password = Upwd.getText().toString();

//        SigninImpl.signin(view,SignInActivity.this,useremail,password,SignInActivity.this);
        if (DetectConnection.checkInternetConnection(SignInActivity.this)) {
//            useremail = Uemail.getText().toString();
//            String password = Upwd.getText().toString();

            if (useremail.length() > 0) {
                if (password.length() > 0) {
                    if (TextUtils.isEmpty(regId)) {
                        regId = registerGCM(useremail, password,"S",null,"");
                    }

                    if (!regId.equals("")) {

                        storeRegistrationId(getApplicationContext(), regId);

                        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(SignInActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        } else {
                            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String uuid = tManager.getDeviceId();

                            CustomerEntity customerData=new CustomerEntity();
                            customerData.setCustomerEmail(useremail);
                            customerData.setCustomerPassword(password);

                            CustomerDeviceEntity customerDeviceData=new CustomerDeviceEntity();
                            customerDeviceData.setDeviceToken(regId);
                            customerDeviceData.setDeviceName("A");
                            customerDeviceData.setDeviceUuid(uuid);

                            customerDeviceModel.setCustomer(customerData);
                            customerDeviceModel.setDevice(customerDeviceData);

                            CustomermEntity staffRes= null;
                            try {
                                staffRes = userModule.getlogin(customerDeviceModel);
                            } catch (APIServerException e) {
                                CommonUtils.Snackbar(Uemail, e.getMessage());
                                return;
                            }

                            if (staffRes != null) {
                                if (staffRes.getCustomer().getCustomerId() != null) {

                                    try {
                                        userModule.addStaff(staffRes.getCustomer());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        settingmodule.CheckSetting(staffRes.getCustomer().getCustomerEmail(),staffRes.getCustomer().getCustomerPassword(),"C");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("staff_id", staffRes.getCustomer().getCustomerId());
                                    editor.putString("user_name", staffRes.getCustomer().getCustomerFirstName());
                                    editor.putString("company_Id", staffRes.getCustomer().getMerchantId());
                                    editor.putString("mobile", staffRes.getCustomer().getCustomerMobile());
                                    editor.putString("profill", staffRes.getCustomer().getCustomerStatus());
                                    editor.putString("agent", staffRes.getCustomer().getUserType());
                                    editor.putString("tokendevice", regId);
                                    editor.commit();

                                    Intent intent = new Intent(context, ProgressActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    SignInActivity.this.finish();

                                } else {
                                    CommonUtils.Snackbar(Uemail, getString(R.string.Try_Again));
                                }
                            } else {
                                CommonUtils.Snackbar(Uemail, getString(R.string.Try_Again));
                            }
                        }
                    }
                } else {
                    CommonUtils.Snackbar(Uemail, getString(R.string.do_not_leave_emty));
                }
            } else {
                CommonUtils.Snackbar(Uemail, getString(R.string.do_not_leave_emty));
            }
        } else {
            CommonUtils.Snackbar(Uemail, getString(R.string.No_Internet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        AppEventsLogger.activateApp(this);

        web_updateCallWS web_updateCallWS = new web_updateCallWS();
        web_updateCallWS.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        accessTokenTracker.stopTracking();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        startActivity(intent);
        SignInActivity.this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    public String findPostion(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {

                        Intent intent = new Intent(getApplicationContext(),CredencialView.class);
                        startActivity(intent);
                    }

                    else
                    {

                    }

                }
                else
                {

                }
                break;
        }

        return null;
    }

}
