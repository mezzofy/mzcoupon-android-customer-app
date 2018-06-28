package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Entity.CampGrpDetailEntity;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpecialDetailActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public AlertDialog Dialog;
    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    RelativeLayout layout;
    final Context context = this;

    ListView list;
    List<CampaignmEntity> prodList = null;


    private Campaign_Module campaignModule;


    String CampgrpId;
    int offset = 0, totalCount = 0;
    int size = 10;

    String CampgrpImageurl, CampgrpName;

    JSONObject jsonobj = null;

    private PullToRefreshListView mPullRefreshListView;
    List<CampGrpDetailEntity> productIdslist;

    String flag = "pulldown";

    private ProgressBar progress;
    private ProgressBar progress_detail;

    SharedPreferences settings;
    double latitude;
    double longitude;
    String CamIdforDetail=null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            buildGoogleApiClient();

            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.specialdetail);
            campaignModule = new Campaign_Module(context);

            progress = (ProgressBar) findViewById(R.id.progress1);
            progress_detail = (ProgressBar) findViewById(R.id.progress1);

            Bundle extras = getIntent().getExtras();
            CampgrpId = extras.getString("CampgrpId");
            CampgrpImageurl = extras.getString("CampgrpImageurl");
            CampgrpName = extras.getString("CampgrpName");


            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);



            productIdslist = campaignModule.getCampaginGrpProductlist(CampgrpId);

            if(productIdslist!=null) {
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }

            mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
            list = mPullRefreshListView.getRefreshableView();
            list.setSelector(R.drawable.listselector);

            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;
                    CamIdforDetail=prodList.get(position).getCampaign().getCampaignId();
                    AsyncCallDetail task = new AsyncCallDetail();
                    task.execute(position);
                }
            });

            ImageView img = (ImageView) findViewById(R.id.imageView1);
            int loader = R.drawable.no_image_icon;
            Glide.with(SpecialDetailActivity.this)
                    .load(CampgrpImageurl)
                    .error(R.drawable.no_image_icon)
                    .into(img);

            mPullRefreshListView.setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START : Mode.BOTH);
            mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                    flag = "pullup";



                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    // TODO Auto-generated method stub


                    flag = "pulldown";


                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<CampaignmEntity> albumList;
        int pos = 0;

        public ImageBaseAdapter(Context _MyContext, List<CampaignmEntity> _albumList) {
            albumList = null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList = _albumList;
        }

        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView = null;
            pos = position;
            try {
                if (convertView == null) {
                    LayoutInflater li = getLayoutInflater();
                    MyView = li.inflate(R.layout.speciallayout, null);
                } else {
                    MyView = convertView;
                }

                ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
                TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
                TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
                TextView selltext = (TextView) MyView.findViewById(R.id.textView3);
                TextView note1 = (TextView) MyView.findViewById(R.id.TextView01);
                TextView note2 = (TextView) MyView.findViewById(R.id.TextView02);
                TextView note3 = (TextView) MyView.findViewById(R.id.TextView03);
                TextView distance=(TextView)MyView.findViewById(R.id.textView6);


                ImageView catgryimg = (ImageView) MyView.findViewById(R.id.imageView8);
                final ImageView favImage = (ImageView) MyView.findViewById(R.id.imageView2);

                CampaignEntity campaignData=new CampaignEntity();
                campaignData=albumList.get(position).getCampaign();

                if (campaignData!=null && campaignData.getCampaignimages()!=null && campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage() != null) {
                    try {
                        Glide.with(MyContext)
                                .load(campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage())
                                .error(R.drawable.appicon)
                                .into(iv);
                        Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    iv.setImageResource(R.drawable.no_image_icon);
                }

                if(distance!=null) {
                    if(campaignData.getExpiryname()!=null)
                        if (campaignData.getExpiryname().equals("D")){
                            distance.setText(campaignData.getDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Days));
                        }else{
                            distance.setText(campaignData.getDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Hours));
                        }
                }else {
                    if(campaignData.getExpiryname()!=null)
                        if (campaignData.getExpiryname().equals("D")) {

                            distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Days));
                        } else {
                            distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Hours));
                        }
                }

                TextView left = (TextView) MyView.findViewById(R.id.textView8);
                if(campaignData.getDailyLimit()!=null && campaignData.getDailyLimit().equals("A")) {

                    left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() + " | " + MyContext.getResources().getString(R.string.left) + " " + campaignData.getRedeemcoupon());

                }else {
                    left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() );
                }

                bustext.setText(campaignData.getCampaignName());
                bustext.setMaxLines(2);
                bustext.setEllipsize(TextUtils.TruncateAt.END);


                if(campaignData.getBrand()!=null && campaignData.getBrand().equals("F"))
                {
                    pricetext.setText("Free MZCoupon");
                    selltext.setVisibility(View.INVISIBLE);
                }
                else {

                    if (settings.getString("decimal", "N").equals("Y")) {
                        pricetext.setText(settings.getString("currency", "IDR") + " " + String.format("%,.0f", campaignData.getSellingPrice()));
                        if (campaignData.getOrginalPrice() > 0) {
                            selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                        } else {

                            selltext.setVisibility(View.INVISIBLE);

                        }

                        if (String.format("%,.0f", campaignData.getOrginalPrice()).equals("0.0")) {
                            selltext.setText("");
                        }
                    } else {
                        pricetext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getSellingPrice()));
                        selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                        if (String.format("%,.0f", campaignData.getOrginalPrice()).equals("0.0")) {
                            selltext.setText("");
                        }
                    }
                }

                if(campaignData.getFavourite()!=null && campaignData.getFavourite().equals("Y"))
                    favImage.setImageResource(R.drawable.fav_white);
                else
                    favImage.setImageResource(R.drawable.fav);


                selltext.setPaintFlags(selltext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



                Log.d("getCampaignNote1--",campaignData.getCampaignNote1());
                if (campaignData.getCampaignNote1() != null) {
                    note1.setText(campaignData.getCampaignNote1());
                } else {
                    note1.setVisibility(View.GONE);
                }
                if (campaignData.getCampaignNote2() != null) {
                    note2.setText(campaignData.getCampaignNote2());
                } else {
                    note2.setVisibility(View.GONE);
                }
//        if (campaignData.getCampaignNote3() != null) {
//            note3.setText(albumList.get(position).getCampaignNote3());
//        } else {
//            note3.setVisibility(View.GONE);
//        }

                if (campaignData.getFavourite() != null)
                    if (campaignData.getFavourite().equals("Y")) {
                        //toggle.setChecked(true);
                        favImage.setImageResource(R.drawable.fav_white);
                    } else {
                        favImage.setImageResource(R.drawable.fav);
                    }
                favImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (albumList.get(position).getCampaign().getFavourite()!=null && albumList.get(position).getCampaign().getFavourite().equals("N")) {
                            favImage.setImageResource(R.drawable.fav_white);
                            try {
                                campaignModule.updateCampaignFavourite(albumList.get(position).getCampaign().getCampaignId(), "Y");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            albumList.get(position).getCampaign().setFavourite("Y");

                        } else {
                            favImage.setImageResource(R.drawable.fav);
                            try {
                                campaignModule.updateCampaignFavourite(albumList.get(position).getCampaign().getCampaignId(), "N");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            albumList.get(position).getCampaign().setFavourite("N");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

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


    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        ArrayList<CampaignmEntity> campaignModels=new ArrayList<CampaignmEntity>();
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            if(list!=null) {
                list.setClickable(false);
                list.setEnabled(false);
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            for(CampGrpDetailEntity campGrpDetailData:productIdslist) {
                    String cmpid = campGrpDetailData.getCampaignId();
                    CampaignEntity tempcampaignData = campaignModule.getCampaignDetailfromServerAPI(cmpid, latitude, longitude);

                    if (tempcampaignData != null) {
                        CampaignmEntity temp = new CampaignmEntity();
                        temp.setCampaign(tempcampaignData);
                        campaignModels.add(temp);
                    }
            }
            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");

            mPullRefreshListView.onRefreshComplete();
            if(list!=null) {
                list.setClickable(true);
                list.setEnabled(true);
            }


            if(campaignModels!=null && campaignModels.size()>0)
            {
                for(CampaignmEntity tempCmpModel:campaignModels) {
                    try {
                        campaignModule.addCampaign(tempCmpModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            prodList = null;
            try {
                prodList = campaignModule.getCampaigngroupList(productIdslist);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(prodList!=null)
                list.setAdapter(new ImageBaseAdapter(getApplicationContext(), prodList));

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    public class AsyncCallDetail extends AsyncTask<Integer, Void, Void> {


        CampaignEntity campaignData = null;


        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress_detail.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            //CampaignDetail
                Log.d("mlatitude  mlongitude--","     "+latitude);
                if(latitude>0 || longitude>0) {
                    campaignData = campaignModule.getCampaignDetailfromServerAPI(CamIdforDetail, latitude, longitude);

                }

                return null;
            }

            @Override
            protected synchronized void onPostExecute(Void result) {
                Log.i("TAG", "onPostExecute");
                // new Thread(myThread).start();
                try {
                    list.setClickable(true);
                    list.setEnabled(true);

                    if(campaignData!=null) {
                        CampaignmEntity campaignModel=new CampaignmEntity();
                        campaignModel.setCampaign(campaignData);
                        campaignModule.addCampaign(campaignModel);
                    }

                    Intent intent = new Intent(context, Campaigndetail_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("prodid",CamIdforDetail);
                    intent.putExtra("listprod", list.getFirstVisiblePosition());

                    startActivity(intent);
                    overridePendingTransition(0, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);
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
            // new Thread(myThread).start();
            progress.setVisibility(View.INVISIBLE);
            list.setClickable(true);
            list.setEnabled(true);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(context, TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", getString(R.string.menu_Products));
        intent.putExtra("currTab", 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
        SpecialDetailActivity.this.finish();
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SpecialDetailActivity.this);
        builder.setTitle("GPS Settings")
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialog.cancel();
                                }catch (Exception E){
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

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SpecialDetailActivity.this, R.style.AppCompatAlertDialogStyle);

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