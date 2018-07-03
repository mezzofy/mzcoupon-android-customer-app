package com.mezzofy.mzcoupon.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Adapter.CartBaseAdapter;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Cart_Module;
//import com.mezzofy.mojodomo.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.pojo.PaypalRes;
import com.mezzofy.mzcoupon.pojo.TransactionRes;
import com.mezzofy.mzcoupon.pojo.Transactiondtl;
import com.paypal.android.sdk.payments.*;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CartActivity extends Fragment implements OnClickListener {

    ListView list;
    List<CartEntity> cartList = null;


    private Campaign_Module campaignModule;
    private Customer_Module userModule;
    private Cart_Module cartModule;

    private boolean doubleBackToExitPressedOnce = false;

    String Msg, RespNo, RefNo, Respcode;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;

    enum Direction {LEFT, RIGHT;}

    ProgressDialog progressDialog;
    TextView titletext;
    String flag = "active";
    int pos;
    private ProgressBar progress_detail;
    ImageView activ;
    double latitude;
    double longitude;


    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config;

    private JSONObject jsonobj = null;
    Gson gson = new Gson();

    SharedPreferences settings;
    private String randomno;

    RelativeLayout layout;
    CommonUtils common;
    View view;
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            cartModule = new Cart_Module(getActivity());
            userModule = new Customer_Module(getActivity());
            campaignModule = new Campaign_Module(getActivity());


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            Msg = settings.getString("Msg", null);
            RespNo = settings.getString("RespNo", null);
            RefNo = settings.getString("RefNo", null);
            Respcode = settings.getString("Respcode", null);

            if (Msg != null)
                if (Msg.equals("approved")) {
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                } else {
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }

            cartList = null;
            cartList = cartModule.getCartList();

            if (cartList!=null && cartList.size() == 0) {
                view = inflater.inflate(R.layout.cart_temp_layout, null);
            } else {
                view = inflater.inflate(R.layout.cart, null);

                progress_detail = (ProgressBar) view.findViewById(R.id.progress1);
                list = (ListView) view.findViewById(R.id.listView1);
                list.setSelector(R.drawable.listselector);

                if(cartList!=null)
                    list.setAdapter(new CartBaseAdapter(getActivity(), cartList));

                list.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        position = (int) id;
                        AsyncCallDetail task = new AsyncCallDetail();
                        task.execute(position);
                    }
                });
            }

            common = new CommonUtils();
            layout = (RelativeLayout) view.findViewById(R.id.signin_page);

            TextView pay = (TextView) view.findViewById(R.id.paytext);
            pay.setOnClickListener(this);

            ImageView transaction = (ImageView) view.findViewById(R.id.imageView2);
            transaction.setOnClickListener(this);

            titletext = (TextView) view.findViewById(R.id.txtTitle);

            if(cartModule.getTotalcart()>0) {
                if (settings.getString("decimal", "N").equals("Y")) {
                    titletext.setText(settings.getString("currency", "IDR") + String.format("%,.2f", cartModule.getTotalcart()));
                } else {
                    titletext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", cartModule.getTotalcart()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.paytext:
                paynowClick();
                break;
            case R.id.imageView2:
                Intent intent = new Intent(getActivity(), PaymentTranscationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
    }

    protected void paynowClick() {

        if (cartList.size() != 0) {
//            if (!DetectConnection.checkInternetConnection(context)) {
//                common.Snackbar(layout, getString(R.string.No_Internet));
//            } else {
            flag = "active";
//                for (CartEntity album : cartList) {
//                    flag=album.getFlag();
//                }

            if (flag.equals("active")) {

                randomno = CommonModule.uniqueid().replace("-", "").toUpperCase();

                CustomerEntity resval = null;
                try {
                    resval = userModule.getUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resval != null) {
//                        if (resval.getUserType().equals("S") || resval.getUserType().equals("A")) {
//                            Intent intent = new Intent(CartActivity.this, AgentCashActivtiy.class);
//                            try {
//                                intent.putExtra("total", cartModule.getTotalcart());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            intent.putExtra("topup", "cart");
//                            startActivity(intent);
//                            overridePendingTransition(0, 0);
//                        } else {
                    Intent intent = new Intent(getActivity(), MyScanActivity.class);
                    try {
                        intent.putExtra("total", cartModule.getTotalcart());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("topup", "cart");
                    startActivity(intent);

//                        }
//                    } else {
//                        common.Snackbar(layout, flag + "  not unavailable, please remove it ");
//
//                    }
                }
            }//net connection
//        } else {
//            common.Snackbar(layout, getString(R.string.Cart_Is_Empty));
//        }
        }
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }


    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            CustomerEntity staffRes = null;
            try {
                staffRes = userModule.getUser();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TransactionRes Res = new TransactionRes();
            Res.setTransactionNo(RespNo);
            Res.setPayReceipt(RefNo);
            Res.setPayResponse(Respcode);
            Res.setTransactionId(0);
            Res.setCouponType("C");
            Res.setPlaceId(staffRes.getMerchantId());
            Res.setCustomerId(staffRes.getCustomerId());
//            try {
//                Res.setTransactionTotal(cartModule.getTotalcart());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Res.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()).toString());
            Res.setHashCode("0");
            Res.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()).toString());
            Res.setStatus("A");

            List<Transactiondtl> transdtls = new ArrayList<Transactiondtl>();
            try {
                cartList = cartModule.getCartList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cartList != null && cartList.size()>0)
                for (CartEntity res : cartList) {
                    Transactiondtl transd = new Transactiondtl();
//                    transd.setProdId(res.getProdId());
                    transd.setProdName(res.getCampaignName());
                    transd.setProdPrice(Double.parseDouble(res.getOrginalPrice()));
                    transd.setProdQty(Double.valueOf(res.getProductQty()));
                    transd.setProdTotal(Double.valueOf(res.getTotalPrice()));
                    transdtls.add(transd);
                }

            Res.setTransdtls(transdtls);

//            TranscationModule module = new TranscationModule(getApplicationContext());
//            TransactionRes response = module.postTransc(Res);
//
//            if (response.getTransactionId() != 0) {
//                TransactionRes result = module.getTransc(response.getTransactionId());
//                if (result != null) {
//                    module.addTransc(result);
//                }
//            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            Intent intent = new Intent(getActivity(), TabViewActivtiy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("tabName", "");

            if (Msg != null)
                if (Msg.equals("approved")) {
                    try {
                        cartModule.deletechart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("currTab", 3);
                } else {
                    intent.putExtra("currTab", 4);
                }

            cartList = null;
            try {
                cartList = cartModule.getCartList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cartList != null && cartList.size() > 0)
                list.setAdapter(new CartBaseAdapter(getActivity().getApplicationContext(), cartList));

            try {
                titletext.setText(settings.getString("currency", "IDR") + String.format("%,.2f", cartModule.getTotalcart()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            int cout = 0;
            try {
                cout = cartModule.getItemcart();
            } catch (Exception e) {
                e.printStackTrace();
            }


            settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Msg", null);
            editor.putString("RespNo", null);
            editor.putString("RefNo", null);
            editor.putString("Respcode", null);
            editor.putInt("notif_count", cout);
            editor.commit();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public class AsyncCallDetail extends AsyncTask<Integer, Void, Void> {
        int position;
        CartEntity album = null;

        @Override
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress_detail.setVisibility(View.VISIBLE);
        }

        @Override
        protected synchronized Void doInBackground(Integer... params) {
            Log.i("TAG", "doInBackground");
            position = params[0];
            album = cartList.get(position);
            if (album != null) {
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            progress_detail.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getActivity(), Cart_ProdDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("prodid", album.getCampaignId());
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {

                        jsonobj = confirm.toJSONObject();
                        String temp = jsonobj.get("response").toString();
                        PaypalRes res = gson.fromJson(temp, PaypalRes.class);

                        if (res != null) {
                            Msg = res.getState();
                            if (res.getState().equals("approved")) {
                                Respcode = "0";
                            } else {
                                Respcode = "1";
                            }
                            RespNo = res.getId();
                            RefNo = randomno;
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("Msg", Msg);
                            editor.putString("RespNo", RespNo);
                            editor.putString("RefNo", RefNo);
                            editor.putString("Respcode", Respcode);
                            editor.commit();
                            if (Respcode.equals("0")) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_succe", "pay_succe");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_fail", "pay_fail");
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                common.Snackbar(layout, "An invalid MZPayment");
            }
        }
    }

}