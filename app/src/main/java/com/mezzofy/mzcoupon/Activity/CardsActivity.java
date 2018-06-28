package com.mezzofy.mzcoupon.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.module.Setting_Module;
import com.viewpagerindicator.TabPageIndicator;

public class CardsActivity extends Fragment {
    String[] CONTENT;

    String coupstats,usertype=null;
    ViewPager pager;
    private Setting_Module settingModule;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.cards_tabs, null);
        try {

            CONTENT = getActivity().getResources().getStringArray(R.array.cards_array);

            FragmentPagerAdapter adapter = new GoogleMusicAdapter(getActivity().getSupportFragmentManager());

            pager = (ViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);

            TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
            indicator.setViewPager(pager);

            Bundle extras = getActivity().getIntent().getExtras();

            settingModule=new Setting_Module(getActivity().getApplicationContext());

            if (extras != null)
                coupstats = extras.getString("coupstats");
            if (coupstats != null)
                if (!coupstats.equals(""))
                    pager.setCurrentItem(Integer.parseInt(coupstats));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Positon------",String.valueOf(position));
            switch (position) {
                case 0:
                    return new MyCouponCampaignList_Activity();
                case 1:
                    return new CouponRedeemList_Activity();
                case 2:
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
