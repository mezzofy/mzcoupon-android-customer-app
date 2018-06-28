package com.mezzofy.mzcoupon.Activity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Entity.PoListmEntity;
import com.mezzofy.mzcoupon.module.PoOrder_Module;
import com.mezzofy.mzcoupon.pojo.TransactionRes;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

public class PaymentTranscationActivity extends Activity {

    final Context context = this;
    ListView list;
    List<PoEntity> transList = null;
    private PoOrder_Module transcationModule;
    int offset = 0, totalCount = 0, count = 0;
    int size = 10;
    private PullToRefreshListView mPullRefreshListView;
    JSONObject jsonobj = null;
    List<TransactionRes> transactionList;
    String staffId;
    int pos;
    private ProgressBar progress;
    SharedPreferences settings;
    String flag = "pulldown";
    String frm;
    Gson gson = new Gson();
    RelativeLayout layout;
    CommonUtils common;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.transcation_main);
            transcationModule = new PoOrder_Module(context);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            progress = (ProgressBar) findViewById(R.id.progress1);

            Bundle extras = getIntent().getExtras();
            if (extras != null)
                frm = extras.getString("frm");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
            list = mPullRefreshListView.getRefreshableView();
            list.setSelector(R.drawable.listselector);
            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    position = (int) id;
                    PoEntity album = transList.get(position);

                    Intent intent = new Intent(context, Transcation_detailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("PoId", album.getPoId());
                    intent.putExtra("frm", frm);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    PaymentTranscationActivity.this.finish();
                }
            });


            mPullRefreshListView.setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START : Mode.BOTH);
            mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                    transactionList = transcationModule.getTranscList(staffId, 0, size);
//
//                    flag = "pullup";

//                    AsyncCallWS task = new AsyncCallWS();
//                    task.execute();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    // TODO Auto-generated method stub

//                    offset = (offset / 10) * 10;
//                    String res = JSONSTRINGS.getJSONFromUrl( "transactions/" + staffId + "?offset=" + offset + "&size=" + size);
//                    PaymentServer server = gson.fromJson(res, PaymentServer.class);
//
//                    if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("0")) {
//                        totalCount = Integer.parseInt(String.valueOf(server.getResponse().getTotal()));
//                        count = server.getResponse().getSize();
//                    }
//
//                    if (offset < totalCount)
//                        offset = offset + size;
//
//                    transactionList = transcationModule.getTranscList(staffId, offset, size);
//
//                    flag = "pulldown";

//                    AsyncCallWS task = new AsyncCallWS();
//                    task.execute();
                }
            });

            AsyncOnload task = new AsyncOnload();
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<PoEntity> albumList;
        int pos = 0;
        Boolean favclk = false;

        public ImageBaseAdapter(Context _MyContext, List<PoEntity> _albumList) {
            albumList = null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList = _albumList;
        }

        @Override
        public int getCount() {
            if(albumList!=null)
            return albumList.size();
            else
                return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.transcation, null);
            } else {
                MyView = convertView;
            }

            TextView trantext = (TextView) MyView.findViewById(R.id.textView1);
            TextView datetext = (TextView) MyView.findViewById(R.id.textView3);
            TextView selltext = (TextView) MyView.findViewById(R.id.textView2);
            TextView tranNotext = (TextView) MyView.findViewById(R.id.textView4);
            ImageView remov = (ImageView) MyView.findViewById(R.id.imageViewRemove);
            TextView payresp = (TextView) MyView.findViewById(R.id.TextView01);
            payresp.setTextColor(Color.parseColor("#ffec0805"));
            payresp.setTypeface(Typeface.DEFAULT_BOLD);

            tranNotext.setText(albumList.get(position).getPoId().toString());
//					    tranNotext.setTextColor(Color.parseColor("#ffec0805"));
            tranNotext.setTypeface(Typeface.DEFAULT_BOLD);
            trantext.setText(albumList.get(position).getPoNo());
            if (settings.getString("decimal", "N").equals("Y")) {
                selltext.setText(" " + settings.getString("currency", "IDR") + String.format("%,.2f", albumList.get(position).getPoTotal()) + " ");
            } else {
                selltext.setText(" " + settings.getString("currency", "IDR") + String.format("%,.0f", albumList.get(position).getPoTotal()) + " ");

            }


            if(albumList.get(position).getPoDate()!=null) {
                long timestamp = Long.parseLong(albumList.get(position).getPoDate());
                Log.d("timestamp po", timestamp + "  - " + albumList.get(position).getPoDate());

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                sdf.setTimeZone(TimeZone.getDefault());
                String formattedDate = sdf.format(timestamp);
                datetext.setText(formattedDate);
            }
            else
                datetext.setText("");


            if (albumList.get(position).getPayResponse().equals("0")) {
                if (!albumList.get(position).getPoStatus().equals("null")) {
                    payresp.setText(albumList.get(position).getPoStatus());
                } else {
                    payresp.setText(getString(R.string.Success));
                }
            } else {
                payresp.setText(getString(R.string.Failure));
            }

            remov.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeListItem(position);
                }
            });

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


    protected void removeListItem(int positon) {

        pos = positon;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.Sure_Delete);
        alertDialogBuilder.setPositiveButton(R.string.Yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        transcationModule.deleteTransc(transList.get(pos).getTransactionId());
//                        transcationDao.deleteTrans(transList.get(pos).getTransactionId());
//                        transList.remove(pos);
                        list.setAdapter(new ImageBaseAdapter(PaymentTranscationActivity.this, transList));

                        common.Snackbar(layout, getString(R.string.Item_Deleted));
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


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

            PoListmEntity poListModel = transcationModule.getOrderAPI(staffId, 1);
            if (poListModel != null) {
                try {
                    transcationModule.addOrderdetail(poListModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            transList = null;
            try {
                transList = transcationModule.getPolist();
            } catch (Exception e) {
                e.printStackTrace();
            }

            progress.setVisibility(View.INVISIBLE);
            list.setClickable(true);
            list.setEnabled(true);

            if (PaymentTranscationActivity.this != null && transList!=null)
                if (!PaymentTranscationActivity.this.isFinishing()) {
                    list.setAdapter(new ImageBaseAdapter(getApplicationContext(), transList));
                }

            if (transactionList != null)
                transactionList.clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        PaymentTranscationActivity.this.finish();
    }

}