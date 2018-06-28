package com.mezzofy.mzcoupon.Activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.Item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.OrdermEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.CouponOrder_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

public class PreviewActivity extends Activity {

    ListView list;
    ArrayList<Item> items = new ArrayList<Item>();
    final Context context = this;

    private CouponOrder_Module couponOrderModule;
    private Coupon_Module couponModule;
    private Campaign_Module campaignModule;
    private Merchantsite_Module merchantsiteModule;
    private Customer_Module customerModule;

    String campaignId, siteid;
    String couponId, exp, frm,ordertype,siteName,Address;

    private EditText Contactfield, Remarkfield;

    OrderEntity orderRes;


    ProgressDialog progressDialog;
    AlertDialog alertlog;
    OrdermEntity response = null;

    DecimalFormat mFormat = new DecimalFormat("00");
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

    RelativeLayout layout;
    CommonUtils common;

    CustomerEntity customerData;
    SharedPreferences settings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            setContentView(R.layout.preview);

            couponOrderModule = new CouponOrder_Module(context);
            couponModule = new Coupon_Module(context);
            campaignModule = new Campaign_Module(context);
            customerModule=new Customer_Module(context);

            merchantsiteModule = new Merchantsite_Module(context);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            Bundle extras = getIntent().getExtras();
            siteid = extras.getString("siteid",null);
            frm = extras.getString("frm",null);
            ordertype=extras.getString("Ordertype",null);
            siteName=extras.getString("siteName",null);
            Address=extras.getString("Address",null);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);


            Log.d("cupid----",couponId);

            CouponEntity couponData = couponModule.getCoupon(couponId);
            customerData=customerModule.getUser();

            Contactfield = (EditText) findViewById(R.id.Contactfield);
            Contactfield.setText(customerData.getCustomerMobile());
            Remarkfield = (EditText) findViewById(R.id.Remarkfield);

            TextView textView1 = (TextView) findViewById(R.id.textView1);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            TextView textView3 = (TextView) findViewById(R.id.textView3);
            TextView textView4 = (TextView) findViewById(R.id.textView4);
            TextView textView6 = (TextView) findViewById(R.id.textView6);

            Button calbtn = (Button) findViewById(R.id.imageViewMail);
            calbtn.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getString(R.string.Are_you_sure));
            builder1.setCancelable(true);
            builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("tabName", "");
                    intent.putExtra("currTab", 2);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    PreviewActivity.this.finish();
                }


            });
            builder1.setNegativeButton(R.string.Cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            alertlog = builder1.create();

            calbtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    alertlog.show();
                }
            });

            textView1.setText(couponData.getCouponName());

            if(siteid!=null)
                textView2.setText(siteName);
            else if(Address!=null)
                textView2.setText(Address);


            textView3.setText(ordertype);

            TextView purchtext = (TextView) findViewById(R.id.purch);
            purchtext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(PreviewActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            OrderEntity orderData=new OrderEntity();
            orderData.setCustomerId(customerData.getCustomerId());
            orderData.setCustomerAddress(customerData.getCustomerAddress());
            orderData.setContactNo(Contactfield.getText().toString());
            orderData.setOrderRemark(Remarkfield.getText().toString());
            orderData.setSiteId(siteid);
            orderData.setOrderType(ordertype);
            orderData.setCouponId(couponId);

            OrdermEntity orderModel=new OrdermEntity();
            orderModel.setOrder(orderData);


            CouponOrder_Module module = new CouponOrder_Module(getApplicationContext());
            try {
                response = module.PostOrderToServer(orderModel);
            } catch (APIServerException e) {
                common.Snackbar(layout, e.getMessage());
            }
            if (response != null) {
                try {
                    module.addOrderlist(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            Intent intent = new Intent(context, Redeem_StatusActivity.class);

            if (response != null && response.getOrder()!=null && response.getOrder().getCouponId()!=null) {

                common.Snackbar(layout,getString(R.string.Order_Confirmed));

                //orderDao.deleteOrderpost(cupid);

                intent.putExtra("stk", "successredeem");

            } else {
                intent.putExtra("stk", "fail");
            }

            startActivity(intent);
            overridePendingTransition(0, 0);
            PreviewActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    private static String toUnicode(char ch) {
        return String.format("\\u%04x", (int) ch);
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();

    }

}
