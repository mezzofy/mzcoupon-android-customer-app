package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;

import java.util.ArrayList;

public class FavouriteActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public android.support.v7.app.AlertDialog Dialog;
    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;


    RelativeLayout layout;
    final Context context = this;
    ListView list;
    private ArrayList<CampaignmEntity> campaignModel=null;
    private Campaign_Module campaignModule;
    private Merchantsite_Module merchantsiteModule;
    private ProgressBar progress_detail;
    private PullToRefreshListView mPullRefreshListView;
    String prodfav;
    SharedPreferences settings;
    double latitude;
    double langitude;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            buildGoogleApiClient();

            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            campaignModule = new Campaign_Module(context);
            merchantsiteModule = new Merchantsite_Module(context);

            Bundle extras = getIntent().getExtras();
            prodfav = extras.getString("prodfav");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            campaignModel = null;
            campaignModel = campaignModule.getCampaignfavList();

            if (campaignModel.size() == 0) {
                setContentView(R.layout.fav_temp_layout);
            } else {
                setContentView(R.layout.favouritpage);


                progress_detail = (ProgressBar) findViewById(R.id.progress1);

                TextView specialtext = (TextView) findViewById(R.id.txtTitle);
                specialtext.setText(R.string.Favourite);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);


                mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
                list = mPullRefreshListView.getRefreshableView();
                list.setSelector(R.drawable.listselector);
                list.setAdapter(new ImageBaseAdapter(this, campaignModel));
                list.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        position = (int) id;
                        AsyncCallDetail task = new AsyncCallDetail();
                        task.execute(position);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        ArrayList<CampaignmEntity> albumList;
        CampaignEntity campaignData;
        int pos = 0;
        // ImageLoader class instance

        public ImageBaseAdapter(Context _MyContext, ArrayList<CampaignmEntity> _albumList) {
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
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.favouriteadapter, null);
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

            campaignData=albumList.get(position).getCampaign();

            if (campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage() != null && campaignData.getCampaignimages().size() != 0) {
                try {
                    Glide.with(MyContext)
                            .load(albumList.get(position).getCampaign().getCampaignimages().get(0).getCampaignimage().getCampaignImage())
                            .error(R.drawable.appicon)
                            .into(iv);

//                    imgLoader.DisplayImage(albumList.get(position).getProdImageList().get(0).getProductImage(), loader, iv);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                iv.setImageResource(R.drawable.no_image_icon);
            }
            TextView distance=(TextView) MyView.findViewById(R.id.textView6);

            if(distance!=null) {
                if (campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")){
                    distance.setText(" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Days));
                    //distance.setText(campaignData.getOutletDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Days));
                }else{
                    distance.setText(" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Hours));
                   // distance.setText(campaignData.getOutletDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Hours));
                }
            }else {
                if (campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")) {

                    distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Days));
                } else {
                    distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Hours));
                }
            }

//            TextView left = (TextView) MyView.findViewById(R.id.textView8);
//            if(campaignData.getDailyLimitType()!=null && campaignData.getDailyLimitType().equals("A")) {
//
//                left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getSoldCoupon() + " | " + MyContext.getResources().getString(R.string.left) + " " + campaignData.getRedemptionQuota());
//
//            }else {
//                left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getSoldCoupon());
//            }


            bustext.setText(campaignData.getCampaignName());
            bustext.setMaxLines(2);
            bustext.setEllipsize(TruncateAt.END);
            if (settings.getString("decimal", "N").equals("Y")) {
                pricetext.setText(settings.getString("currency", "IDR")+" " + String.format("%,.0f", campaignData.getSellingPrice()));
                if(campaignData.getOrginalPrice()>0) {
                    selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                }
                else {

                    selltext.setVisibility(View.INVISIBLE);

                }

            } else {
                pricetext.setText(settings.getString("currency", "IDR")+" " + String.format("%,.0f", campaignData.getSellingPrice()));
                selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                if (String.format("%,.0f", campaignData.getOrginalPrice()).equals("0.00")) {
                    selltext.setText("");
                }
            }
            selltext.setPaintFlags(selltext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            if (campaignData.getCampaignNote1() != null)
                note1.setText(campaignData.getCampaignNote1());
            else
                note1.setVisibility(View.GONE);
            if (campaignData.getCampaignNote2() != null)
                note2.setText(campaignData.getCampaignNote2());
            else
                note2.setVisibility(View.GONE);
            if (campaignData.getCampaignNote3() != null)
                note3.setText(campaignData.getCampaignNote3());
            else
                note3.setVisibility(View.GONE);

            final ImageView favImage = (ImageView) MyView.findViewById(R.id.imageView2);


            if (campaignData.getFavourite() != null)
                if (campaignData.getFavourite().equals("Y")) {
                    //toggle.setChecked(true);
                    favImage.setImageResource(R.drawable.fav_white
                    );
                }

            favImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (campaignData.getFavourite().equals("N")) {
                        //change to 0 here
                        favImage.setImageResource(R.drawable.fav_white);
                        try {
                            campaignModule.updateCampaignFavourite(campaignData.getCampaignId(), "Y");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        campaignData.setFavourite("Y");
                    } else {
                        favImage.setImageResource(R.drawable.fav);
                        try {
                            campaignModule.updateCampaignFavourite(campaignData.getCampaignId(), "N");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        campaignData.setFavourite("N");
                    }
                }

            });


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



    public class AsyncCallDetail extends AsyncTask<Integer, Void, Void> {

        int position;
        CampaignEntity album = null;


        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress_detail.setVisibility(View.VISIBLE);
        }

        @Override
        protected synchronized Void doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            position = params[0];
            album = campaignModel.get(position).getCampaign();

//            if (album != null) {
//                //ProductRes productRes = productModule.getProduct(album.getProdId());
////                ProductRes productRes = productModule.getProduct(album.getProdId(),latitude,langitude);
//
//                if (productRes != null) {
//                    ProductRes prodres = productDao.getProductRes(productRes.getProdId());
////                    if (prodres != null)
////                        if (!productRes.getHashCode().equals(prodres.getItem_hashCode())) {
//
//                            productDao.updateitem_hashcode(productRes.getProdId(), productRes.getHashCode());
//
//                            ArrayList<GroupRes> Groupitemsdelete = productDao.getProductGroupList(productRes.getProdId());
//                            for (GroupRes groupRes : Groupitemsdelete) {
//                                List<ItemRes> itemRes = itemModule.getItemResList(groupRes.getGroupId());
//                                if (itemRes != null)
//                                    for (ItemRes res : itemRes) {
//
//                                        itemModule.deleteModifier(res.getItemId());
//
//                                    }
//                                itemModule.deleteItem(groupRes.getGroupId());
//                            }
//
//
//                            productDao.deleteProductGroup(productRes.getProdId());
//
//                            productModule.addProduct(productRes);
//
//                            ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(productRes.getProdId());
//                            for (SiteProduct prodsite : Productsite) {
//                                MechantSiteEntity mechantSite = merchantsiteModule.getMerchant_site(prodsite.getSiteId());
//                                if (mechantSite != null) {
//                                    merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                    merchantsiteModule.addMerchant_site(mechantSite);
//
//                                    MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                    if (merchant != null)
//                                        merchantModule.addMerchant(merchant);
//                                }
//                            }
//
//
//                            ArrayList<GroupRes> Groupitems = productDao.getProductGroupList(productRes.getProdId());
//                            for (GroupRes groupRes : Groupitems) {
//                                List<ItemRes> itemRes = itemModule.getItem(groupRes.getGroupId());
//                                if (itemRes != null) {
//                                    int k = 0;
//                                    for (ItemRes res : itemRes) {
//                                        if (k != 0) {
//                                            itemModule.addItem(res);
//                                        }
//                                        k++;
//                                    }
//                                }
//                            }
//
//                        }
////                }
//            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            progress_detail.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(context, Campaigndetail_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", album.getCampaignId());
            intent.putExtra("prodfav", prodfav);
            startActivity(intent);
            overridePendingTransition(0, 0);
            FavouriteActivity.this.finish();

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
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", "");

        if (prodfav.equals("spfav")) {
            intent.putExtra("currTab", 0);
        } else {
            intent.putExtra("currTab", 0);
        }

        startActivity(intent);
        overridePendingTransition(0, 0);
        FavouriteActivity.this.finish();
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
            langitude = mCurrentLocation.getLongitude();

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
            langitude = mCurrentLocation.getLongitude();
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FavouriteActivity.this);
        builder.setTitle("GPS Settings")
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialog.cancel();
                                }catch (Exception e){
                                    e.printStackTrace();
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

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(FavouriteActivity.this, R.style.AppCompatAlertDialogStyle);

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