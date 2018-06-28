package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.OrdermEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.CouponOrder_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;


import com.mezzofy.mzcoupon.pojo.SiteProduct;
import com.mezzofy.mzcoupon.pojo.StockRes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mezzofy.mzcoupon.apputills.AppLocationService;
import com.mezzofy.mzcoupon.apputills.CommonUtils;


/**
 * Created by LENOVO on 12/06/2015.
 */
public class Service_PickupActivity extends Activity {


    ListView listMsite, listordty;
    final Context context = this;

    private Merchantsite_Module merchantsiteModule;
    private Campaign_Module campaignModule;
    private CouponOrder_Module couponOrderModule;

    ArrayList<SiteProduct> siteProducts = null;
    List<SiteEntity> mechantSites = null;


    int prodid, compysiteId;
    String siteId,Ordertype;

    String couponId, exp,campaignId,staffId;
    String redeem;

    EditText custloctxt;
    RelativeLayout locview;
    Button btnloc;
    TextView txthder, txtct;

    StockRes res;

    AppLocationService appLocationService;
    CampaignEntity campaignData;
    SiteEntity siteData;

    Geocoder geocoder;
    List<Address>currenctAddress;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.pickup_outlet);

            merchantsiteModule = new Merchantsite_Module(context);
            campaignModule = new Campaign_Module(context);
            couponOrderModule = new CouponOrder_Module(context);
            appLocationService = new AppLocationService(Service_PickupActivity.this);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);

            TextView specialtext = (TextView) findViewById(R.id.txtTitle);
            specialtext.setText(R.string.Order_Typetit);

            txthder = (TextView) findViewById(R.id.textViewhd);
//            txttm = (TextView) findViewById(R.id.textViewtm);
            txtct = (TextView) findViewById(R.id.textViewct);
            txthder.setVisibility(View.GONE);
            txtct.setVisibility(View.GONE);
            locview = (RelativeLayout) findViewById(R.id.listView3);
            custloctxt = (EditText) findViewById(R.id.custloctxt);
            locview.setVisibility(View.GONE);
            btnloc = (Button) findViewById(R.id.btnloc);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            if(campaignId!=null) {
                CampaignmEntity campaignModel = campaignModule.getCampaign(campaignId);
                if (campaignModel != null)
                    campaignData = campaignModel.getCampaign();
            }
            mechantSites = merchantsiteModule.getcampaignSiteList(campaignData.getCampaignId());

            final ArrayList<String> orderty = new ArrayList<String>();
            orderty.add(getString(R.string.Redeem));

            if (campaignData.getBooking().equals("A")) {
                orderty.add(getString(R.string.Booking));
            }

            if (campaignData.getOutcall().equals("A")) {
                orderty.add(getString(R.string.OutCall));
            }


            //Gps location
            Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                double latitude = gpsLocation.getLatitude();
                double longitude = gpsLocation.getLongitude();
                //Add initialization:
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    if(latitude>0 && longitude>0)
                    currenctAddress = geocoder.getFromLocation(latitude,
                            longitude, 10);

                    Log.d("LocationSampleActivity", currenctAddress.get(1).getAddressLine(0)+" "+currenctAddress.get(1).getLocality());
                    custloctxt.setText(currenctAddress.get(1).getAddressLine(0)+","+currenctAddress.get(1).getAddressLine(1)+","+currenctAddress.get(1).getAddressLine(2));
                } catch (IOException e1) {
                    Log.e("LocationSampleActivity","IO Exception in getFromLocation()");
                    e1.printStackTrace();

                } catch (IllegalArgumentException e2) {
                    // Error message to post in the log
                    String errorString = "Illegal arguments " +
                            Double.toString(latitude) +
                            " , " +
                            Double.toString(longitude) +
                            " passed to address service";
                    Log.e("LocationSampleActivity", errorString);
                    e2.printStackTrace();

                }
            }

            btnloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Gps location
                    Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
                    if (gpsLocation != null) {
                        double latitude = gpsLocation.getLatitude();
                        double longitude = gpsLocation.getLongitude();
                        try {
                            currenctAddress = geocoder.getFromLocation(latitude,
                                    longitude, 10);

                            custloctxt.setText(currenctAddress.get(1).getAddressLine(0)+","+currenctAddress.get(1).getAddressLine(1)+","+currenctAddress.get(1).getAddressLine(2));
                        } catch (IOException e1) {
                            Log.e("LocationSampleActivity","IO Exception in getFromLocation()");
                            e1.printStackTrace();

                        } catch (IllegalArgumentException e2) {
                            // Error message to post in the log
                            String errorString = "Illegal arguments " +
                                    Double.toString(latitude) +
                                    " , " +
                                    Double.toString(longitude) +
                                    " passed to address service";
                            Log.e("LocationSampleActivity", errorString);
                            e2.printStackTrace();

                        }
                    }
                }
            });


            listordty = (ListView) findViewById(R.id.listView1);
            listordty.setSelector(R.drawable.listselector);
            listordty.setAdapter(new OrdertyBaseAdapter(this, orderty));
            ListHelper.getListViewSize(listordty);
            listordty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;

                    ImageView chkmk;

                    for (int j = 0; j < parent.getChildCount(); j++) {
                        chkmk = (ImageView) parent.getChildAt(j).findViewById(R.id.imageView1);
                        chkmk.setVisibility(View.GONE);
                    }

                    chkmk = (ImageView) parent.getChildAt(position).findViewById(R.id.imageView1);
                    chkmk.setVisibility(View.VISIBLE);

                    redeem = orderty.get(position);

                    siteId = null;
                    compysiteId = 0;

                    txthder.setVisibility(View.GONE);
                    txtct.setVisibility(View.GONE);

                    listMsite.setAdapter(null);
                    ListHelper.getListViewSize(listMsite);


                    locview.setVisibility(View.GONE);
                    listMsite.setVisibility(View.GONE);


                    if (redeem.equals(getString(R.string.Booking))) {
                        txthder.setText(R.string.Pickup_From);
                        txthder.setVisibility(View.VISIBLE);
                        listMsite.setVisibility(View.VISIBLE);
                        listMsite.setAdapter(new merchanBaseAdapter(getApplicationContext(), mechantSites));
                        ListHelper.getListViewSize(listMsite);
                        Ordertype="Booking";

                    }

                    if (redeem.equals(getString(R.string.OutCall))) {
                        txtct.setVisibility(View.VISIBLE);
                        locview.setVisibility(View.VISIBLE);
                        Ordertype="OutCall";
                    }

                }
            });

//
//            listMsite = (ListView) findViewById(R.id.listView2);
//            listMsite.setSelector(R.drawable.listselector);
//            listMsite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    position = (int) id;
//                    siteData = mechantSites.get(position);
//
//                    siteId = siteData.getSiteId();
//
//
//
//                    ImageView chkmk;
//
//                    for (int j = 0; j < parent.getChildCount(); j++) {
//                        chkmk = (ImageView) parent.getChildAt(j).findViewById(R.id.imageView1);
//                        chkmk.setVisibility(View.GONE);
//                    }
//
//                    chkmk = (ImageView) parent.getChildAt(position).findViewById(R.id.imageView1);
//                    chkmk.setVisibility(View.VISIBLE);
//
//                    if (redeem.equals(getString(R.string.Booking))) {
//                        if (mechantSites.size() != 0) {
//
//                            listMsite.setAdapter(new merchanBaseAdapter(getApplicationContext(), mechantSites));
//                        }
//                    }
//
//                }
//            });


            listMsite = (ListView) findViewById(R.id.listView2);
            listMsite.setSelector(R.drawable.listselector);
            listMsite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;

                    siteData = mechantSites.get(position);

                    siteId = siteData.getSiteId();
                    ImageView chkmk;

                    for (int j = 0; j < parent.getChildCount(); j++) {
                        chkmk = (ImageView) parent.getChildAt(j).findViewById(R.id.imageView1);
                        chkmk.setVisibility(View.GONE);
                    }

                    chkmk = (ImageView) parent.getChildAt(position).findViewById(R.id.imageView1);
                    chkmk.setVisibility(View.VISIBLE);

                }
            });


            TextView purchtext = (TextView) findViewById(R.id.purch);
            purchtext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent=null;
                    CommonUtils common = new CommonUtils();
                    if (redeem != null) {

                        if (redeem.equals(getString(R.string.Redeem))) {

                            intent = new Intent(context, QrCodewalnetActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);


                        } else {
                            if (redeem.equals(getString(R.string.Booking))) {
                                if (siteId != null ) {
                                    intent = new Intent(context, PreviewActivity.class);
                                    OrderEntity orderRes = new OrderEntity();
                                    orderRes.setCouponId(couponId);
                                    orderRes.setPickupDeliveryType("P");
                                    orderRes.setSiteId(siteId);
                                    orderRes.setCustomerId(staffId);
                                    orderRes.setOrderDate(new Date().toString());
                                    OrdermEntity orderModel=new OrdermEntity();
                                    orderModel.setOrder(orderRes);
                                    try {
                                        couponOrderModule.addOrderlist(orderModel);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    common.Snackbar(v, "MZSite Id Null");
                                }
                            } else if (!custloctxt.getText().toString().equals("") ) {

                                intent = new Intent(context, PreviewActivity.class);
                                OrderEntity orderData = new OrderEntity();
                                orderData.setCouponId(couponId);
                                orderData.setPickupDeliveryType("D");
                                orderData.setSiteId(null);
                                orderData.setCustomerId(staffId);
                                orderData.setOrderDate(new Date().toString());

                                orderData.setCustomerAddress(custloctxt.getText().toString());
                                OrdermEntity orderModel=new OrdermEntity();
                                orderModel.setOrder(orderData);
                                try {
                                    couponOrderModule.addOrderlist(orderModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("Address", custloctxt.getText().toString());

                            } else {
                                common.Snackbar(v,getString(R.string.Choose_Delivery_From_and_Time));
                            }
                        }

                        Log.d("cupid-----pickup",couponId +"  "+campaignId);
                        intent.putExtra("siteid", siteId);
                        if(siteId!=null)
                            intent.putExtra("siteName", siteData.getSiteName());
                        intent.putExtra("frm", "serv_pick");
                        intent.putExtra("Ordertype", Ordertype);
                        if (custloctxt.getText() != null) {
                            Log.d("cupid-----pickup", couponId + "  " + campaignId);
                            intent.putExtra("Address", custloctxt.getText().toString());
                        }
                        else
                            intent.putExtra("Address", siteData.getSiteAddress());

                        startActivity(intent);
                        overridePendingTransition(0, 0);



                    } else {
                        common.Snackbar(v, getString(R.string.Choose_One_of_order_type));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class merchanBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<SiteEntity> albumList;
        int pos = 0;


        public merchanBaseAdapter(Context _MyContext, List<SiteEntity> _albumList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.pickup_address, null);
            } else {
                MyView = convertView;
            }

            TextView sitetext = (TextView) MyView.findViewById(R.id.textView1);
            TextView addrestext = (TextView) MyView.findViewById(R.id.textView2);

            sitetext.setText(albumList.get(position).getSiteName());
            addrestext.setText(albumList.get(position).getSiteAddress());

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




    public class OrdertyBaseAdapter extends BaseAdapter {
        Context MyContext;
        ArrayList<String> albumList;
        int pos = 0;


        public OrdertyBaseAdapter(Context _MyContext, ArrayList<String> _albumList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.ordertype_detail, null);
            } else {
                MyView = convertView;
            }

            TextView sitetext = (TextView) MyView.findViewById(R.id.textView1);

            sitetext.setText(albumList.get(position));


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


    @Override
    protected void onStop() {
        super.onStop();
        appLocationService.onStop();
//        stopService(new Intent("server.track.START_TRACK_SERVICE"));
    }
}
