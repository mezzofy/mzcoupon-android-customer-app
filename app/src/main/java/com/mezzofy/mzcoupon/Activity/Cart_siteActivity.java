package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.SiteProduct;


import java.util.ArrayList;
import java.util.List;

public class Cart_siteActivity extends Activity {

    ListView list;
    final Context context = this;
    Merchantsite_Module merchantsiteModule;

    ArrayList<SiteProduct> siteProducts = null;
    List<SiteEntity> mechantSites = null;

    Campaign_Module campaign_module;

    String prodid;
    String cartid;

    CommonUtils common;
    RelativeLayout layout;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.outlets);
            merchantsiteModule = new Merchantsite_Module(context);
            campaign_module = new Campaign_Module(context);


            Bundle extras = getIntent().getExtras();
            prodid = extras.getString("prodid");
            cartid = extras.getString("cartid");


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            mechantSites=merchantsiteModule.getcampaignSiteList(prodid);




            list = (ListView) findViewById(R.id.listView1);
            list.setSelector(R.drawable.listselector);
            list.setAdapter(new ImageBaseAdapter(this, mechantSites));
            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;
                    SiteEntity album = mechantSites.get(position);

                    Intent intent = new Intent(context, Cart_siteLocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("prodid", prodid);
//                    intent.putExtra("Latitude", album.getLatitude());
//                    intent.putExtra("Longitude", album.getLongitude());
                    intent.putExtra("sitename", album.getSiteName());
                    intent.putExtra("address", album.getSiteAddress());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Cart_siteActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        intent = new Intent(context, Cart_ProdDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("prodid", prodid);
        intent.putExtra("cartid", cartid);

        startActivity(intent);
        overridePendingTransition(0, 0);
        Cart_siteActivity.this.finish();
    }
}