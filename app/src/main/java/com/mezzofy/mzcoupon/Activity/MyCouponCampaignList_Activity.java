package com.mezzofy.mzcoupon.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Adapter.MercouponAdapter;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;
import com.mezzofy.mzcoupon.pojo.CouponRes;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mezzofy.mzcoupon.R.style.AppCompatAlertDialogStyle;

public class MyCouponCampaignList_Activity extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public AlertDialog Dialog;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    ListView list;
    RelativeLayout layout;

    List<CampaignEntity> CampaignList = null;
    ArrayList<CouponRes> TempCampaignList = null;

    List<CustomerCouponmEntity> CustomerCouponlist;

    int offset = 20, totalCount = 0, count = 0;

    private SwipyRefreshLayout mSwipyRefreshLayout;

    JSONObject jsonobj = null;

    Typeface regular;
    private ProgressBar progress;

    AsyncCallWS Actytask;

    TextView tempvoucher;
    ImageView view17;

    String flag = "pulldown";

    FloatingActionButton mFab;

    ProgressDialog progressDialog;

    private Coupon_Module couponModule;
    private Campaign_Module campaignModule;
    private Merchantsite_Module merchantsiteModule;
    private Setting_Module settingModule;
    Gson gson = new Gson();
    double latitude;
    double longitude;

    String customerId,usertype;
    SharedPreferences settings;


    int Pages = 1;
    private MercouponAdapter merchantCouponlistAdapter;
    private SizemEnity size;

    RelativeLayout relativeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootViewc = null;

        try {
            buildGoogleApiClient();
            rootViewc = inflater.inflate(R.layout.coupon, container, false);

            progress = (ProgressBar) rootViewc.findViewById(R.id.progress1);

            regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

            couponModule = new Coupon_Module(getActivity().getApplicationContext());
            settingModule=new Setting_Module(getActivity().getApplicationContext());

            merchantsiteModule = new Merchantsite_Module(getActivity().getApplicationContext());
            campaignModule=new Campaign_Module(getActivity().getApplicationContext());


            mFab = (FloatingActionButton) rootViewc.findViewById(R.id.fabbutton);
            mFab.setColor(Color.parseColor("#000000"));
            mFab.setDrawable(getResources().getDrawable(R.drawable.table_white));

            settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            CampaignList=null;
            tempvoucher = (TextView) rootViewc.findViewById(R.id.textviewtemp);
            view17 = (ImageView) rootViewc.findViewById(R.id.imageview17);


            relativeLayout = (RelativeLayout) rootViewc.findViewById(R.id.nocouponrl);


            mSwipyRefreshLayout = (SwipyRefreshLayout) rootViewc.findViewById(R.id.swipyrefreshlayout);
            list = (ListView) rootViewc.findViewById(R.id.listview);
            list.setSelector(R.drawable.listselector);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;

                    CampaignEntity album = CampaignList.get(position);

                    if (album.getSize() !=null) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), CouponList_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("status", "A");
                        intent.putExtra("size", album.getSize());
                        startActivity(intent);
//				    MyCouponCampaignList_Activity.this.getActivity().finish();

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("campaignId", album.getCampaignId());
                        editor.putString("exp", "act");
                        editor.commit();
                    }
                }
            });


            mFab.listenTo(list);

            mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    try {
                        if (direction == SwipyRefreshLayoutDirection.TOP) {

                            Pages = 1;
                            CustomerCouponlist = couponModule.getCustomercoupon(customerId, Pages);
                            if (CustomerCouponlist != null && CustomerCouponlist.size() > 0) {
                                merchantCouponlistAdapter = new MercouponAdapter(getActivity(), CampaignList);
                                list.setAdapter(merchantCouponlistAdapter);
                                ListHelper.getListViewSize(list);
                            }
                            mSwipyRefreshLayout.setRefreshing(false);

                        }
                        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                            flag = "pulldown";
                            try {

                                size = (SizemEnity) ObjectSerializer.deserialize(settings.getString("couponsize", ObjectSerializer.serialize(new SizemEnity())));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (++Pages <= size.getPagesize()) {
                                AsyncCallWS task = new AsyncCallWS();
                                task.execute();
                            }
                            else
                                mSwipyRefreshLayout.setRefreshing(false);


                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mFab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), SiteListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootViewc;

    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            if(list!=null) {
                list.setClickable(false);
                list.setEnabled(false);
            }

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(getActivity());
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

                CustomerCouponlist=new ArrayList<>();
                if(customerId!=null)
                    CustomerCouponlist = couponModule.getCustomercoupon(customerId,Pages);

            } catch (Exception e) {
                e.printStackTrace();
           }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();


            if (CustomerCouponlist != null) {
                try {
                    boolean res=campaignModule.updateCampaignflage();
                    if(res)
                        campaignModule.addCampaign(CustomerCouponlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(CustomerCouponlist!=null) {
                try {
                    CampaignList = campaignModule.getCampaignSizeList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(CampaignList!=null) {
                merchantCouponlistAdapter = new MercouponAdapter(getActivity(), CampaignList);
                list.setAdapter(merchantCouponlistAdapter);
                ListHelper.getListViewSize(list);
                relativeLayout.setVisibility(View.GONE);
            }



            mSwipyRefreshLayout.setRefreshing(false);

            try {
                list.setClickable(true);
                list.setEnabled(true);
                if(CampaignList!=null)
                    list.setSelection(CampaignList.size()-20);


            } catch (Exception e) {
                e.printStackTrace();
            }



            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;

            }
            if (CustomerCouponlist != null)
                CustomerCouponlist.clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            try {
                latitude = mCurrentLocation.getLatitude();
                longitude = mCurrentLocation.getLongitude();

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void onResume() {
        super.onResume();

        String userType= null;
        try {
            userType = settingModule.getSettings("Type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        customerId = settings.getString("staff_id", null);
        if(userType!=null && userType.equals("G"))
            signInAlert();
        else {
            Actytask = new AsyncCallWS();
            Actytask.execute();
        }
    }



    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    private void ShowGpsSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), AppCompatAlertDialogStyle);

            if (Dialog != null && Dialog.isShowing()) {


            } else {
                alertDialogBuilder.setTitle("Permission");
                alertDialogBuilder.setMessage("This App needs " + msg + " permission please enable!");
                alertDialogBuilder.setIcon(R.drawable.appicon);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getContext().getPackageName()));
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

    public void signInAlert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), AppCompatAlertDialogStyle);
        builder1.setMessage(getString(R.string.LogIn_msg));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                CouponDB dbHelper = new CouponDB(getContext());
                dbHelper.clearTables();

                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        builder1.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(getContext(), TabViewActivtiy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("currTab",0);
                intent.putExtra("tabName","Deals");
                startActivity(intent);
            }
        });

        builder1.show();
    }

}
