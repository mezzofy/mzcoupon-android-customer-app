package com.mezzofy.mzcoupon.Activity;

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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Adapter.MerCouponRedeemAdapter;
import com.mezzofy.mzcoupon.Adapter.MercouponExpAdapter;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.CouponRes;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 12/06/2015.
 */
public class CouponExpList_Activity extends Fragment implements
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

    private Coupon_Module couponModule;
    private Merchant_Module merchantModule;
    private Merchantsite_Module merchantsiteModule;
    private Campaign_Module productModule;


    List<CustomerCouponmEntity> ExpCouponList = new ArrayList<CustomerCouponmEntity>();
    List<CustomerCouponmEntity> TempCouponList = new ArrayList<CustomerCouponmEntity>();
    List<CustomerCouponmEntity> transexpcouponList;

    int Pages = 1,limit=0;
    int offset = 20;
    String CustomerId;
    private SizemEnity size;

    private SwipyRefreshLayout mSwipyRefreshLayout;
    JSONObject jsonobj = null;
    List<CouponRes> transactioncouponList;

    Typeface regular;
    private ProgressBar progress;

    ExpAsyncOnload Exptask;

    String flag = "pulldown";

    TextView tempvoucher;
    ImageView view17;

//    FloatingActionButton mFab;

    SharedPreferences settings;

    String status, exp;

    Gson gson = new Gson();

    double latitude;
    double logitude;

    ExpAsyncOnload Actytask;
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.couponexp, container, false);
        try{

            buildGoogleApiClient();

        status = "E";

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            CustomerId = settings.getString("staff_id",null);




        couponModule = new Coupon_Module(getActivity().getApplicationContext());
        merchantModule = new Merchant_Module(getActivity().getApplicationContext());
        merchantsiteModule = new Merchantsite_Module(getActivity().getApplicationContext());
        productModule = new Campaign_Module(getActivity().getApplicationContext());


        progress = (ProgressBar) rootView.findViewById(R.id.progress1);

        regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

//        mFab = (FloatingActionButton) rootView.findViewById(R.id.fabbutton);
//        mFab.setColor(Color.parseColor("#000000"));
//        mFab.setDrawable(getResources().getDrawable(R.drawable.table_white));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tempvoucher = (TextView) rootView.findViewById(R.id.textViewtemp);
        view17 = (ImageView) rootView.findViewById(R.id.imageView17);
            mSwipyRefreshLayout = (SwipyRefreshLayout) rootView.findViewById(R.id.swipyrefreshlayout);
            list = (ListView) rootView.findViewById(R.id.listview);
            list.setSelector(R.drawable.listselector);


            relativeLayout = (RelativeLayout) rootView.findViewById(R.id.nocouponrl);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                position = (int) id;

                AsyncCallWSDetail task = new AsyncCallWSDetail();
                task.execute(position);

            }
        });

//        mFab.listenTo(list);



            mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    try {
                        if (direction == SwipyRefreshLayoutDirection.TOP) {

                            limit=0;
                            Pages=1;
                            ExpCouponList = couponModule.getcouponlistfromserver(CustomerId, status,Pages);
                            if (ExpCouponList != null && ExpCouponList.size() > 0) {
                                list.setAdapter(new MercouponExpAdapter(getActivity(), ExpCouponList));
                                ListHelper.getListViewSize(list);
                            }
                            mSwipyRefreshLayout.setRefreshing(false);

                        }
                        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                            flag = "pulldown";
                            try {

                                size = (SizemEnity) ObjectSerializer.deserialize(settings.getString("size", ObjectSerializer.serialize(new SizemEnity())));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (++Pages <= size.getPagesize()) {
                                ExpAsyncOnload task = new ExpAsyncOnload();
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



//        mFab.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                MerchantDao merchantDao = new MerchantDao(getActivity());
//                ArrayList<MZMerchant> merchantList = merchantDao.getMerchantList();
//
//                if (merchantList.size() > 0)
//                    if (merchantList.size() == 1) {
//                        Intent intent = new Intent(getActivity(), MerchantListActivtiy.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                    } else {
//                        Intent intent = new Intent(getActivity(), MerchantListActivtiy.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                    }
//            }
//        });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }


    private class ExpAsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            if(list!=null) {
                list.setClickable(false);
                list.setEnabled(false);
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            try {
                TempCouponList = couponModule.getcouponlistfromserver(CustomerId, status,Pages);

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();


            if (TempCouponList != null){
                try {
                    boolean res=couponModule.updateCouponflage(status);
                    if(res)
                        couponModule.addCoupon(TempCouponList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if(TempCouponList!=null && TempCouponList.size()>0) {
                try {
                    ExpCouponList.addAll(couponModule.getCouponList(status, limit, offset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (ExpCouponList!=null && ExpCouponList.size() == 0) {
                tempvoucher.setVisibility(View.VISIBLE);
                view17.setVisibility(View.VISIBLE);

            } else {
                tempvoucher.setVisibility(View.GONE);
                view17.setVisibility(View.GONE);
            }



            if (ExpCouponList != null && ExpCouponList.size() > 0) {
                list.setAdapter(new MercouponExpAdapter(getActivity(), ExpCouponList));
                ListHelper.getListViewSize(list);
                relativeLayout.setVisibility(View.GONE);
            }

            progress.setVisibility(View.INVISIBLE);
            if(list!=null)
            {
                list.setClickable(true);
                list.setEnabled(true);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    private class AsyncCallWSDetail extends AsyncTask<Integer, Void, Void> {

        int position;
        CustomerCouponmEntity album = null;

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            position = params[0];
            album = ExpCouponList.get(position);
//lat,lnt check aruna
            CouponEntity expcoupondata=album.getCoupon();
            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            progress.setVisibility(View.INVISIBLE);
            list.setClickable(true);
            list.setEnabled(true);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("campaignId",album.getCoupon().getCampaignId());
            editor.putString("couponId", album.getCoupon().getCouponId());
            editor.putString("exp", "exp");
            editor.putString("expday", album.getCoupon().getEndDate());
            editor.commit();

            Intent intent = new Intent(getActivity().getApplicationContext(), CouponDetail_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
    public void onResume() {
        super.onResume();
        status = "E";
        Actytask = new ExpAsyncOnload();
        Actytask.execute();
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
        if(Exptask!=null)
         Exptask.cancel(true);
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
                logitude = mCurrentLocation.getLongitude();
            }catch (Exception E){
                E.printStackTrace();
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
            logitude = mCurrentLocation.getLongitude();
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

       // builder.setCancelable(false);
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

            AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

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
}
