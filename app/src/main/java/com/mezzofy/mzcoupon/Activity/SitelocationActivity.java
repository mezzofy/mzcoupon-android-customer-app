package com.mezzofy.mzcoupon.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.MapSiteRes;


import java.util.ArrayList;

public class SitelocationActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    final Context context = this;
    Double Longitude;
    Double Latitude;
    int specialId;
    String prodid;
    String siteid;
    String specialImg, specialname;
    String address, sitename;
    String prodfav, flag, contact;

    TextView txttitle;

    Merchantsite_Module sitemodule;

    CustomerEntity resval;
    SiteEntity site = null;
    ArrayList<MapSiteRes> listlookup = new ArrayList<>();

    ImageView Mappoint;

    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.sitelocation);
            sitemodule = new Merchantsite_Module(getApplicationContext());

            Bundle extras = getIntent().getExtras();
            //lavanya
            Latitude = extras.getDouble("Latitude");
            Longitude = extras.getDouble("Longitude");
            prodid = extras.getString("prodid");
            sitename = extras.getString("sitename");
            address = extras.getString("address");
            specialId = extras.getInt("specialId");
            specialImg = extras.getString("specialimg");
            specialname = extras.getString("specialname");
            prodfav = extras.getString("prodfav");
            contact = extras.getString("contact");
            flag = extras.getString("flag", "null");

            Mappoint = (ImageView) findViewById(R.id.map);


            Customer_Module daouser = new Customer_Module(getApplicationContext());
            resval = daouser.getUser();


//            listlookup = sitemodule.getMapSiteResList(resval.getPlace_id(), Latitude, Longitude);

            if (listlookup != null && listlookup.size() > 0) {
                MapSiteRes res = new MapSiteRes();

                res.setLatitude(Latitude);
                res.setLongitude(Longitude);
                res.setSiteName("Current Location");
                res.setSiteAddress("");
                res.setMerchantName("");
                listlookup.add(0, res);
            }

            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            // Showing status
            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            } else { // Google Play Services are available

                // Getting reference to the SupportMapFragment of activity_main.xml
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.webView1);

                // Getting GoogleMap object from the fragment
                googleMap = fm.getMap();

                markerOptions = new MarkerOptions();

                if (listlookup != null && listlookup.size() > 0) {
                    for (MapSiteRes reslist : listlookup) {
                        if (reslist != null && reslist.getLatitude() != null && reslist.getLongitude() != null) {

                            googleMap.addMarker(markerOptions.position(new LatLng(reslist.getLatitude(), reslist.getLongitude())).title(reslist.getSiteName()).snippet(reslist.getMerchantName() + "\n" + reslist.getSiteAddress()));

                            if (reslist.getSiteId() != null) {
                                siteid = reslist.getSiteId();
                                site = sitemodule.getMerchantSite(reslist.getSiteId());
                                if (site != null)
                                    sitemodule.addMerchantSite(site);
                            }
                        }
                    }

                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(Latitude, Longitude);

                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    // Showing the current location in Google Map
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Zoom in the Google Map
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                } else {
                    if (Latitude != null && Longitude != null) {
                        googleMap.addMarker(markerOptions.position(new LatLng(Latitude, Longitude)).title("Current Location").snippet(""));

                        // Creating a LatLng object for the current location
                        LatLng latLng = new LatLng(Latitude, Longitude);

                        // Showing the current location in Google Map
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Zoom in the Google Map
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }
                }
            }

            googleMap.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng arg0) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage(R.string.Google_Map_Navigation);
                    builder2.setCancelable(true);
                    builder2.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + Latitude + "," + Longitude));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }
                    });
                    builder2.setNegativeButton(R.string.Cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert12 = builder2.create();
                    alert12.show();
                }
            });

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {

                    if(!marker.getTitle().equals("Current Location")) {
                        final CommonUtils common = new CommonUtils();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(marker.getTitle());
                        builder2.setMessage(marker.getSnippet());
                        builder2.setCancelable(true);
                        builder2.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//                                site = siteDao.getMerchantSite(marker.getPosition().latitude, marker.getPosition().longitude);

                                if (site != null) {

//                                    Intent intent = new Intent(context, OutletDetail_Activity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.putExtra("merchid", site.getMerchantId());
//                                    intent.putExtra("siteId", site.getSiteId());
//                                    intent.putExtra("sitename",site.getSiteName());
//                                    intent.putExtra("flag", "tabview");
//                                    startActivity(intent);
//                                    overridePendingTransition(0, 0);
                                } else {
                                    common.Snackbar(txttitle, getString(R.string.outlet_detail_alert));
                                }
                            }
                        });
                        builder2.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert12 = builder2.create();
                        alert12.show();
                    }
                    return false;
                }
            });


            Mappoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    googleMap.addMarker(markerOptions.position(new LatLng(Latitude, Longitude)).title("Current Location").snippet(""));

                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(Latitude, Longitude);

                    // Showing the current location in Google Map
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Zoom in the Google Map
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

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
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        if (flag == null) {
            Intent intent = new Intent(context, SiteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", prodid);
            intent.putExtra("specialId", specialId);
            intent.putExtra("specialimg", specialImg);
            intent.putExtra("specialname", specialname);
            intent.putExtra("prodfav", prodfav);

            startActivity(intent);
            overridePendingTransition(0, 0);
            SitelocationActivity.this.finish();
        } else {
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //	locationManager.removeUpdates(this);
//	    	stopService(new Intent("server.track.START_TRACK_SERVICE"));
    }

}



