package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.Entity.NotificationmEntity;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.module.Notification_Module;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


public class NotificationActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;
    ListView list;
    int offset = 0, totalCount = 0, count = 0;
    private PullToRefreshListView mPullRefreshListView;
    String staffId;
    int pos;
    private ProgressBar progress;
    SharedPreferences settings;
    String flag = "pulldown";
    String frm;
    int Page=1;
    ImageView imagetemp;
    TextView txttemp;
    SizemEnity size;

    Notification_Module notificationModule;



    List<NotificationmEntity> notifiList = new ArrayList<NotificationmEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.topup_transaction);

            TextView txtitle = (TextView) findViewById(R.id.txtTitle);
            txtitle.setText(getString(R.string.notification));

            progress = (ProgressBar) findViewById(R.id.progress1);
            imagetemp = (ImageView) findViewById(R.id.imageView20);
            txttemp = (TextView) findViewById(R.id.textView33);

            notificationModule = new Notification_Module(getApplicationContext());

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
            list = mPullRefreshListView.getRefreshableView();
            list.setSelector(R.drawable.listselector);

            mPullRefreshListView.setMode(mPullRefreshListView.getMode() == PullToRefreshBase.Mode.BOTH ? PullToRefreshBase.Mode.PULL_FROM_START : PullToRefreshBase.Mode.BOTH);
            mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                    flag = "pullup";

                    Page=1;
                    AsyncOnload onload = new AsyncOnload();
                    onload.execute();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    // TODO Auto-generated method stub


                    flag = "pulldown";
                    try {
                        size=(SizemEnity) ObjectSerializer.deserialize(settings.getString("size", ObjectSerializer.serialize(new SizemEnity())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(++Page<=size.getPagesize()) {
                        AsyncOnload onload = new AsyncOnload();
                        onload.execute();
                    }
                }
            });

            AsyncOnload task = new AsyncOnload();
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);

        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            notifiList = notificationModule.getNotificationListfromserver(staffId, Page);

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if (notifiList != null && notifiList.size() == 0) {
                imagetemp.setVisibility(View.VISIBLE);
                txttemp.setVisibility(View.VISIBLE);
            } else {
                imagetemp.setVisibility(View.GONE);
                txttemp.setVisibility(View.GONE);
            }

            progress.setVisibility(View.INVISIBLE);
            mPullRefreshListView.onRefreshComplete();
            list.setClickable(true);
            list.setEnabled(true);

            if (NotificationActivity.this != null)
                if (!NotificationActivity.this.isFinishing()) {
                    if (notifiList != null)
                        list.setAdapter(new ImageBaseAdapter(getApplicationContext(), notifiList));
                    if (flag.equals("pulldown")) {
                        if (notifiList != null && notifiList.size() > 0)
                            list.setSelection(notifiList.size());
                    }
                }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<NotificationmEntity> albumList;
        int pos = 0;
        Boolean favclk = false;

        public ImageBaseAdapter(Context _MyContext, List<NotificationmEntity> _albumList) {
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
                MyView = li.inflate(R.layout.notify_adapter, null);
            } else {
                MyView = convertView;
            }
            TextView title = (TextView) MyView.findViewById(R.id.title);
            TextView desc = (TextView) MyView.findViewById(R.id.desc);
            TextView date = (TextView) MyView.findViewById(R.id.date);

            title.setText(albumList.get(position).getNotification().getNotificationTitle());

            desc.setText(org.apache.commons.lang.StringEscapeUtils.unescapeJava(albumList.get(position).getNotification().getNotificationDesc()));
            desc.setMaxLines(2);


            long timestamp = Long.parseLong(albumList.get(position).getNotification().getCreatedOn());
            Log.d("timestamp po",timestamp+"  - "+albumList.get(position).getNotification().getCreatedOn());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(timestamp);
            date.setText(formattedDate);

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
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 3);
        startActivity(intent);
        overridePendingTransition(0, 0);
        NotificationActivity.this.finish();
    }
}