package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.mezzofy.mzcoupon.Adapter.BannerImgsViewPagerAdapter;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.StoragePermission;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class AgreeActivity extends Activity implements OnClickListener {

    final Context context = this;
    private boolean doubleBackToExitPressedOnce = false;

    CommonUtils common;
    RelativeLayout layout;

    private ViewPager viewpagerBanner;
    private CirclePageIndicator pageIndicator;

    private BannerImgsViewPagerAdapter viewpagerAdapter;
    private Timer swipeTimer;
    private int NUM_PAGES;
    private int currentPage = 0;

    private int[] images = {
            R.drawable.tutorial_1,
            R.drawable.tutorial_2,
            R.drawable.shakebg,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            FacebookSdk.sdkInitialize(AgreeActivity.this);
            setContentView(R.layout.agree_page);

            StoragePermission.verifyStoragePermissions(AgreeActivity.this);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            TextView agreebtn = (TextView) findViewById(R.id.TextViewagree);
            agreebtn.setOnClickListener(this);

            viewpagerBanner = (ViewPager) findViewById(R.id.banner_view);
            pageIndicator = (CirclePageIndicator) findViewById(R.id.pgindicator);
            viewpagerAdapter = new BannerImgsViewPagerAdapter(AgreeActivity.this, images);
            viewpagerBanner.setAdapter(viewpagerAdapter);
            pageIndicator.setViewPager(viewpagerBanner);

            NUM_PAGES = 3;
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES ) {
                        currentPage = 0;
                    }
                    viewpagerBanner.setCurrentItem(currentPage++, true);
                }
            };

            swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 1000, 5000); // end auto slide
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.TextViewagree:
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_agree", "agree");
                editor.commit();

                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                AgreeActivity.this.finish();
                break;
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        common.Snackbar(layout, getString(R.string.click_BACK_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
