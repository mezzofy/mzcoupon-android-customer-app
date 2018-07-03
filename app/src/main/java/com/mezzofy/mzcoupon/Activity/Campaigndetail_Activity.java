package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Adapter.SpecialDetailAdapter;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailEntity;
import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailmEntity;
import com.mezzofy.mzcoupon.Entity.PomEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Payment_Module;
import com.mezzofy.mzcoupon.pojo.Merchant;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class Campaigndetail_Activity extends Activity implements OnClickListener {


    final Context context = this;

    Merchant merchant;
    private CampaignmEntity productRes;
    SharedPreferences settings;

    private Customer_Module userModule;
    private Campaign_Module campaignModule;
    private Cart_Module cartModule;

    TextView couponvalue;
    TextView purchtext;
    TextView coupon_dtl, coupon_dtltemp;
    TextView more, less;
    TextView prodtext;

    TextView termtexttemp, termtext;

    int listprod;
    String prodid;
    String staffId;
    int i;

    String prodfav;

    Float prod_val;

    LinearLayout image_layout, View1, Viewtemp;

    RelativeLayout relative_main, qty_layout;

    public ProgressDialog pDialog;

    String flag;

    CustomerEntity staffRes;
    Payment_Module paymentModule;


    RelativeLayout relativeLayout3, relativeLayout4, relativeLayout5, relativeLayout6;
    CommonUtils common;

    double latitude;
    double longitude;
        String ptype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.special_prod_detail);

        try {
            campaignModule = new Campaign_Module(context);
            cartModule = new Cart_Module(context);
            userModule = new Customer_Module(context);
            paymentModule=new Payment_Module(context);

            common = new CommonUtils();

            Bundle extras = getIntent().getExtras();
            prodid = extras.getString("prodid");
            prodfav = extras.getString("prodfav");
            listprod = extras.getInt("listprod");
            flag = extras.getString("flag");
            ptype=extras.getString("ptype");

            staffRes = userModule.getUser();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            productRes = campaignModule.getCampaign(prodid);

            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

            SpecialDetailAdapter adapter = new SpecialDetailAdapter(getApplicationContext(), productRes, prodid);
            viewPager.setAdapter(adapter);

            ImageView playbtn = (ImageView) findViewById(R.id.playtbn);

            TextView redpricetext = (TextView) findViewById(R.id.textView2);
            TextView selpricetext = (TextView) findViewById(R.id.textView3);
            prodtext = (TextView) findViewById(R.id.textView6);

            coupon_dtl = (TextView) findViewById(R.id.coupon_dtl);
            Viewtemp = (LinearLayout) findViewById(R.id.Viewtemp);
            coupon_dtltemp = (TextView) findViewById(R.id.coupon_dtltemp);
            termtext = (TextView) findViewById(R.id.lntextView5);
            termtexttemp = (TextView) findViewById(R.id.lntextViewtemp);
            purchtext = (TextView) findViewById(R.id.addcart);
            //TextView paid=(TextView)findViewById(R.id.textView10);
            TextView sold = (TextView) findViewById(R.id.textView12);

            relativeLayout3 = (RelativeLayout) findViewById(R.id.reviewsz);
            relativeLayout4 = (RelativeLayout) findViewById(R.id.locationz);
            relativeLayout5 = (RelativeLayout) findViewById(R.id.callz);
            relativeLayout6 = (RelativeLayout) findViewById(R.id.sharez);

            relativeLayout3.setOnClickListener(this);
            relativeLayout4.setOnClickListener(this);
            relativeLayout5.setOnClickListener(this);
            relativeLayout6.setOnClickListener(this);

            final CampaignEntity campaignData=productRes.getCampaign();

            if (campaignData.getDailyLimitType()!=null && campaignData.getDailyLimitType().equals("A")) {

                if (campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")) {

                    sold.setText(getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() + "\n" + getResources().getString(R.string.left) + " " + campaignData.getDailyLimit() + "\n" + getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + getResources().getString(R.string.Days));
                } else {
                    sold.setText(getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() + "\n" + getResources().getString(R.string.left) + " " + campaignData.getDailyLimit() + "\n" + getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + getResources().getString(R.string.Hours));
                }
            }else {
                if (campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")) {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                    symbols.setInfinity("∞");
                    DecimalFormat decimalFormat = new DecimalFormat("#.#####", symbols);

                    sold.setText(getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() + "\n" + getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + getResources().getString(R.string.Days));
                } else {
                    sold.setText(getResources().getString(R.string.sold) + " " + campaignData.getRedeemcoupon() + "\n" + getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + getResources().getString(R.string.Hours));
                }
            }

                LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.lineView2);

            linearLayout1.setVisibility(View.GONE);

            linearLayout1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });


            TextView optios = (TextView) findViewById(R.id.lntextView6);
            optios.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MenuList_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("prodid", prodid);
                    intent.putExtra("form", "prod");
                    intent.putExtra("prodfav", prodfav);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Campaigndetail_Activity.this.finish();


                }
            });


            qty_layout = (RelativeLayout) findViewById(R.id.qty_layout);
            image_layout = (LinearLayout) findViewById(R.id.image_layout);

            View1 = (LinearLayout) findViewById(R.id.View1);

            relative_main = (RelativeLayout) findViewById(R.id.relative_main);

            more = (TextView) findViewById(R.id.more);
            more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Viewtemp.setVisibility(View.VISIBLE);
                    View1.setVisibility(View.GONE);
                }
            });

            less = (TextView) findViewById(R.id.moretemp);
            less.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Viewtemp.setVisibility(View.GONE);
                    View1.setVisibility(View.VISIBLE);
                }
            });

            couponvalue = (TextView) findViewById(R.id.qty);

            i = 1;


            TextView addcoupon = (TextView) findViewById(R.id.add);
            addcoupon.setOnClickListener(this);

            TextView cancelcoupon = (TextView) findViewById(R.id.cancel);
            cancelcoupon.setOnClickListener(this);

            Typeface regular = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            prodtext.setText(campaignData.getCampaignName());
            coupon_dtl.setText(campaignData.getCampaignDesc());
            coupon_dtl.setMovementMethod(new ScrollingMovementMethod());
            coupon_dtl.setTypeface(regular, Typeface.NORMAL);
            coupon_dtltemp.setText(campaignData.getCampaignDesc());
            coupon_dtltemp.setMovementMethod(new ScrollingMovementMethod());
            coupon_dtltemp.setTypeface(regular, Typeface.NORMAL);
            termtext.setText(campaignData.getCampaignTc());


            if (staffRes!=null && campaignData.getBrand()!=null && !campaignData.getBrand().equals("F")) {
                if (campaignData.getOrginalPrice() > 0)
                    redpricetext.setText(getString(R.string.Original_Price) + " " + "IDR" + campaignData.getOrginalPrice());
                else
                    redpricetext.setText("");

                if (campaignData.getSellingPrice() > 0)
                    selpricetext.setText(getString(R.string.Now) + " " +  "IDR" + campaignData.getSellingPrice());
                else
                    selpricetext.setText("");


            }



            if (campaignData.getBrand()!=null && campaignData.getBrand().equals("F")) {

                purchtext.setText(getString(R.string.Download_Coupon));
                qty_layout.setVisibility(View.GONE);

            } else {

                if (staffRes!=null && !staffRes.getCustomerStatus().equals("G") && staffRes.getUserType().equals("C")) {
                    prod_val = campaignData.getSellingPrice();
                    couponvalue.setText(getResources().getString(R.string.qty) + "  " + i);
                    purchtext.setText(getResources().getString(R.string.Add_Cart) + " " +  "IDR" + " " +  prod_val);
                }
                else
                    purchtext.setText(getResources().getString(R.string.Add_Cart));



            }


            purchtext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (staffRes!=null && !staffRes.getCustomerStatus().equals("G") && staffRes.getUserType().equals("C") && campaignData.getBrand()!=null && !campaignData.getBrand().equals("F")) {

                        int stockQty =campaignModule.checkproduct(campaignData.getCampaignId(), i);

                        if (stockQty!=0) {
                            CartEntity cartData = new CartEntity();
                            cartData.setMerchantId(staffRes.getMerchantId());
                            cartData.setCampaignId(productRes.getCampaign().getCampaignId());
                            cartData.setCampaignName(productRes.getCampaign().getCampaignName());
                            cartData.setCampaignImage(productRes.getCampaign().getCampaignimages().get(0).getCampaignimage().getCampaignImage());
                            cartData.setProductQty(String.valueOf(i));
                            cartData.setSellingPrice(String.valueOf(productRes.getCampaign().getSellingPrice()));
                            cartData.setOrginalPrice(String.valueOf(productRes.getCampaign().getOrginalPrice()));
                            cartData.setTotalPrice(prod_val);
                            cartData.setCampaignTc(productRes.getCampaign().getCampaignTc());
                            cartData.setCampaignCode(productRes.getCampaign().getCampaignCode());
                            cartData.setStatus("A");
                            try {
                                cartModule.addCart(cartData);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int cout = 0;
                            try {
                                cout = cartModule.getItemcart();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("notif_count", cout);
                            editor.commit();
                            common.Snackbar(qty_layout, getString(R.string.Purchased));

//                                Intent intent = null;
//                                if (prodfav == null) {
//                                    intent = new Intent(context, TabMainActivity.class);
//                                    intent.putExtra("tabName", getString(R.string.menu_Products));
//                                    intent.putExtra("currTab", 3);
//                                } else {
//                                    intent = new Intent(context, FavouriteActivity.class);
//                                }
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("listprod", listprod);
//                                intent.putExtra("prodfav", prodfav);
//                                startActivity(intent);
//                                overridePendingTransition(0, 0);
//                                Campaigndetail_Activity.this.finish();
                        } else {
                            common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                        }
                    } else if (campaignData.getBrand()!=null && campaignData.getBrand().equals("F")) {

                        if (staffRes!=null && staffRes.getUserType().equals("C") && !staffRes.getCustomerStatus().equals("G")) {
                            int stockQty = campaignModule.checkproduct(campaignData.getCampaignId(), 1);

                            if (stockQty != 0) {
                                AsyncCallWS asyncCallWS = new AsyncCallWS();
                                asyncCallWS.execute();
                            } else
                                common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                        } else {
                            signInAlert();
                        }
                    }
                    else {
                        signInAlert();
                    }
                }
            });

            final ImageView favImage = (ImageView) findViewById(R.id.imageView1);
            favImage.setImageResource(R.drawable.fav);

            if(campaignData.getFavourite()!=null && campaignData.getFavourite().equals("Y"))
                favImage.setImageResource(R.drawable.fav_white);
            else
                favImage.setImageResource(R.drawable.fav);

            if (productRes.getCampaign().getFavourite() != null)
                if (productRes.getCampaign().getFavourite().equals("Y")) {
                    //toggle.setChecked(true);
                    favImage.setImageResource(R.drawable.fav_white);
                } else {
                    favImage.setImageResource(R.drawable.fav);
                }


            favImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (productRes.getCampaign().getFavourite().equals("N")) {
                        //change to 0 here
                        favImage.setImageResource(R.drawable.fav_white);
                        try {
                            campaignModule.updateCampaignFavourite(productRes.getCampaign().getCampaignId(), "Y");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        productRes.getCampaign().setFavourite("Y");
                    } else {
                        favImage.setImageResource(R.drawable.fav);
                        try {
                            campaignModule.updateCampaignFavourite(productRes.getCampaign().getCampaignId(), "N");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        productRes.getCampaign().setFavourite("N");
                    }

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
                intent = new Intent(context, TermConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("prodid", prodid);
                intent.putExtra("prodfav", prodfav);
                intent.putExtra("txttitle", getString(R.string.Reviews));
                intent.putExtra("flag", "product");
                intent.putExtra("prodterm", productRes.getCampaign().getReviewUrl());
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.locationz:
                intent = new Intent(context, SiteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("prodid", prodid);
                intent.putExtra("prodfav", prodfav);
                intent.putExtra("flag", "product");
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.callz:
                if(merchant!=null && merchant.getHotline()!=null)
                    callAlert();
                else
                    common.Snackbar(qty_layout, "No MZMerchant Hotline");
                break;
            case R.id.sharez:
                share();
                break;
            case R.id.add:
                addCoupon();
                break;
            case R.id.cancel:
                cancelCoupon();
                break;
            case R.id.more:
                cancelCoupon();
                break;
            case R.id.moretemp:
                cancelCoupon();
                break;
        }
    }




    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        PoEntity poData1;
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            try {
                if (pDialog == null) {
                    // in standard case YourActivity.this
                    pDialog = new ProgressDialog(Campaigndetail_Activity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");



            PomEntity poListModel = new PomEntity();
            String ref_No = UUID.randomUUID().toString();
            PoEntity poData = new PoEntity();
            poData.setCustomerId(staffRes.getCustomerId());
            poData.setPoNo(ref_No);
            poData.setPoStatus("A");
            poData.setPayReceipt("");
            poData.setPayResponse("0");
            poData.setCouponType("F");

            poListModel.setPo(poData);

            ArrayList pomodellist = new ArrayList<PoDetailmEntity>();
            PoDetailEntity poDetailData = new PoDetailEntity();
            poDetailData.setCampaignId(prodid);
            poDetailData.setCampaignQty("1");

            PoDetailmEntity poDetailModel = new PoDetailmEntity();
            poDetailModel.setPodetail(poDetailData);
            pomodellist.add(poDetailModel);

            poListModel.setPodetails(pomodellist);

            try {
                poData1 = paymentModule.DownloadFreeCouponFromServer(poListModel);
            } catch (APIServerException e) {
                Toast.makeText(getApplicationContext(),"Error--"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                    pDialog = null;
                }

                if(poData1!=null && poData1.getPoId()!=null)
                {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Campaigndetail_Activity.this);
                builder1.setTitle(getString(R.string.Coupon_Downloaded));
                builder1.setMessage(getString(R.string.Please_chk_Redeem));
                builder1.setCancelable(true);
                builder1.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = null;
                        if (prodfav == null) {
                            intent = new Intent(context, TabViewActivtiy.class);
                            intent.putExtra("tabName", getString(R.string.menu_Products));
                            intent.putExtra("currTab", 1);

                        } else {
                            intent = new Intent(context, FavouriteActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("listprod", listprod);
                        intent.putExtra("prodfav", prodfav);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Campaigndetail_Activity.this.finish();
                    }


                });

                AlertDialog alertlog = builder1.create();
                alertlog.show();
            }
            else
                    Toast.makeText(getApplicationContext(),"Error--"+ Applaunch.ErrorMessage, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public void callAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder1.setMessage(getString(R.string.Call) + "\n" + merchant.getRemark() + "");
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + merchant.getRemark()));
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
    }


    public void share() {
        File file = null;

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("http://www.mojo-domo.com/download")
                .setTitle(productRes.getCampaign().getCampaignName())
                .setContentDescription(settings.getString("currency", "IDR") + String.format("%,.2f", productRes.getCampaign().getSellingPrice()))
                .setContentImageUrl(productRes.getCampaign().getCampaignimages().get(0).getCampaignimage().getCampaignImage()).setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

        LinkProperties linkProperties = new LinkProperties().setFeature("sharing");
        branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    StringBuffer shareBody = new StringBuffer();
                    shareBody.append(url);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            }
        });
    }


    public void addCoupon() {

        i = (i + 1);
        int res =campaignModule.checkproduct(prodid, i);
        if (res > 0) {
            prod_val = prod_val + productRes.getCampaign().getSellingPrice();
            couponvalue.setText(String.valueOf(getResources().getString(R.string.qty) + "  " + i));
            purchtext.setText(getResources().getString(R.string.Add_Cart) + " " + settings.getString("currency", "IDR") + " " + String.format("%,.2f", prod_val));
        } else {
            if (res == 0) {
                common.Snackbar(qty_layout, getString(R.string.out_of_stock));
            } else {
                common.Snackbar(qty_layout, getString(R.string.out_of_stock));
            }
        }
    }


    public void cancelCoupon() {
        if (i <= 1) {
            if (i == 1) {
                int res =campaignModule.checkproduct(prodid, i);
                if (res >0 ) {
                    prod_val = productRes.getCampaign().getSellingPrice();
                    couponvalue.setText(String.valueOf(getResources().getString(R.string.qty) + "  " + i));
                    purchtext.setText(getResources().getString(R.string.Add_Cart) + " " + settings.getString("currency", "IDR") + " " + String.format("%,.2f", prod_val));
                } else {
                    if (res == 0) {
                        common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                    } else {
                        common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                    }
                }
            }
        } else {
            i = (i - 1);
            int res =campaignModule.checkproduct(prodid, i);
            if (res >0 ) {
                prod_val = prod_val - productRes.getCampaign().getSellingPrice();
                couponvalue.setText(String.valueOf(getResources().getString(R.string.qty) + "  " + i));
                purchtext.setText(getResources().getString(R.string.Add_Cart) + " " + settings.getString("currency", "IDR") + " " + String.format("%,.2f", prod_val));
            } else {
                if (res == 0) {
                    common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                } else {
                    common.Snackbar(qty_layout, getString(R.string.out_of_stock));
                }
            }
        }
    }

    public void showMessage(String code) {

        if (code.equals("P0101")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.P0101));
        } else if (code.equals("P0102")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.P0102));
        } else if (code.equals("P0103")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.invalid_code));
        } else if (code.equals("P0104")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.P0104));
        } else if (code.equals("P0105")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.P0105));
        } else if (code.equals("P0106")) {
            CommonUtils.Snackbar(qty_layout, getString(R.string.P0106));
        }
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = null;
        if (flag != null) {
            finish();
        } else if (prodfav == null) {
            intent = new Intent(context, TabViewActivtiy.class);
            intent.putExtra("tabName", getString(R.string.menu_Products));
            intent.putExtra("currTab", 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("listprod", listprod);
            intent.putExtra("prodfav", prodfav);
            intent.putExtra("ptype",ptype);
            startActivity(intent);
            overridePendingTransition(0, 0);
            Campaigndetail_Activity.this.finish();
        } else {
            intent = new Intent(context, FavouriteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("listprod", listprod);
            intent.putExtra("prodfav", prodfav);
            startActivity(intent);
            overridePendingTransition(0, 0);
            Campaigndetail_Activity.this.finish();

        }

    }

    @Override
    public void onLowMemory() {
        // use it only for older API version
        super.onLowMemory();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public void signInAlert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder1.setMessage(getString(R.string.LogIn_msg));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                MojodomoDB dbHelper = new MojodomoDB(getApplicationContext());
                dbHelper.clearTables();

                Intent intent = new Intent(Campaigndetail_Activity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                Campaigndetail_Activity.this.finish();
            }
        });
        builder1.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder1.show();
    }

}