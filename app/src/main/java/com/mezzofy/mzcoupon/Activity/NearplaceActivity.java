package com.mezzofy.mzcoupon.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.MapSiteRes;

import java.util.ArrayList;

public class NearplaceActivity extends FragmentActivity implements LocationListener {

    final Context context = this;

    GoogleMap googleMap;
    ArrayList<MapSiteRes> siteList = null;

    static double lat;
    static double log;

    private Merchantsite_Module merchantsiteModule;
    LocationManager locationManager;
    int company_Id;
    AlertDialog alert11;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //  meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5000; //  minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.map_places);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            merchantsiteModule = new Merchantsite_Module(NearplaceActivity.this);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            company_Id = settings.getInt("company_Id", 0);

            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            // Showing status
            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            } else { // Google Play Services are available

                // Getting reference to the SupportMapFragment of activity_main.xml
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                // Getting GoogleMap object from the fragment
                googleMap = fm.getMap();

                // Enabling MyLocation Layer of Google Map
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // Getting LocationManager object from System Service LOCATION_SERVICE
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//            // Creating a criteria object to retrieve provider
//            Criteria criteria = new Criteria();
//
//            // Getting the name of the best provider
//            String provider = locationManager.getBestProvider(criteria, true);
//
//            // Getting Current Location
//            Location location = locationManager.getLastKnownLocation(provider);


                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                }


   //        if(location!=null){
//                onLocationChanged(location);
//            }
//            locationManager.requestLocationUpdates(provider, 50000, 10, this);//5000millsec , 10meter

                googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {
                        // TODO Auto-generated method stub
                        //Log.d("Marker", siteList.get(Integer.parseInt(arg0.getId().substring(1))).getSiteId().toString());
//					    Intent intent;
//		 		        intent = new Intent(context, TabMainActivity.class);
//		 		        intent.putExtra("currTab", 0);
//		 			    intent.putExtra("tabName", "");
//					    intent.putExtra("MerchantId", siteList.get(Integer.parseInt(arg0.getId().substring(1))).getMerchantId());
//  					intent.putExtra("SiteId", siteList.get(Integer.parseInt(arg0.getId().substring(1))).getSiteId());
//          			startActivity(intent);
//          			overridePendingTransition(0, 0);
                        alertMethod(siteList.get(Integer.parseInt(arg0.getId().substring(1)) + 1).getSiteContact().toString());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        NearplaceActivity.this.finish();
        Intent intent;
        intent = new Intent(context, TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 4);
        startActivity(intent);
        overridePendingTransition(0, 0);

    }

    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
//        LatLng latLng = new LatLng(1.557971, 103.796394);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

//        siteList = merchantsiteModule.getMapSiteResList(company_Id, latitude, longitude);
//        siteList = merchantsiteModule.getMapSiteResList(company_Id,1.557971, 103.796394);

        int m = 0;
        if (siteList != null)
            for (MapSiteRes b : siteList) {
                if (m != 0) {
                    //getLatLongFromAddress(b.getAddress().trim().replace("\r\n", ""));
                    if (!b.getLatitude().equals("null") && !b.getLatitude().equals("")) {
                        LatLng ll = new LatLng(Double.valueOf(b.getLatitude()), Double.valueOf(b.getLongitude()));

                        googleMap.addMarker(new MarkerOptions().position(ll).title(b.getSiteName().trim()).snippet(b.getSiteAddress().trim()));
                    }
                }
                m++;
            }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

//    public static void getLatLongFromAddress(String youraddress) throws UnsupportedEncodingException {
//        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(youraddress, "UTF-8") + "&sensor=false";
//        HttpGet httpGet = new HttpGet(uri);
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//
//            log = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lng");
//
//            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lat");
//
//            Log.d("latitude", String.valueOf(lat));
//            Log.d("longitude", String.valueOf(log));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    public void alertMethod(String numb) {

        final String[] cnumb = numb.split("\\|");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(getString(R.string.Call) + "\n" + cnumb[0]);
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cnumb[0]));
                if (ActivityCompat.checkSelfPermission(NearplaceActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
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


        alert11 = builder1.create();
        alert11.show();
    }
}


