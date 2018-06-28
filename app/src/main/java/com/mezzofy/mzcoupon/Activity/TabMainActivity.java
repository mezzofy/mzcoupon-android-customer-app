package com.mezzofy.mzcoupon.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.FontsOverride;


@SuppressWarnings("deprecation")
public class TabMainActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    public static TabMainActivity tabs;
    int notif_count;
    int cutab = 0;
    int tabstatus = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tabs = this;
        setContentView(R.layout.tabview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FontsOverride.setDefaultFont(this, "DEFAULT", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Roboto-Light.ttf");

        setTabs();

        getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int i = getTabHost().getCurrentTab();
                TabHost tabHost = getTabHost();
                TabWidget tw = tabHost.getTabWidget();

                for (int j = 0; j < tw.getChildCount(); j++) {
                    if (i == j) {
                        TextView tabContent = (TextView) tabHost.getTabWidget().getChildAt(j).findViewById(R.id.title);
                        tabContent.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        TextView tabContent = (TextView) tabHost.getTabWidget().getChildAt(j).findViewById(R.id.title);
                        tabContent.setTextColor(Color.parseColor("#9c9c9b"));
                    }
                }
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("passcode", "");
                editor.apply();
            }
        });
    }

    public void setTabs() {
        Bundle extras = getIntent().getExtras();
        int currTab = 0;
        String tabName = "";

        notif_count = 0;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notif_count", notif_count);
        editor.apply();

        if (extras != null) {
            currTab = extras.getInt("currTab");
            tabName = extras.getString("tabName");
            tabstatus = extras.getInt("tabstatus");
        }

        notif_count = settings.getInt("notif_count", 0);

        addTab(getString(R.string.menu_Deals), getResources().getDrawable(R.drawable.icon_activities_tab), CampaignMainList_Activity.class, currTab);

    }

    private void addTab(String labelId, Drawable drawableId, Class<?> c, int currTab) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        intent.putExtra("tabstatus", tabstatus);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setTextColor(Color.parseColor("#9c9c9b"));
        title.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        title.setText(labelId);

        if (currTab == cutab)
            title.setTextColor(Color.parseColor("#FFFFFF"));

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageDrawable(drawableId);
        TextView notfi = (TextView) tabIndicator.findViewById(R.id.textView1);
        notfi.setVisibility(View.INVISIBLE);


        if (notif_count != 0) {
            notfi.setVisibility(View.VISIBLE);
            notfi.setText(String.valueOf(notif_count));
            notif_count = 0;
        }

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(currTab);
        cutab++;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TabMainActivity.this.finish();
    }

}