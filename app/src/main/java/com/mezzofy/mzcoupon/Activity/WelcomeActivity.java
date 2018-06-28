

package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Adapter.DragListAdapter;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.GCM_Register;
import com.mezzofy.mzcoupon.Entity.CustomerDevicemEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;
import com.mezzofy.mzcoupon.pojo.DrawRes;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("NewApi")
public class WelcomeActivity extends GCM_Register implements View.OnClickListener, ShakeDetector.Listener, AdapterView.OnItemClickListener {

    final Context context = this;

    private boolean doubleBackToExitPressedOnce = false;
    private AlertDialog Dialog;

    private ImageView luckydraw;
    private EditText code;


    private PullToRefreshListView PullRefreshListView;

    private DragListAdapter adapter;

    private ProgressBar progress;

    boolean resval = false;
    private boolean bviewinitilized = false;


    private AsyncCallWSDetail onload;
    private web_updateCallWS web_updateCallWS;


    ShakeDetector sd;
    SensorManager sensorManager;

    private List<Beacon> beaconList = new ArrayList<>();

    private ArrayList<DrawRes> promoList;

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public AlertDialog settDialog;


    private CustomerDevicemEntity customerDeviceModel=new CustomerDevicemEntity();
    private Customer_Module userModule;
    private Setting_Module settingmodule;

    private TextView guest;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.welcome_page);


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            viewInitialisation();

            guest=(TextView)findViewById(R.id.guest);


            userModule=new Customer_Module(WelcomeActivity.this);
            settingmodule=new Setting_Module(WelcomeActivity.this);

            guest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        settingmodule.CheckSetting("guest@coupon.com","guest","G");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context, ProgressActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    WelcomeActivity.this.finish();
                }
            });

//            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//            sd = new ShakeDetector(this);
//
//            onload = new AsyncCallWSDetail();
//            onload.execute();
//
//            web_updateCallWS = new web_updateCallWS();
//            web_updateCallWS.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewInitialisation() {


        if(bviewinitilized)
            return;

        bviewinitilized = true;

        TextView signbtn = (TextView) findViewById(R.id.sign_in);
        signbtn.setOnClickListener(this);
        TextView regbtn = (TextView) findViewById(R.id.register);
        regbtn.setOnClickListener(this);

        ImageView qrcode = (ImageView) findViewById(R.id.qr_code);
        qrcode.setOnClickListener(this);
        luckydraw = (ImageView) findViewById(R.id.lucky_draw);
        luckydraw.setOnClickListener(this);
        PullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView);
        PullRefreshListView.setOnItemClickListener(this);

        progress = (ProgressBar) findViewById(R.id.progress1);
        //beacon = (ImageView) findViewById(R.id.imageView15);
        code = (EditText) findViewById(R.id.text);

        promoList = new ArrayList<>();


           }

    public void updatebeaconList(List<Beacon> list) {
        beaconList = list;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lucky_draw:
                if (code.getText().toString().length() > 0) {
                    addLuckyDraw(luckydraw, code.getText().toString());
                } else {
                    CommonUtils.Snackbar(luckydraw, getString(R.string.code_alert));
                }
                break;
            case R.id.qr_code:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                break;
            case R.id.sign_in:
                intent = new Intent(context, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.register:
                intent = new Intent(context, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            SystemRequirementsChecker.checkWithDefaultDialogs(this);

            viewInitialisation();
//            Applaunch.moddrag.stopBeacon();
//            Applaunch.moddrag.startBeacon();

            if(sd != null)
                sd.start(sensorManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {

//            Applaunch.moddrag.stopBeacon();
            if(sd != null)
                sd.stop();

        } catch (Exception ex) {
        }

        super.onPause();
    }


    @Override
    protected void onStop() {

        try {

//            Applaunch.moddrag.stopBeacon();
            if(sd != null)
                sd.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {

            if (web_updateCallWS != null)
                web_updateCallWS.cancel(true);

            if (onload != null)
                onload.cancel(true);


        } catch (Exception e) {

        }
        super.onDestroy();
    }


    @Override
    public void hearShake() {

//        beaconList = Applaunch.moddrag.getbeaconList();
        if (beaconList != null && beaconList.size() > 0) {
            Vibrator v = (Vibrator) WelcomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);


            if(sd != null)
                sd.stop();

//            DrawRes res = Applaunch.moddrag.getBeaconOnshake(beaconList, luckydraw);
//            if (res != null) {
//                luckyDrawonclick(res);
//                beaconList = new ArrayList<>();
//            } else {
//
//                if(sd != null)
//                    sd.start(sensorManager);
//                CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
//            }
        } else {
            CommonUtils.Snackbar(luckydraw, getString(R.string.try_again));
        }
    }

    public void addLuckyDraw(View view, String code) {

//        DrawRes objdraw = Applaunch.moddrag.addluckydrag(view, code);
//        if (objdraw != null) {
//            luckyDrawonclick(objdraw);
//        } else {
//            CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
//        }
    }

    public void luckyDrawonclick(DrawRes objdraw) {

        if (objdraw != null && objdraw.getTitle() != null) {

//            promoList = daodrag.getPromocodeList();

            if (promoList != null && promoList.size() > 0) {
                adapter = new DragListAdapter(WelcomeActivity.this, promoList);
                PullRefreshListView.setAdapter(adapter);
            }

            if (objdraw.getTitle().equals("MZMerchant Promotion")) {
//                Intent intent = new Intent(WelcomeActivity.this, DragMerchantActivity.class);
//                intent.putExtra("objpromo", objdraw);
//                startActivity(intent);

            }else{
//                Intent intent = new Intent(WelcomeActivity.this, DragDetailActivity.class);
//                intent.putExtra("objpromo", objdraw);
//                startActivity(intent);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DrawRes ret = promoList.get(position - 1);

        if (ret != null && ret.getTitle().equals("MZMerchant Promotion")) {
//            Intent intent = new Intent(WelcomeActivity.this, DragMerchantActivity.class);
//            intent.putExtra("objpromo", ret);
//            startActivity(intent);
        } else {
//            Intent intent = new Intent(WelcomeActivity.this, DragDetailActivity.class);
//            intent.putExtra("objpromo", ret);
//            startActivity(intent);
        }
    }

    private class AsyncCallWSDetail extends AsyncTask<Integer, Void, Void> {

        int position;

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            PullRefreshListView.setClickable(false);
            PullRefreshListView.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

//            promoList = Applaunch.moddrag.getpromocode();

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            progress.setVisibility(View.GONE);
            PullRefreshListView.setClickable(true);
            PullRefreshListView.setEnabled(true);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("SCAN_RESULT");
                Gson gson = new Gson();
                if (result != null) {
                    result.replaceAll("\"", "'");

                    try {
//                        DrawRes res = gson.fromJson(result, DrawRes.class);
//                        resval = daodrag.addPromo(res);
//                        DrawRes objdraw = Applaunch.moddrag.getluckyDraw(luckydraw, res);
//                        luckyDrawonclick(objdraw);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
                    }
                }
            }
        }
        if (resultCode == RESULT_CANCELED) {
            //handle cancel
        }
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
            if (DetectConnection.checkInternetConnection(WelcomeActivity.this)) {
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
            try {
                int retval = Integer.compare(Integer.valueOf(version), Integer.valueOf(new_version.trim()));
                if (retval < 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeActivity.this);
                    if (Dialog != null && Dialog.isShowing()) {
                    } else {
                        alertDialogBuilder.setTitle(context.getString(R.string.you_are_not_updated_title));
                        alertDialogBuilder.setMessage(context.getString(R.string.you_are_not_updated_message));
                        alertDialogBuilder.setIcon(R.drawable.appicon);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton(getString(R.string.Update), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                WelcomeActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.mezzofy.mzcoupon")));
                                dialog.cancel();
                            }
                        });
                        Dialog = alertDialogBuilder.create();
                        Dialog.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        CommonUtils.Snackbar(luckydraw, getString(R.string.click_BACK_exit));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(android.Manifest.permission.READ_PHONE_STATE)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Toast.makeText(getActivity().getApplicationContext(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                    opendialog("Read Phone Status");
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }


    }

    public void opendialog(String msg) {
        try {

            AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(WelcomeActivity.this, R.style.AppCompatAlertDialogStyle);

            if (settDialog != null && settDialog.isShowing()) {


            } else {
                alertDialogBuilder.setTitle("Permission");
                alertDialogBuilder.setMessage("This App needs " + msg + " permission please enable!");
                alertDialogBuilder.setIcon(R.drawable.appicon);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(myAppSettings, 168);
                            dialog.cancel();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                settDialog = alertDialogBuilder.create();
                settDialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}