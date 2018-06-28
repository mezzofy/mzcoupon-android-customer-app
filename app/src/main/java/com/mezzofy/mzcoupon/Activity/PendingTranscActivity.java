package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.mzcoupon.Entity.WalletTxnEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.Entity.SizemEnity;
import com.mezzofy.mzcoupon.Entity.WalletTxnListmEntity;
import com.mezzofy.mzcoupon.module.WalletTranscation_Module;


import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mezzofy.mzcoupon.apputills.CommonUtils;

/**
 * Created by LENOVO on 28/09/2015.
 */
public class PendingTranscActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;
    ListView list;
    List<WalletTxnEntity> transList = null;
    int Pages=1,limit=0,offset=10;
    private PullToRefreshListView mPullRefreshListView;
    JSONObject jsonobj = null;

    String staffId;
    int pos;
    private ProgressBar progress;
    SharedPreferences settings;
    String flag = "pulldown";
    String frm;
    ImageView imagetemp;
    TextView txttemp;
    TextView textView;
    private SizemEnity size;

    WalletTranscation_Module walletTranscation_module;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pending_transaction);

        walletTranscation_module = new WalletTranscation_Module(context);

        progress = (ProgressBar) findViewById(R.id.progress1);
        imagetemp = (ImageView) findViewById(R.id.imageView20);
        txttemp = (TextView) findViewById(R.id.textView33);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        staffId = settings.getString("staff_id", null);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textView = (TextView) findViewById(R.id.txtTitle);
        textView.setText(getString(R.string.pending_payment));

        transList = null;
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
        list = mPullRefreshListView.getRefreshableView();
        list.setSelector(R.drawable.listselector);

        mPullRefreshListView.setMode(mPullRefreshListView.getMode() == PullToRefreshBase.Mode.BOTH ? PullToRefreshBase.Mode.PULL_FROM_START : PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                flag = "pullup";
                Pages=1;
                limit=0;
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                flag = "pulldown";

                try {
                    size=(SizemEnity) ObjectSerializer.deserialize(settings.getString("WalletTranssize", ObjectSerializer.serialize(new SizemEnity())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(++Pages<=size.getPagesize()) {
                    limit+=offset;
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });

            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        List<WalletTxnEntity> albumList;
        int pos = 0;
        Boolean favclk = false;

        public ImageBaseAdapter(Context _MyContext, List<WalletTxnEntity> _albumList) {
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
                MyView = li.inflate(R.layout.pendingtransc, null);
            } else {
                MyView = convertView;
            }

            TextView trantext = (TextView) MyView.findViewById(R.id.textView1);
            TextView selltext = (TextView) MyView.findViewById(R.id.textView2);
            TextView tranNotext = (TextView) MyView.findViewById(R.id.textView4);
            TextView mertext = (TextView) MyView.findViewById(R.id.textView5);
            TextView outltext = (TextView) MyView.findViewById(R.id.textView6);
            TextView AppBtn = (TextView) MyView.findViewById(R.id.textView7);
            TextView RjBtn = (TextView) MyView.findViewById(R.id.textView8);
            TextView payresp = (TextView) MyView.findViewById(R.id.TextView01);
            payresp.setTypeface(Typeface.DEFAULT_BOLD);

            try {
                Date sx = new SimpleDateFormat("yyyyMMddHHmm").parse(albumList.get(position).getTransactionDate());
                tranNotext.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(sx).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            payresp.setTypeface(Typeface.DEFAULT_BOLD);
            payresp.setText(albumList.get(position).getTransactionPayRef());

            selltext.setText(" HK$ " + String.format("%,.2f", albumList.get(position).getTransactionAmount()) + " ");

//            mertext.setText(albumList.get(position).getMerchant_name());
//            outltext.setText(albumList.get(position).getOutlet_name());

            if (albumList.get(position).getTransactionType().equals("P")) {
                trantext.setText(getString(R.string.PENDING));
                trantext.setTextColor(Color.parseColor("#E65100"));
            } else if (albumList.get(position).getTransactionType().equals("T")) {
                trantext.setText(getString(R.string.TOPUP));
                trantext.setTextColor(Color.parseColor("#18FFFF"));
            } else if (albumList.get(position).getTransactionType().equals("F")) {
                trantext.setText(getString(R.string.REJECTED));
                trantext.setTextColor(Color.parseColor("#DD2C00"));
            } else if (albumList.get(position).getTransactionType().equals("M")) {
                trantext.setText(MyContext.getResources().getString(R.string.adjustment_m));
                trantext.setTextColor(Color.parseColor("#4CAF50"));
            } else if (albumList.get(position).getTransactionType().equals("A")) {
                trantext.setText(MyContext.getResources().getString(R.string.adjustment_a));
                trantext.setTextColor(Color.parseColor("#18FFFF"));
            } else if (albumList.get(position).getTransactionType().equals("D")) {
                trantext.setText(MyContext.getResources().getString(R.string.Payment));
                trantext.setTextColor(Color.parseColor("#4CAF50"));
            } else if (albumList.get(position).getTransactionType().equals("V")) {
                trantext.setText(MyContext.getResources().getString(R.string.Void));
                trantext.setTextColor(Color.parseColor("#00bfff"));
            }

            AppBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeListItem(position, 1, "Approve");
                }
            });

            RjBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeListItem(position, 2, "Decline");
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

    protected void removeListItem(final int position, final int resp, final String msg) {

        pos = position;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (msg.equals("Approve"))
            alertDialogBuilder.setMessage(getString(R.string.Confirm_Approve));
        else
            alertDialogBuilder.setMessage(getString(R.string.Confirm_Decline));


        alertDialogBuilder.setPositiveButton(R.string.Yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        String apprcode = Utils.md5(Utils.md5(Utils.md5(transList.get(position).getTransactionReference() + String.format("%.2f", transList.get(position).getTransactionAmount()) + transList.get(position).getCustomerId() + resp)));
//
//                        WalletPayment walletPayment = new WalletPayment();
//                        walletPayment.setCustomer_id(transList.get(position).getCustomerId());
//                        walletPayment.setPayment_response(resp);
//                        walletPayment.setApproval_code(apprcode);
//                        walletPayment.setTransaction_amount(transList.get(position).getTransaction_amount());
//                        walletPayment.setTransaction_reference(transList.get(position).getTransaction_reference());
//                        walletPayment.setTransactionPayRef(transList.get(position).getTransaction_pay_ref());
//                        walletPayment.setCreated_on(transList.get(position).getCreated_on());
//                        WalletServer server = transcationModule.payResp(walletPayment);
//                        if (server == null || server.getResult().getResponse_code().equals("1") || server.getResult().getResponse_code().equals("11007") || server.getResult().getResponse_code().equals("11008")) {
//                            if (msg.equals("Approve"))
//                                CommonUtils.Snackbar(textView,getString(R.string.Failure));
//                            else
//                                CommonUtils.Snackbar(textView, getString(R.string.Decline) + " " + getString(R.string.Failure));
//
//                        } else {
                            try {
                                walletTranscation_module.Deletewallettrans(transList.get(pos).getTransactionReference());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            transList.remove(pos);
                             if(transList!=null && transList.size()>0)
                                 list.setAdapter(new ImageBaseAdapter(PendingTranscActivity.this, transList));

                            if (transList.size() == 0) {
                                imagetemp.setVisibility(View.VISIBLE);
                                txttemp.setVisibility(View.VISIBLE);
                            } else {
                                imagetemp.setVisibility(View.GONE);
                                txttemp.setVisibility(View.GONE);
                            }
                            if (msg.equals("Approve"))
                                CommonUtils.Snackbar(textView, getString(R.string.Approve) + " " + getString(R.string.Success));
                            else
                            CommonUtils.Snackbar(textView,getString(R.string.Decline) + " " + getString(R.string.Success));
//                        }

                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.Cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        WalletTxnListmEntity transaction;
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            list.setClickable(false);
            list.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");


            transaction = walletTranscation_module.getwalletPendingTransc(staffId,"P",Pages);

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if (transaction != null) {
                try {
                    walletTranscation_module.addWalletTransaction(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                transList = walletTranscation_module.getPandingTransList(limit, offset);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (transList == null) {
                imagetemp.setVisibility(View.VISIBLE);
                txttemp.setVisibility(View.VISIBLE);
                txttemp.setText(R.string.No_PENDTranscation);
            } else {
                imagetemp.setVisibility(View.GONE);
                txttemp.setVisibility(View.GONE);
            }

            mPullRefreshListView.onRefreshComplete();
            list.setClickable(true);
            list.setEnabled(true);

            if (PendingTranscActivity.this != null && transList!=null)
                if (!PendingTranscActivity.this.isFinishing()) {
                    list.setAdapter(new ImageBaseAdapter(getApplicationContext(), transList));
                    if (flag.equals("pulldown")) {
                        list.setSelection(transList.size());
                    }
                }

            if (transList != null)
                transList.clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

}