package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Adapter.ImagePagerAdapter;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;

public class Cart_ProdDetailActivity extends Activity implements OnClickListener {

    RelativeLayout layout;
    final Context context = this;

    private Campaign_Module campaignModule;
    private MerchantEntity merchantData;
    private Merchant_Module merchantModule;
    private Cart_Module cartModule;

    String prodid, staffId;

    String cartid;

    CartEntity cartData;
    CampaignmEntity campaignModel;

    EditText qtyText;
    TextView titletext;

    CommonUtils common;
    SharedPreferences settings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.cartdetail);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);
            campaignModule = new Campaign_Module(context);
            merchantModule=new Merchant_Module(context);
            cartModule = new Cart_Module(context);

            Bundle extras = getIntent().getExtras();
            prodid = extras.getString("prodid");
            cartid = extras.getString("cartid");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            campaignModel = campaignModule.getCampaign(prodid);
            merchantData = merchantModule.getMerchantList();

            cartData = cartModule.getCartData(prodid);

            titletext = (TextView) findViewById(R.id.txtTitle);
            titletext.setText(cartData.getCampaignName());

            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            ImagePagerAdapter adapter = new ImagePagerAdapter(getApplicationContext(), campaignModel.getCampaign());
            viewPager.setAdapter(adapter);

            TextView redpricetext = (TextView) findViewById(R.id.textView2);
            TextView selpricetext = (TextView) findViewById(R.id.textView3);
            TextView proddesctext = (TextView) findViewById(R.id.textView4);
            TextView termtext = (TextView) findViewById(R.id.lntextView5);
            TextView prodtext = (TextView) findViewById(R.id.textView6);

            CampaignEntity campaignData=campaignModel.getCampaign();
            Typeface regular = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
            prodtext.setText(campaignData.getCampaignName());
            proddesctext.setText(campaignData.getCampaignDesc());
            proddesctext.setMovementMethod(new ScrollingMovementMethod());
            proddesctext.setTypeface(regular, Typeface.NORMAL);

            DecimalFormat df = new DecimalFormat("#.##");

            if (settings.getString("decimal", "N").equals("Y")) {


                if (campaignData.getOrginalPrice() != 0.00) {


                    String formatted = df.format(campaignData.getOrginalPrice());
//                    redpricetext.setText(getString(R.string.Original_Price) + " " + settings.getString("currency", "IDR") + String.format("%,.2f", campaignData.getOrginalPrice()));
                    redpricetext.setText(getString(R.string.Original_Price) + " " + settings.getString("currency", "IDR") + formatted);
                    redpricetext.setPaintFlags(redpricetext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                selpricetext.setText(getString(R.string.Now) + " " + settings.getString("currency", "IDR") + String.format("%,.2f", campaignData.getSellingPrice()));
            } else {
                if (campaignData.getOrginalPrice() != 0.00) {
                    String formatted = df.format(campaignData.getOrginalPrice());
//                    redpricetext.setText(getString(R.string.Original_Price) + " " + settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                    redpricetext.setText(getString(R.string.Original_Price) + " " + settings.getString("currency", "IDR") + formatted);
                    redpricetext.setPaintFlags(redpricetext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                selpricetext.setText(getString(R.string.Now) + " " + settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getSellingPrice()));

            }
            termtext.setText(campaignData.getCampaignTc());

            RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.reviewsz);
            RelativeLayout relativeLayout4 = (RelativeLayout) findViewById(R.id.locationz);
            RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.callz);
            RelativeLayout relativeLayout6 = (RelativeLayout) findViewById(R.id.sharez);
            relativeLayout3.setOnClickListener(this);
            relativeLayout4.setOnClickListener(this);
            relativeLayout5.setOnClickListener(this);
            relativeLayout6.setOnClickListener(this);

//            ArrayList<GroupRes> grplist = productDao.getProductGroupList(campaignData.getProdId());
//            if (grplist != null) {
////			  linearLayout1.setVisibility(View.VISIBLE);
//            }

            qtyText = (EditText) findViewById(R.id.editText1);
            qtyText.setEnabled(false);
            qtyText.setText(String.valueOf(cartData.getProductQty()));

            Button addbtn = (Button) findViewById(R.id.button2);
            Button subbtn = (Button) findViewById(R.id.button1);

            addbtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int qty = Integer.valueOf(qtyText.getText().toString());
                    qtyText.setText(String.valueOf(++qty));

                }
            });

            subbtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int qty = Integer.valueOf(qtyText.getText().toString());
                    if (qty > 1)
                        qtyText.setText(String.valueOf(--qty));
                }
            });


            TextView updatetext = (TextView) findViewById(R.id.TextView01);
            updatetext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    double tot = Double.valueOf(qtyText.getText().toString()) * campaignModel.getCampaign().getSellingPrice();

                    try {
                        cartModule.updateQty(prodid, qtyText.getText().toString(), tot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (settings.getString("decimal", "N").equals("Y")) {

                        try {
                            titletext.setText(settings.getString("currency", "IDR") + String.format("%,.2f", cartModule.getTotalcart()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            titletext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", cartModule.getTotalcart()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    common.Snackbar(layout,getString(R.string.Updated));
                }
            });


            relativeLayout4.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, Cart_siteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("prodid", prodid);
                    intent.putExtra("cartid", cartid);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Cart_ProdDetailActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.reviewsz:
                intent = new Intent(context, Cart_termConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("prodid", prodid);
                intent.putExtra("cartid", cartid);
                intent.putExtra("txttitle", getString(R.string.Reviews));
                intent.putExtra("prodterm", campaignModel.getCampaign().getReviewUrl());
                startActivity(intent);
                overridePendingTransition(0, 0);
                Cart_ProdDetailActivity.this.finish();
                break;
            case R.id.locationz:
                break;
            case R.id.callz:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(getString(R.string.Call) + "\n" + merchantData.getMerchantName() + "");
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + merchantData.getMerchantHotline()));
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

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
            case R.id.sharez:
                share();
                break;
        }
    }

    public void share() {
        File file = null;

        try {
            URL url = new URL(campaignModel.getCampaign().getCampaignimages().get(0).getCampaignimage().getCampaignImage());
            InputStream content = (InputStream) url.getContent();
            Bitmap bm = BitmapFactory.decodeStream(content);
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

            file = new File(extStorageDirectory, "mypic.png");

            FileOutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        String shareBody;
        if (settings.getString("decimal", "N").equals("Y")) {
            shareBody = campaignModel.getCampaign().getCampaignName() + "\n" + settings.getString("currency", "IDR") + String.format("%,.2f", campaignModel.getCampaign().getSellingPrice()) + "\nhttp://www.balicoupon.id";
        } else {
            shareBody = campaignModel.getCampaign().getCampaignName() + "\n" + settings.getString("currency", "IDR") + String.format("%,.0f", campaignModel.getCampaign().getSellingPrice()) + "\nhttp://www.balicoupon.id";
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Balicoupon");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

//				  sharingIntent.setType("*/*");
//				  sharingIntent.setType("application/octet-stream");
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(context, TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("flag", "cart");

        startActivity(intent);
        overridePendingTransition(0, 0);
        Cart_ProdDetailActivity.this.finish();
    }

}
