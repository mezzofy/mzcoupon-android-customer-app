package com.mezzofy.mzcoupon.Activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;

public class Coupon_SiteActivity extends Activity {

    RelativeLayout layout;

    ListView list;

    final Context context = this;

    private Merchantsite_Module merchantsiteModule;

    List<SiteEntity> siteProducts = null;

    String campaignId;

    String exp, expday, couponId;
    SharedPreferences settings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.outlets);

            merchantsiteModule = new Merchantsite_Module(context);

            Bundle extras = getIntent().getExtras();
            expday = extras.getString("expday");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

//            siteProducts = merchantsiteModule.getcampaignSiteList(campaignId);


            list = (ListView) findViewById(R.id.listView1);
            list.setSelector(R.drawable.listselector);
//            list.setAdapter(new ImageBaseAdapter(this, siteProducts));
            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;
                    SiteEntity album = siteProducts.get(position);

                    Intent intent = new Intent(context, Coupon_siteLocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("campaignId", campaignId);
                    intent.putExtra("exp", exp);
                    intent.putExtra("couponId", couponId);
//                    intent.putExtra("Latitude", album.getLatitude());
//                    intent.putExtra("Longitude", album.getLongitude());
                    intent.putExtra("sitename", album.getSiteName());
                    intent.putExtra("address", album.getSiteAddress());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Coupon_SiteActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        expday = extras.getString("expday");

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        campaignId = settings.getString("campaignId", null);
        couponId = settings.getString("couponId", null);
        exp = settings.getString("exp", null);

        Log.d("campaignId-----",campaignId);

        try {
            siteProducts = merchantsiteModule.getcampaignSiteList(campaignId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.setAdapter(new ImageBaseAdapter(this, siteProducts));
    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<SiteEntity> albumList;
        int pos = 0;


        public ImageBaseAdapter(Context _MyContext, List<SiteEntity> _albumList) {
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
                MyView = li.inflate(R.layout.outletslist, null);
            } else {
                MyView = convertView;
            }

            TextView sitetext = (TextView) MyView.findViewById(R.id.textView1);
            TextView addrestext = (TextView) MyView.findViewById(R.id.textView2);
            TextView contacttext = (TextView) MyView.findViewById(R.id.textView3);
            LinearLayout linearLayout = (LinearLayout) MyView.findViewById(R.id.View3);

            sitetext.setText(albumList.get(position).getSiteName());
            addrestext.setText(albumList.get(position).getSiteAddress());
            if (albumList.get(position).getSiteContact() != null && !albumList.get(position).getSiteContact().equals("")) {
                contacttext.setText(albumList.get(position).getSiteContact());
            } else {
                linearLayout.setVisibility(View.INVISIBLE);
            }

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(getString(R.string.Call) + "\n" + albumList.get(position).getSiteContact() + "");
            builder1.setCancelable(true);
            builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + albumList.get(position).getSiteContact()));
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

            final AlertDialog alert11 = builder1.create();

            contacttext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    alert11.show();
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


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent;
        intent = new Intent(context, CouponDetail_Activity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        Coupon_SiteActivity.this.finish();
    }
}