package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.OrdermEntity;
import com.mezzofy.mzcoupon.Entity.SitemEnity;
import com.mezzofy.mzcoupon.module.CouponOrder_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by LENOVO on 18/06/2015.
 */
public class Merch_RedeemActivity extends Activity {

    private Coupon_Module couponModule;

    String exp, couponId, flag;

    String site_id,ref_no;

    String campaignId;
    String staffId;
    String merchid;

    TextView textView1, textView2, textView3, textView4,demomerchant;
    EditText inputtxt;

    AlertDialog.Builder alertDialog, alertDialog2;

    DecimalFormat mFormat = new DecimalFormat("00");
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

    OrdermEntity response = null;
    SitemEnity siteModel = null;
    CouponEntity cup;

    RelativeLayout layout;
    CommonUtils common;

    SharedPreferences settings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            setContentView(R.layout.merch_redeempage);
            couponModule = new Coupon_Module(getApplicationContext());
            Merchant_Module MDAO=new Merchant_Module(getApplicationContext());

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            Bundle extras = getIntent().getExtras();
            if(extras!=null) {
                flag = extras.getString("flag");
                site_id = extras.getString("site_id");
                ref_no = extras.getString("ref_no");
            }

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);

            cup = couponModule.getCoupon(couponId);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            MerchantEntity objmerchant=MDAO.getMerchantList(merchid);
            textView1 = (TextView) findViewById(R.id.textView18);
            textView2 = (TextView) findViewById(R.id.textView19);
            textView3 = (TextView) findViewById(R.id.textView20);
            textView4 = (TextView) findViewById(R.id.textView21);
            inputtxt = (EditText) findViewById(R.id.editText);
            demomerchant=(TextView)findViewById(R.id.textView17);
            demomerchant.setText(objmerchant.getMerchantName());

            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");
            inputtxt.setText("");

            alertDialog = new AlertDialog.Builder(Merch_RedeemActivity.this);
            alertDialog.setTitle(getString(R.string.Invalid_Pin_no));
//                    alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog.setPositiveButton(getString(R.string.Retry),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            textView1.setText("");
                            textView2.setText("");
                            textView3.setText("");
                            textView4.setText("");
                            inputtxt.setText("");
                        }
                    });
            alertDialog.setNegativeButton(getString(R.string.Close),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            textView1.setText("");
                            textView2.setText("");
                            textView3.setText("");
                            textView4.setText("");
                            inputtxt.setText("");

                            Intent intent = new Intent(getApplicationContext(), QrCodewalnetActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("campaignId", campaignId);
                            intent.putExtra("couponId", couponId);
                            intent.putExtra("exp", exp);

                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            Merch_RedeemActivity.this.finish();
                        }
                    });

            alertDialog2 = new AlertDialog.Builder(Merch_RedeemActivity.this);
            alertDialog2.setTitle(getString(R.string.Confirm_Redeem));
//                    alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog2.setPositiveButton(getString(R.string.Yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            AsyncOnPost asyncOnCheck = new AsyncOnPost();
                            asyncOnCheck.execute();
                        }
                    });
            alertDialog2.setNegativeButton(getString(R.string.NO),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            textView1.setText("");
                            textView2.setText("");
                            textView3.setText("");
                            textView4.setText("");
                            inputtxt.setText("");

                            Intent intent = new Intent(getApplicationContext(), QrCodewalnetActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("campaignId", campaignId);
                            intent.putExtra("couponId", couponId);
                            intent.putExtra("exp", exp);

                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            Merch_RedeemActivity.this.finish();
                        }
                    });

            inputtxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    textView1.setText("");
                    textView2.setText("");
                    textView3.setText("");
                    textView4.setText("");

                    int snum = inputtxt.length();

                    switch (snum) {
                        case 1:
                            textView1.setText("X");
                            break;
                        case 2:
                            textView1.setText("X");
                            textView2.setText("X");
                            break;
                        case 3:
                            textView1.setText("X");
                            textView2.setText("X");
                            textView3.setText("X");

                            break;
                        case 4:
                            textView1.setText("X");
                            textView2.setText("X");
                            textView3.setText("X");
                            textView4.setText("X");
                            break;
                    }

                    if (inputtxt.length() == 4) {
                        AsyncOnCheck asyncOnCheck = new AsyncOnCheck();
                        asyncOnCheck.execute();
                    }

                    if (inputtxt.length() > 4) {
                        textView1.setText("");
                        textView2.setText("");
                        textView3.setText("");
                        textView4.setText("");
                        inputtxt.setText("");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class AsyncOnCheck extends AsyncTask<String, Void, Void> {

        String passcode = null;

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            passcode = inputtxt.getText().toString();
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            Coupon_Module couponModule = new Coupon_Module(getApplicationContext());
            siteModel = couponModule.sitepassCheck(passcode);

            return null;

        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if (siteModel == null) {

                common.Snackbar(layout, getString(R.string.Invalid_Pin_no));

            } else {
                if(ref_no==null ) {
                    alertDialog2.setCancelable(false);
                    alertDialog2.show();
                }
                else if(ref_no!=null && siteModel.getSite().getSiteId().equals(site_id)){
                    alertDialog2.setCancelable(false);
                    alertDialog2.show();
                }
                else {
                    common.Snackbar(layout, getString(R.string.Invalid_Pin_no));
                }
            }

            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");
            inputtxt.setText("");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");

        }
    }


    public class AsyncOnPost extends AsyncTask<String, Void, Void> {

        CouponOrder_Module couponOrderModule = new CouponOrder_Module(getApplicationContext());
        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            Log.i("TAG", "onPostExecute");
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            try {
                if (siteModel != null) {

                    OrderEntity orderData = new OrderEntity();

                    String dmyhms = mFormat.format(Double.valueOf(cal.get(Calendar.DATE))) + "-" + mFormat.format(Double.valueOf(cal.get(Calendar.MONTH) + 1)) + "-" + cal.get(Calendar.YEAR) + " " + mFormat.format(Double.valueOf(cal.get(Calendar.HOUR_OF_DAY))) + ":" + mFormat.format(Double.valueOf(cal.get(Calendar.MINUTE))) + ":" + mFormat.format(Double.valueOf(cal.get(Calendar.SECOND)));
                    if (flag != null && flag.equals("mass")) {

//                    OrderPostRes res = transcationModule.massManualRedeem(site_id, couponId);
//
//                    orderPostRes.setCouponId(couponId);
//                    orderPostRes.setOrderDate(dmyhms);
//                    orderPostRes.setPickupDelivery("O");
//                    orderPostRes.setSiteId(site_id);
//                    orderPostRes.setProdId(0);
//                    orderPostRes.setCustomerId(staffId);5
//
//                    if(res != null) {
//                        response = transcationModule.postCouponOrder(orderPostRes);
//                    }else{
//                        response = null;
//                    }

                    }

                    else {
                        orderData.setSiteId(siteModel.getSite().getSiteId());
                        orderData.setCouponId(cup.getCouponNo());
                        orderData.setOrderDate(dmyhms);
                        orderData.setOrderType("R");
                        orderData.setCustomerId(staffId);
                        orderData.setCouponId(couponId);

                        OrdermEntity orderModel=new OrdermEntity();
                        orderModel.setOrder(orderData);

                        response = couponOrderModule.PostOrderToServer(orderModel);
                    }
                }


            } catch (APIServerException e) {
                common.Snackbar(layout, e.getMessage());
            }
            return null;

        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            Intent intent = new Intent(getApplicationContext(), Redeem_StatusActivity.class);

            if (response != null) {
                try {
                    couponOrderModule.addOrderlist(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                common.Snackbar(layout,getString(R.string.Order_Confirmed));
                intent.putExtra("stk", "successredeem");

            } else {
                intent.putExtra("stk", "fail");
            }

            startActivity(intent);
            overridePendingTransition(0, 0);
            Merch_RedeemActivity.this.finish();

            textView1.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");
            inputtxt.setText("");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), QrCodewalnetActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        Merch_RedeemActivity.this.finish();
    }
}
