package com.mezzofy.mzcoupon.Activity;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.mezzofy.mzcoupon.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(SplashScreen.this);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor =  settings.edit();

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

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginCheckAction.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onStart() {
        super.onStart();

        Branch branch = Branch.getInstance(getApplicationContext());

        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error != null) {
                    Log.i("BranchTestBed", "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.i("BranchTestBed", "branch init complete!");
                    if (branchUniversalObject != null) {
                        Log.i("BranchTestBed", "title " + branchUniversalObject.getTitle());
                        Log.i("BranchTestBed", "CanonicalIdentifier " + branchUniversalObject.getCanonicalIdentifier());
                        Log.i("ContentMetaData", "metadata " + branchUniversalObject.getMetadata());

                        Intent result = new Intent(Intent.ACTION_VIEW, Uri.parse(branchUniversalObject.getCanonicalIdentifier()));
                        startActivity(result);
                        finish();
                    }

                    if (linkProperties != null) {
                        Log.i("BranchTestBed", "Channel " + linkProperties.getChannel());
                        Log.i("BranchTestBed", "control params " + linkProperties.getControlParams());
                    }
                }
            }
        }, this.getIntent().getData(), this);
    }

}