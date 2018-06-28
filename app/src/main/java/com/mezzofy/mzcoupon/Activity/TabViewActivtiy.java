package com.mezzofy.mzcoupon.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;


import java.util.Locale;

public class TabViewActivtiy extends FragmentActivity implements View.OnClickListener  {

    ImageView dealimage, couponimage, shakeimage,cartimage, settingimage,walletimage;
    TextView dealtext, coupontext, shaketext,carttext, settingtext,wallettext;
    RelativeLayout tabone, tabtwo, tabthree, tabfour,tabfive,Tabsix;

    int notif_count = 0;
    TextView notifi;

    public static TabMainActivity tabs;
    private Cart_Module cartModule;
    int cutab = 0;
    int specialId;
    int listprod;
    String specialImg, specialname, coupstats,Currency;
    SharedPreferences settings;
    public AlertDialog Dialog;
    String ptype="P";
    private Setting_Module settingModule;
    int currTab = 0;
    String tabName = "",userType;
    int cout = 0;
    double totalamount=0.0;

    Merchant_Module merchantModule;
    MerchantEntity merchantData;


    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tab);

        notif_count = 0;


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notif_count", cout);
        editor.putString("Price_plan",null);
        editor.commit();

        Currency=settings.getString("currency",null);

        String languageToLoad  = settings.getString("language", "en");
        Locale locale = null;
        if(languageToLoad.equals("en")) {
            locale = new Locale(Locale.ENGLISH.getLanguage());
        }else  if(languageToLoad.equals("zh-rCN")) {
            locale = new Locale(Locale.SIMPLIFIED_CHINESE.getLanguage(),Locale.SIMPLIFIED_CHINESE.getCountry());
        }else  if(languageToLoad.equals("zh-rTW")) {
            locale = new Locale(Locale.TRADITIONAL_CHINESE.getLanguage(),Locale.TRADITIONAL_CHINESE.getCountry());
        }
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        notif_count = settings.getInt("notif_count", 0);

        initilaiseView();

        Bundle extras = getIntent().getExtras();

        cartModule=new Cart_Module(TabViewActivtiy.this);
        settingModule=new Setting_Module(TabViewActivtiy.this);
//        merchantModule=new Merchant_Module(TabViewActivtiy.this);

//        try {
//            merchantData=merchantModule.getMerchantList();
//            Currency=merchantData.getCurrency();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            cout = cartModule.getItemcart();
            totalamount=cartModule.getTotalcart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        notif_count = 0;


        if (extras != null) {
            currTab = extras.getInt("currTab");
            tabName = extras.getString("tabName");
            specialId = extras.getInt("specialId");
            specialImg = extras.getString("specialimg");
            specialname = extras.getString("specialname");
            coupstats = extras.getString("coupstats");
            listprod = extras.getInt("listprod");
            ptype = extras.getString("ptype","P");
        }



        if(extras!=null) {
            String flag = extras.getString("flag");
//            if (flag != null && flag.equals("wallet")) {
//                onClick(Tabsix);
//            }
            if (flag != null && flag.equals("setting")) {
                onClick(tabfive);
            } else if (flag != null && flag.equals("cart")) {
                onClick(tabfour);
            } else if (flag != null && flag.equals("shake")) {
                onClick(tabthree);
            }else if (flag != null && flag.equals("coupon")) {
                onClick(tabtwo);
            }else{
                onClick(tabone);
            }
        }
        else {
            onClick(tabone);
        }

        updatenotification();
    }

    private void initilaiseView() {

        tabone = (RelativeLayout) findViewById(R.id.tab_one);
        tabone.setOnClickListener(this);

        tabtwo = (RelativeLayout) findViewById(R.id.tab_two);
        tabtwo.setOnClickListener(this);

//        tabthree = (RelativeLayout) findViewById(R.id.tab_three);
//        tabthree.setOnClickListener(this);

        tabfour = (RelativeLayout) findViewById(R.id.tab_four);
        tabfour.setOnClickListener(this);

        tabfive = (RelativeLayout) findViewById(R.id.tab_five);
        tabfive.setOnClickListener(this);

//        Tabsix= (RelativeLayout) findViewById(R.id.tab_six);
//        Tabsix.setOnClickListener(this);
//        wallettext = (TextView) findViewById(R.id.wallet_text);
//        wallettext.setText(getString(R.string.menu_Wallet));

        dealimage = (ImageView) findViewById(R.id.deal_image);
        couponimage = (ImageView) findViewById(R.id.coupon_image);
//        shakeimage = (ImageView) findViewById(R.id.shake_image);
        cartimage = (ImageView) findViewById(R.id.cart_image);
        settingimage = (ImageView) findViewById(R.id.setting_image);

        dealtext = (TextView) findViewById(R.id.deal_text);
        dealtext.setText(getString(R.string.menu_Deals));

        coupontext = (TextView) findViewById(R.id.coupon_text);
        coupontext.setText(getString(R.string.menu_Products));

//        shaketext = (TextView) findViewById(R.id.shake_text);
//        shaketext.setText(getString(R.string.menu_shake));

        carttext = (TextView) findViewById(R.id.cart_text);
        carttext.setText(getString(R.string.Cart));

        settingtext = (TextView) findViewById(R.id.setting_text);
        settingtext.setText(getString(R.string.menu_settings));



        notifi = (TextView) findViewById(R.id.badgetxt);
    }

    @Override
    public void onClick(View v) {
        Intent it1;
        switch (v.getId()) {
            case R.id.tab_one:
                unSelectImage();
                dealimage.setImageResource(R.drawable.deals_w);
                changeTextcolor();
                dealtext.setTextColor(getResources().getColor(R.color.white));
                loadFragment(new CampaignMainList_Activity());
                break;
            case R.id.tab_two:
                unSelectImage();
                couponimage.setImageResource(R.drawable.coupons);
                changeTextcolor();
                coupontext.setTextColor(getResources().getColor(R.color.white));
                loadFragment(new CardsActivity());
                break;
//            case R.id.tab_three:
//                unSelectImage();
//                shakeimage.setImageResource(R.drawable.drag);
//                changeTextcolor();
//                shaketext.setTextColor(getResources().getColor(R.color.white));
//                loadFragment(new DragActivity());
//                break;
            case R.id.tab_four:

                unSelectImage();
                cartimage.setImageResource(R.drawable.cart);
                changeTextcolor();
                carttext.setTextColor(getResources().getColor(R.color.white));
                carttext.setText(Currency +" "+ String.format("%,.0f", totalamount));
                loadFragment(new CartActivity());
               break;
            case R.id.tab_five:
                unSelectImage();
                settingimage.setImageResource(R.drawable.settings);
                changeTextcolor();
                settingtext.setTextColor(getResources().getColor(R.color.white));
                loadFragment(new SettingActivity());
                break;

//            case R.id.tab_six:
//                unSelectImage();
//                changeTextcolor();
//                wallettext.setTextColor(getResources().getColor(R.color.white));
//                loadFragment(new Wallet_Activity());
//                break;
            default:
                break;
        }
    }

    private void unSelectImage() {
        dealimage.setImageResource(R.drawable.deals_m);
        couponimage.setImageResource(R.drawable.coupons_m);
//        shakeimage.setImageResource(R.drawable.drag_m);
        cartimage.setImageResource(R.drawable.cart_m);
        settingimage.setImageResource(R.drawable.settings_m);
    }


    private void changeTextcolor() {
        dealtext.setTextColor(ContextCompat.getColor(TabViewActivtiy.this, R.color.tabtext_color));
        coupontext.setTextColor(ContextCompat.getColor(TabViewActivtiy.this, R.color.tabtext_color));
//        shaketext.setTextColor(ContextCompat.getColor(TabViewActivtiy.this, R.color.tabtext_color));
        carttext.setTextColor(ContextCompat.getColor(TabViewActivtiy.this, R.color.tabtext_color));
        settingtext.setTextColor(ContextCompat.getColor(TabViewActivtiy.this, R.color.tabtext_color));
    }

    public void updatenotification() {
        if (notif_count != 0) {
            notifi.setVisibility(View.VISIBLE);
            notifi.setText(String.valueOf(notif_count));
            notif_count = 0;
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public void exit(){
        TabViewActivtiy.this.finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        CommonUtils.Snackbar(dealtext, getString(R.string.click_BACK_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}