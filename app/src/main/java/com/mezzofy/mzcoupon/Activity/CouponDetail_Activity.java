package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mezzofy.mzcoupon.Adapter.ImagePagerAdapter;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.CouponOrder_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.StockRes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class CouponDetail_Activity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnClickListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public AlertDialog Dialog;
    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;


    final Context context = this;



    private Coupon_Module couponModule;
    private Merchantsite_Module merchantsiteModule;
    private Campaign_Module campaignModule;
    private Merchant_Module marchantmodule;
    private CouponOrder_Module couponOrderModule;


    String campaignId, staffId;

    String couponId, exp, expday,status,couponstatus;

    StockRes res;
    CampaignmEntity campaignModel = null;
    CampaignEntity campaignData=null;
    CouponEntity couponData=null;
    MerchantEntity merchantData=null;

    public ProgressDialog pDialog;

    SharedPreferences settings;

    RelativeLayout layout;
    CommonUtils common;
    double latitude;
    double longitude;

    TextView email_dtl,redpricetext,selpricetext,proddesctext,termtext,prodtext,expdate,ecoupn,redemption,specialtext,send_gift;
    RelativeLayout relativeLayout3,relativeLayout4,relativeLayout6,relativeLayout5;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            buildGoogleApiClient();

            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.coupondetail);

            couponModule = new Coupon_Module(getApplicationContext());
            merchantsiteModule = new Merchantsite_Module(getApplicationContext());
            campaignModule=new Campaign_Module((getApplicationContext()));
            marchantmodule=new Merchant_Module(getApplicationContext());
            couponOrderModule=new CouponOrder_Module(getApplicationContext());

            common = new CommonUtils();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Setvalue();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Setvalue()
    {

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            expday = extras.getString("expday");
            status = extras.getString("status");
        }


        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        campaignId = settings.getString("campaignId", null);
        couponId = settings.getString("couponId", null);
        exp = settings.getString("exp", null);
        staffId = settings.getString("staff_id", null);




        email_dtl = (TextView) findViewById(R.id.email_dtl);
        redpricetext = (TextView) findViewById(R.id.textView2);
        selpricetext = (TextView) findViewById(R.id.textView3);
        proddesctext = (TextView) findViewById(R.id.textView4);
        termtext = (TextView) findViewById(R.id.lntextView5);
        prodtext = (TextView) findViewById(R.id.textView6);
        expdate = (TextView) findViewById(R.id.textView15);
        ecoupn = (TextView) findViewById(R.id.textView16);
        redemption = (TextView) findViewById(R.id.textView1);
        specialtext = (TextView) findViewById(R.id.txtTitle);
        send_gift = (TextView) findViewById(R.id.send_gift);

        layout = (RelativeLayout) findViewById(R.id.signin_page);


        relativeLayout3 = (RelativeLayout) findViewById(R.id.reviewsz);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.locationz);
        relativeLayout4.setOnClickListener(this);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.callz);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6 = (RelativeLayout) findViewById(R.id.sharez);
        relativeLayout6.setOnClickListener(this);



        if(campaignId!=null)
            try {
                campaignModel = campaignModule.getCampaign(campaignId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        if(couponId!=null)
            try {
                couponData = couponModule.getCoupon(couponId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        try {
            merchantData=marchantmodule.getMerchantList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(campaignModel!=null)
            campaignData=campaignModel.getCampaign();



        if(couponData!=null && campaignData!=null) {

            specialtext.setText(couponData.getCouponName());

            if (campaignData.getDailyLimit() != null && campaignData.getDailyLimit().equals("A")) {
                redemption.setText(getResources().getString(R.string.RedemptionQuota) + " " + campaignData.getRedeemcoupon().toString());
            } else {
                redemption.setText(getResources().getString(R.string.RedemptionQuota) + " " + campaignData.getRedeemcoupon().toString());
                redemption.setVisibility(View.GONE);
            }

            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            ImagePagerAdapter adapter = new ImagePagerAdapter(getApplicationContext(), campaignModel.getCampaign());
            viewPager.setAdapter(adapter);

            Typeface regular = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
            prodtext.setText(campaignData.getCampaignName());
            proddesctext.setText(campaignData.getCampaignDesc());
            proddesctext.setMovementMethod(new ScrollingMovementMethod());
            proddesctext.setTypeface(regular, Typeface.NORMAL);


            if(campaignData.getBrand().equals("C")) {
                selpricetext.setText(getString(R.string.Now) + " " + settings.getString("currency", "IDR") + " " + String.format("%,.0f", campaignData.getSellingPrice()));
                redpricetext.setText(getString(R.string.Original_Price) + " " + settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
            }
            else {
                selpricetext.setVisibility(View.GONE);
                redpricetext.setText("Free MZCoupon");
            }

            termtext.setText(campaignData.getCampaignTc());

            ecoupn.setText("eCoupon : " + couponData.getCouponNo());

            long timestamp = Long.parseLong(couponData.getEndDate());
            Log.d("timestamp po",timestamp+"  - "+couponData.getEndDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(timestamp);

            expdate.setText(getString(R.string.Expiry_Date) + " :" + formattedDate);
        }

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.lineView2);

        linearLayout1.setVisibility(View.GONE);


        linearLayout1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AsyncGroupCallWS task = new AsyncGroupCallWS();
                task.execute();

            }
        });

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.Call) + "\n" + "Remarks" + "");
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "Remarks"));
                startActivity(callIntent);
                overridePendingTransition(0, 0);
            }
        });
        builder1.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        TextView purchtext = (TextView) findViewById(R.id.TextViewR);
        if (couponData!=null && couponData.getCouponStatus()!=null && couponData.getCouponStatus().equals("C")) {
            purchtext.setVisibility(View.GONE);
        }

        Log.d("exp------------",exp);
        if (exp != null) {
            if (exp.equals("exp")) {
                purchtext.setVisibility(View.GONE);
            }
            if (exp.equals("red")) {
                purchtext.setText(R.string.Order_View);
            }

            if (exp.equals("act")) {
                send_gift.setVisibility(View.VISIBLE);
                purchtext.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                send_gift.setVisibility(View.GONE);
            }
        }

        send_gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendAsGiftActivity.class);
                intent.putExtra("couponId", couponId);
                startActivity(intent);
            }
        });


        purchtext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (DetectConnection.checkInternetConnection(getApplicationContext())) {
                    Intent intent;
//                    if(exp!= null && exp.equals("red") && couponId!=null)
//                    {
//                        boolean res=couponOrderModule.getCouponOrderDetailAPI(couponId);
//                        if(res) {
//                            intent = new Intent(context, OrderView_Activity.class);
//                        }
//                        else {
//                            intent = new Intent(context, Redeem_StatusActivity.class);
//                            intent.putExtra("store", "successful");
//                        }
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("expday", expday);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//
//                    }
//                    else
//                    {
//                        CustomerCouponmEntity customerCouponModel=couponModule.getcoupondetailfromserver(couponId);
//                        try {
//                            if(customerCouponModel!=null)
//                                couponModule.addCoupon(customerCouponModel);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        couponData=new CouponEntity();
//                        try {
//                            if(couponId!=null)
//                                couponData=couponModule.getCoupon(couponId);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if(customerCouponModel!=null && customerCouponModel.getCoupon()!=null && customerCouponModel.getCoupon().getCouponStatus().equals("R"))
//                        {
//                            intent = new Intent(context, Redeem_StatusActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra("expday", expday);
//                            intent.putExtra("store", "successful");
//                            startActivity(intent);
//                            overridePendingTransition(0, 0);
//
//                        }
//                        else {
//                            AsyncCallWS task = new AsyncCallWS();
//                            task.execute();
//                        }
//                    }
                    intent = new Intent(context, Redeem_StatusActivity.class);
                    intent.putExtra("store", "successful");

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("expday", expday);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    common.Snackbar(layout, getString(R.string.No_Internet));
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.reviewsz:
                intent = new Intent(context, Coupon_termConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("expday", expday);
                intent.putExtra("txttitle", getString(R.string.Reviews));
                intent.putExtra("prodterm", campaignModel.getCampaign().getCampaignTc());

                startActivity(intent);
                overridePendingTransition(0, 0);

                break;
            case R.id.locationz:
                intent = new Intent(context, Coupon_SiteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("expday", expday);
                startActivity(intent);
                overridePendingTransition(0, 0);

                break;
            case R.id.callz:
                if(merchantData!=null)
                    callAlert();
                break;
            case R.id.sharez:
                share();
                break;
        }

    }

    public class AsyncCallWS extends AsyncTask<String, Void, Void> {
        int stockqty;
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            pDialog = new ProgressDialog(CouponDetail_Activity.this);
            pDialog.setMessage("Loading ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            stockqty=1;
//            stockqty=couponModule.getredeamstockfromserver(couponId,campaignId);
//            if (stockqty >0) {
////                    res = couponModule.getStockTrack(campaignId, couponId);
//            }


            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");

            pDialog.dismiss();
            Intent intent;
            if(stockqty>0)
            {
                if(campaignData!=null && campaignData.getPickup()!=null && campaignData.getPickup().equals("A") || campaignData.getDelivery()!=null && campaignData.getDelivery().equals("A"))
                    intent = new Intent(context, PickupActivity.class);
                else if(campaignData!=null && campaignData.getBooking()!=null && campaignData.getBooking().equals("A") ||campaignData.getOutcall()!=null && campaignData.getOutcall().equals("A"))
                    intent = new Intent(context, Service_PickupActivity.class);

                else
                    intent = new Intent(context, QrCodewalnetActivity.class);
            }
            else {
                intent = new Intent(context, Redeem_StatusActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("stk", "fail");
            intent.putExtra("expday", expday);

            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Setvalue();
    }

    public class AsyncGroupCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            pDialog = new ProgressDialog(CouponDetail_Activity.this);
            pDialog.setMessage("Loading ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");

            pDialog.dismiss();

            Intent intent = new Intent(context, MenuList_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("campaignId", campaignId);
            intent.putExtra("couponId", couponId);
            intent.putExtra("exp", exp);
            intent.putExtra("expday", expday);
            intent.putExtra("form", "coupon");

            startActivity(intent);
            overridePendingTransition(0, 0);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");

        }
    }



    public void callAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.Call) + "\n" + merchantData.getMerchantName() + "");
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + merchantData.getMerchantHotline()));
                startActivity(callIntent);
                overridePendingTransition(0, 0);
            }
        });
        builder1.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void share() {
        File file = null;

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("coupon://post?campaignId=" + campaignId + "&coupid=" + couponId + "&expdate=" + expday)
                .setTitle(campaignModel.getCampaign().getCampaignName())
                .setContentDescription(settings.getString("currency", "IDR") + String.format("%,.2f", campaignModel.getCampaign().getSellingPrice()))
                .setContentImageUrl(campaignModel.getCampaign().getCampaignimages().get(0).getCampaignimage().getCampaignImage()).setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

        LinkProperties linkProperties = new LinkProperties().setFeature("sharing");
        branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    StringBuffer shareBody = new StringBuffer();
                    shareBody.append(url);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = null;

        if (exp != null)
            if (exp.equals("act")) {
                intent = new Intent(context, CouponList_Activity.class);
                intent.putExtra("status", "A");
            } else if (exp.equals("red")) {
                intent = new Intent(context, TabViewActivtiy.class);
                intent.putExtra("tabName", "");
                intent.putExtra("currTab", 1);
                intent.putExtra("status", "R");
                intent.putExtra("coupstats", "1");
            } else if (exp.equals("exp")) {
                intent = new Intent(context, TabViewActivtiy.class);
                intent.putExtra("tabName", "");
                intent.putExtra("currTab", 1);
                intent.putExtra("status", "E");
                intent.putExtra("coupstats", "2");
            }
        intent.putExtra("exp", exp);
        intent.putExtra("campaignId", campaignId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        isLocationEnabled();
        if (!isLocationEnabled()) {
            ShowGpsSettings();
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        isLocationEnabled();
        if (!isLocationEnabled()) {
            ShowGpsSettings();
            return;
        }

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();

        }

        startLocationUpdates();

    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        try {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    private void ShowGpsSettings() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CouponDetail_Activity.this);
        builder.setTitle("GPS Settings")
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                dialog.cancel();
                            }
                        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.dismiss();
            }
        });
        //builder.setCancelable(false);
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    opendialog("Read Location");
                }
            }
            if (permissions[i].equals(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    opendialog("Read Location");
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void opendialog(String msg) {
        try {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(CouponDetail_Activity.this, R.style.AppCompatAlertDialogStyle);

            if (Dialog != null && Dialog.isShowing()) {


            } else {
                alertDialogBuilder.setTitle("Permission");
                alertDialogBuilder.setMessage("This App needs " + msg + " permission please enable!");
                alertDialogBuilder.setIcon(R.drawable.appicon);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName()));
                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(myAppSettings, 168);
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Dialog = alertDialogBuilder.create();
                Dialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}