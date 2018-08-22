package com.mezzofy.mzcoupon.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.Size;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.R;

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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.CouponRes;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class CouponList_Activity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public AlertDialog Dialog;
    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    RelativeLayout layout;
    ListView listview;

    private Coupon_Module couponModule;
    private Merchantsite_Module merchantsiteModule;

   private ImageBaseAdapter imageBaseAdapter;

    String campaignId,customerId;
    SizemEnity size;

    JSONObject jsonobj = null;

    List<CustomerCouponmEntity> CustomerCouponlist;


    Typeface regular;

    private ProgressBar progress;
    private ProgressBar progress_detail;

    AsyncOnload Actytask;

    String flag = "pulldown";
    String status, exp;
    SharedPreferences settings;
    ProgressDialog progressDialog;

    double latitude;
    double longitude;

    private SwipyRefreshLayout mSwipyRefreshLayout;
    int Pages=1,limit=0,offset=20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.merc_coupon_page);

            buildGoogleApiClient();


            couponModule = new Coupon_Module(getApplicationContext());
            merchantsiteModule = new Merchantsite_Module(getApplicationContext());


            progress = (ProgressBar) findViewById(R.id.progress1);
            progress_detail = (ProgressBar) findViewById(R.id.progress1);

            regular = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            Bundle extras = getIntent().getExtras();
            status = extras.getString("status");

            Log.d("status----------",status);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            customerId = settings.getString("staff_id", null);
            campaignId = settings.getString("campaignId",null);
            exp = settings.getString("exp",null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            listview = (ListView) findViewById(R.id.listview);
            mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);



            listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    position = (int) id;

                    CouponEntity couponData=CustomerCouponlist.get(position).getCoupon();


                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("campaignId", couponData.getCampaignId());
                    editor.putString("couponId", couponData.getCouponId());
                    editor.putString("exp", "act");
                    editor.putString("expday",  couponData.getEndDate());
                    editor.commit();



                    Intent intent = new Intent(getApplicationContext(), CouponDetail_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("expday", couponData.getEndDate());
                    intent.putExtra("flag", "send_gift");
                    startActivity(intent);

                }
            });


            mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    try {
                        if (direction == SwipyRefreshLayoutDirection.TOP) {

                            Pages=1;
                            limit=0;

                            try {
                                CustomerCouponlist = couponModule.getCampiagncoupon(campaignId,customerId,status,Pages);
                                if(CustomerCouponlist!=null && CustomerCouponlist.size()>0){
                                    imageBaseAdapter = new ImageBaseAdapter(getApplicationContext(), CustomerCouponlist);
                                    listview.setAdapter(imageBaseAdapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSwipyRefreshLayout.setRefreshing(false);

                        }
                        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                            flag = "pulldown";
                            size=(SizemEnity) ObjectSerializer.deserialize(settings.getString("size", ObjectSerializer.serialize(new SizemEnity())));

                            if(++Pages<=size.getPagesize()) {
                                limit+=offset;
                                AsyncOnload onstart = new AsyncOnload();
                                onstart.execute();
                            }
                            else
                                mSwipyRefreshLayout.setRefreshing(false);


                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<CustomerCouponmEntity> albumList;
        int pos = 0;
        Boolean favclk = false;
        CouponRes res;

        public ImageBaseAdapter(Context _MyContext, List<CustomerCouponmEntity> _albumList) {
            albumList = null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList = _albumList;
//            res = merCouponDao.getCouponList(campaignId, "A");
        }

        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                MyView = li.inflate(R.layout.couponlist_detail, null);
            } else {
                MyView = convertView;
            }
            ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
            TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
            TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
            TextView datetext = (TextView) MyView.findViewById(R.id.textView3);
            TextView desc = (TextView) MyView.findViewById(R.id.textView14);
            //TextView redeemded=(TextView) MyView.findViewById(R.id.textView8);
            //redeemded.setText(albumList.get(position).getRedemptionQuota());

            bustext.setTypeface(regular, Typeface.BOLD);
            pricetext.setTypeface(regular, Typeface.BOLD);
            datetext.setTypeface(regular, Typeface.BOLD);
            desc.setTypeface(regular);

            CouponEntity couponData=new CouponEntity();
            couponData=albumList.get(position).getCoupon();


            if (couponData.getProductImageurl() != null && !couponData.getProductImageurl().equals("")) {

                try {
                    Glide.with(MyContext)
                            .load(couponData.getProductImageurl())
                            .error(R.drawable.appicon)
                            .into(iv);
//                    imgLoader.DisplayImage(albumList.get(position).getProductImage(), loader, iv);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                iv.setImageResource(R.drawable.no_image_icon);
            }

            bustext.setText(couponData.getCouponName());
            desc.setText(couponData.getProductDesc());
            desc.setMaxLines(3);
            desc.setEllipsize(TruncateAt.END);

            pricetext.setText(couponData.getCouponNo());

//            if (couponData.getReferEmail() != null) {
//                pricetext.setText(couponData.getCouponNo() + "/" + "\n" + getResources().getString(R.string.send_as_gift));
//            } else if (couponData.getCouponStatus().equals("C")) {
//                pricetext.setText(getString(R.string.Pending));
//            } else {
//                pricetext.setText(couponData.getCouponNo());
//            }
//
//            pricetext.setMaxLines(2);
//            pricetext.setEllipsize(TruncateAt.END);
//
//				if(settings.getString("decimal","N").equals("Y")) {
//					pricetext.setText(" " + settings.getString("currency", "IDR") + String.format("%,.2f", albumList.get(position).getCoupon().getSellingPrice()) + " ");
//				}else{
//					pricetext.setText(" " + settings.getString("currency", "IDR") + String.format("%,.0f", albumList.get(position).getCoupon().getSellingPrice()) + " ");
//				}

            long timestamp = Long.parseLong(couponData.getEndDate());
            Log.d("timestamp po",timestamp+"  - "+couponData.getEndDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(timestamp);
            datetext.setText(formattedDate);

            return MyView;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return albumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }


    private class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            listview.setClickable(false);
            listview.setEnabled(false);

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(CouponList_Activity.this);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            try {

                CustomerCouponlist = couponModule.getCampiagncoupon(campaignId,customerId,status,Pages);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();


            if (CustomerCouponlist != null && CustomerCouponlist.size()>0){
                try {
                    boolean res = couponModule.updateCouponflage(status);
                    if(res)
                        couponModule.addCoupon(CustomerCouponlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            CustomerCouponlist = null;
            try {
                if(campaignId!=null)
                 CustomerCouponlist = couponModule.getCampaignCouponList(campaignId);
            } catch (Exception e) {
                e.printStackTrace();
            }




            if (CouponList_Activity.this != null) {

                imageBaseAdapter = new ImageBaseAdapter(getApplicationContext(), CustomerCouponlist);
                listview.setAdapter(imageBaseAdapter);
            }


            listview.setClickable(true);
            listview.setEnabled(true);


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        Actytask = new AsyncOnload();
        Actytask.execute();
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
    protected void onStop() {Actytask.cancel(true);
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
            latitude=mCurrentLocation.getLatitude();
            longitude=mCurrentLocation.getLongitude();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(CouponList_Activity.this);
        builder.setTitle("GPS Settings")
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialog.cancel();
                                } catch (Exception E) {
                                    E.printStackTrace();
                                }
                            }
                        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.dismiss();
            }
        });


        builder.setCancelable(false);
        AlertDialog alert = builder.create();
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

            AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(CouponList_Activity.this, R.style.AppCompatAlertDialogStyle);

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