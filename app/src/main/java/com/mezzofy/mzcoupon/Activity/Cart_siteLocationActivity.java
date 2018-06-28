package com.mezzofy.mzcoupon.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mezzofy.mzcoupon.R;

public class Cart_siteLocationActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    final Context context = this;
    LocationManager locationManager;
    Double Longitude;
    Double Latitude;
    int prodid;
    String address, sitename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.sitelocation);

            Bundle extras = getIntent().getExtras();
            Latitude = extras.getDouble("Latitude");
            Longitude = extras.getDouble("Longitude");
            prodid = extras.getInt("prodid");
            sitename = extras.getString("sitename");
            address = extras.getString("address");

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

                if (Latitude != null && Longitude != null) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(sitename).snippet(address));

                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(Latitude, Longitude);

                    // Showing the current location in Google Map
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Zoom in the Google Map
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }


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

            final AlertDialog alert12 = builder2.create();


            googleMap.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng arg0) {
                    // TODO Auto-generated method stub
                    alert12.show();

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
        Intent intent = new Intent(context, Cart_siteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("prodid", prodid);

        startActivity(intent);
        overridePendingTransition(0, 0);
        Cart_siteLocationActivity.this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        //	locationManager.removeUpdates(this);
//	    	stopService(new Intent("server.track.START_TRACK_SERVICE"));
    }

}