package com.mezzofy.mzcoupon.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.mezzofy.mzcoupon.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by aruna on 11/6/17.
 */

public class CouponCardsActivity extends FragmentActivity {
    String[] CONTENT;

    private boolean doubleBackToExitPressedOnce = false;


    String coupstats;
    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.cards_tabs);

            CONTENT = getApplicationContext().getResources().getStringArray(R.array.cards_array);

            FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);

            TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(pager);

            Bundle extras = getIntent().getExtras();

            if (extras != null)
                coupstats = extras.getString("coupstats");
            if (coupstats != null)
                if (!coupstats.equals(""))
                    pager.setCurrentItem(Integer.parseInt(coupstats));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new MyCouponCampaignList_Activity();
                case 1:
                    // Games fragment activity
                    return new CouponRedeemList_Activity();
                case 2:
                    // Games fragment activity
                    return new CouponExpList_Activity();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
