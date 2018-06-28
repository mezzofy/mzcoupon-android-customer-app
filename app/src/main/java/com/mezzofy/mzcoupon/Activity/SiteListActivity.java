package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by udhayinforios on 21/1/16.
 */
public class SiteListActivity extends Activity {

    private Merchantsite_Module merchantsiteModule;
    private ProgressBar progress;

    List<SiteEntity> sitelist = new ArrayList<>();

    Integer mer_id;

    String mer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.choose_site);

            Intent intent = getIntent();
            mer_id = intent.getIntExtra("merch_id", 0);
            mer_name = intent.getStringExtra("merch_name");

            merchantsiteModule = new Merchantsite_Module(getApplicationContext());

            sitelist = merchantsiteModule.getSitelist();

            progress = (ProgressBar) findViewById(R.id.progress1);
            progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2D80A2"), android.graphics.PorterDuff.Mode.MULTIPLY);

            ListView listView = (ListView) findViewById(R.id.listView1);
            if(sitelist.size() > 0 )
            listView.setAdapter(new NoteAdapter(getApplicationContext(), sitelist));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(getApplicationContext(),MassCouponActivity.class);
                    intent.putExtra("site_id",sitelist.get(position).getSiteId());
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class NoteAdapter extends BaseAdapter {

        Context MyContext;
        List<SiteEntity> albumList;
        int pos = 0;

        public NoteAdapter(Context _MyContext, List<SiteEntity> _albumList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.merchant_list_adapter, null);
            } else {
                MyView = convertView;
            }

            TextView bustext = (TextView) MyView.findViewById(R.id.list_content);
            bustext.setText(albumList.get(position).getSiteName());

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
        super.onBackPressed();
        finish();
    }
}
