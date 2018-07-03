package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailEntity;
import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailEntity;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.PoDetailmEntity;
import com.mezzofy.mzcoupon.Entity.PomEntity;
import com.mezzofy.mzcoupon.Entity.WalletmEntity;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.Adapter.MyScanAdapter;
import com.mezzofy.mzcoupon.module.Payment_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;
import com.mezzofy.mzcoupon.pojo.CardRes;
import com.mezzofy.mzcoupon.pojo.PaypalRes;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mezzofy.mzcoupon.apputills.CommonModule;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class MyScanActivity extends Activity implements AdapterView.OnItemClickListener {

    final String TAG = getClass().getName();

    private String randomno;

    private double total;

    private int tot, totl;
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    ListView listview = null;
    ArrayList<Item> items = new ArrayList<Item>();
    List<PaymentDetailEntity> payURLList = null;
    List<PaymentDetailEntity> payList = null;

    ProgressDialog progressDialog;

    Payment_Module paymentModule;

    private ProgressBar progress;

    SharedPreferences settings;

    private Cart_Module cartModule;


    private static String CONFIG_ENVIRONMENT;
    private static String CONFIG_CLIENT_ID;

    private static final int REQUEST_CODE_PAYMENT = 1;

    List<CartEntity> cartList = null;

    private static PayPalConfiguration config;

    String Msg, RespNo, RefNo, Respcode;

    private JSONObject jsonobj = null;
    Gson gson = new Gson();

    String url;

    Customer_Module userModule;


    String email, staffname, mobile, topup, skip, message;

    int transfer_id;
    Wallet_Module walletModule;
    WalletEntity wallet;
    String custid,flage,topupflag;

    CustomerEntity staffRes;
        private GoogleApiClient client;


    RelativeLayout layout;
    CommonUtils common;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.scan_card);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Msg = settings.getString("Msg", null);
            RespNo = settings.getString("RespNo", null);
            RefNo = settings.getString("RefNo", null);
            Respcode = settings.getString("Respcode", null);
            custid = settings.getString("staff_id", null);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            walletModule = new Wallet_Module(getApplicationContext());
            userModule = new Customer_Module(getApplicationContext());
            paymentModule=new Payment_Module(getApplicationContext());

            cartModule = new Cart_Module(this);

            cartList = cartModule.getCartList();

            wallet = walletModule.getwalletdetail(custid);

            staffRes = userModule.getUser();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                randomno = CommonModule.uniqueid().replace("-", "").toUpperCase();
                total = extras.getDouble("total");
                tot = (int) (total * 100);
                totl = (int) (total);

                email = extras.getString("email", "");
                staffname = extras.getString("staff_name", "");
                mobile = extras.getString("mobile", "");
                transfer_id = extras.getInt("transfer_id", 0);
                topup = extras.getString("topup");
                skip = extras.getString("skip");
                message = extras.getString("message");
                flage=extras.getString("flag");
                topupflag=extras.getString("topupflag");
            }

//        CONTENT = getApplicationContext().getResources().getStringArray(R.array.Cards_Pay);

            progress = (ProgressBar) findViewById(R.id.progress1);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            listview = (ListView) findViewById(R.id.listView1);
            listview.setOnItemClickListener(this);

            AsyncOnload asyncOnload = new AsyncOnload();
            asyncOnload.execute();



        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        Intent scanIntent;

             url = payURLList.get(position).getPaymentUrl();
            switch (payURLList.get(position).getPaymentType()) {

                case "Red Dot":
                    scanIntent = new Intent(this, CardIOActivity.class);
                    // customize these values to suit your needs.
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false
                    scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false
                    scanIntent.putExtra("paykey", payURLList.get(position).getPaymentKey());
                    scanIntent.putExtra("merhid", payURLList.get(position).getPaymentMerchantId());
                    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
                    break;

                case "eNETS":
                    scanIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                    scanIntent.putExtra("randomno", CommonModule.uniqueid());
                    scanIntent.putExtra("total", total);
                    scanIntent.putExtra("url", url);
                    scanIntent.putExtra("paytype", "enets");
                    scanIntent.putExtra("paykey", payURLList.get(position).getPaymentKey());
                    scanIntent.putExtra("merhid", payURLList.get(position).getPaymentMerchantId());
                    startActivity(scanIntent);
                    overridePendingTransition(0, 0);
                    break;

                case "China UnionPay":
                    scanIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                    scanIntent.putExtra("randomno", CommonModule.uniqueid());
                    scanIntent.putExtra("total", total);
                    scanIntent.putExtra("url", url);
                    scanIntent.putExtra("paytype", "unionpay");
                    scanIntent.putExtra("paykey", payURLList.get(position).getPaymentKey());
                    scanIntent.putExtra("merhid", payURLList.get(position).getPaymentMerchantId());
                    startActivity(scanIntent);
                    overridePendingTransition(0, 0);
                    break;

                case "AliPay":
                    scanIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                    scanIntent.putExtra("randomno", CommonModule.uniqueid());
                    scanIntent.putExtra("total", total);
                    scanIntent.putExtra("paytype", "Alipay");
                    scanIntent.putExtra("url", url);
                    scanIntent.putExtra("paykey", payURLList.get(position).getPaymentKey());
                    scanIntent.putExtra("merhid", payURLList.get(position).getPaymentMerchantId());
                    startActivity(scanIntent);
                    overridePendingTransition(0, 0);
                    break;

                case "MZWallet":
                    promerceDollar();
                    break;

                case "Paypal":
                    if(topupflag!=null && topupflag.equals("topuppaypal"))
                    {
                        PomEntity poListModel = new PomEntity();
                        String Paymentdetailid = payURLList.get(position).getPaymentDetailId();
                        String ref_No = UUID.randomUUID().toString();
                        PoEntity poData = new PoEntity();
                        poData.setCustomerId(custid);
                        poData.setPoNo(ref_No);
                        poData.setPoStatus("A");
                        poData.setPayResponse("0");
                        poData.setCouponType("T");
                        poData.setPoTotal(total);
                        poListModel.setPo(poData);
                        PoEntity poData1 = null;
                        try {
                            poData1 = paymentModule.DownloadChargeCouponFromServer(poListModel);
                        } catch (APIServerException e) {
                            Toast.makeText(getApplicationContext(),"Error--"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (poData1 != null) {
                            Intent intent = new Intent(getApplicationContext(), PrivacyActivity.class);
                            intent.putExtra("flag", "paypal");
                            if(poData1.getPayToken()!=null)
                                intent.putExtra("paytoken",poData1.getPayToken());
                            else
                                intent.putExtra("paytoken","0");
                            intent.putExtra("poid", poData1.getPoId());
                            intent.putExtra("paymentdetailId", Paymentdetailid);
                            intent.putExtra("Remark",message);
                            intent.putExtra("total",total);
                            intent.putExtra("flage",flage);
                            intent.putExtra("topupflag",topupflag);
                            intent.putExtra("prodterm", CommonModule.getUserpath() + "paypalweb_process.do?poId=" + poData1.getPoId() + "&paymentDetailId=" + Paymentdetailid);
                            startActivity(intent);
                        }

                    }else {
                        try {
                            cartList = cartModule.getCartList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        PomEntity poListModel = new PomEntity();
                        String Paymentdetailid = payURLList.get(position).getPaymentDetailId();
                        String ref_No = UUID.randomUUID().toString();
                        PoEntity poData = new PoEntity();
                        poData.setCustomerId(custid);
                        poData.setPoNo(ref_No);
                        poData.setPoStatus("A");
                        poData.setPayResponse("0");
                        poData.setCouponType("C");
                        poListModel.setPo(poData);

                        ArrayList pomodellist = new ArrayList<PoDetailmEntity>();

                        for (CartEntity cartData : cartList) {
                            PoDetailEntity poDetailData = new PoDetailEntity();
                            poDetailData.setCampaignId(cartData.getCampaignId());
                            poDetailData.setCampaignQty(cartData.getProductQty());

                            PoDetailmEntity poDetailModel = new PoDetailmEntity();
                            poDetailModel.setPodetail(poDetailData);
                            pomodellist.add(poDetailModel);
                        }


                        poListModel.setPodetails(pomodellist);

                        PoEntity poData1 = null;
                        try {
                            poData1 = paymentModule.DownloadChargeCouponFromServer(poListModel);
                        } catch (APIServerException e) {
                            Toast.makeText(getApplicationContext(),"Error--"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (poData1 != null) {
                            Intent intent = new Intent(getApplicationContext(), PrivacyActivity.class);
                            intent.putExtra("flag", "paypal");
                            intent.putExtra("poid", poData1.getPoId());
                            intent.putExtra("total",total);
                            intent.putExtra("paymentdetailId", Paymentdetailid);
                            intent.putExtra("prodterm", CommonModule.getUserpath() + "paypalweb_process.do?poId=" + poData1.getPoId() + "&paymentDetailId=" + Paymentdetailid);
                            startActivity(intent);
                        }
                    }
                    break;

                case "Cash":
                    if (skip != null && skip.equals("skip")) {
                        Customer_Module usrmodule = new Customer_Module(getApplicationContext());
                        CustomerEntity res = null;
                        try {
                            res = usrmodule.getUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        scanIntent = new Intent(MyScanActivity.this, CashConformActivity.class);
                        scanIntent.putExtra("total", totl);
                        scanIntent.putExtra("email", res.getCustomerEmail());
                        scanIntent.putExtra("transfer_id", res.getCustomerId());
                        scanIntent.putExtra("staff_name", res.getCustomerFirstName());
                        scanIntent.putExtra("mobile", res.getCustomerMobile());
                        scanIntent.putExtra("topup", "cart");
                        scanIntent.putExtra("total", total);
                        startActivity(scanIntent);
                    } else {
                        scanIntent = new Intent(MyScanActivity.this, AgentCashActivtiy.class);
                        scanIntent.putExtra("total", total);
                        scanIntent.putExtra("topup", "cart");
                        startActivity(scanIntent);
                    }
                    break;
            }
    }


    public void promerceDollar() {
        WalletmEntity walletModel;
        try {
            if (wallet !=  null && cartModule.getTotalcart() > Double.parseDouble(wallet.getWalletCredit())) {
                alertmessage(getString(R.string.balance_alert));
            } else {
                walletModel = walletModule.getwalletQRfromserver(custid);

                if (walletModel != null) {
                    Intent intent = new Intent(getApplicationContext(), PromerceDollarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("pay_succe", "pay_succe");
                    intent.putExtra("transfer_id", custid);
                    intent.putExtra("qrcode", walletModel.getWallet().getQrCode());
                    intent.putExtra("total",cartModule.getTotalcart());
                    intent.putExtra("topup", topup);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), PromerceDollarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("pay_fail", "pay_fail");
                    intent.putExtra("topup", topup);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MyScan Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.mezzofy.mojodomo.mojo/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MyScan Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.mezzofy.mojodomo.mojo/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        CardRes cardRes = null;

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(MyScanActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            cardRes = new CardRes();

            Customer_Module usrmodule = new Customer_Module(getApplicationContext());
            CustomerEntity tblUser = null;
            try {
                tblUser = usrmodule.getUser();
            } catch (Exception e) {
                e.printStackTrace();
            }

            cardRes.setCard_number(params[0]);
            cardRes.setExpiry_date(params[1]);
            cardRes.setCvv2(params[2]);
            cardRes.setKey(params[3]);
            cardRes.setMerchant_id(params[4]);

            try {
                cardRes.setAmount(cartModule.getTotalcart());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                cardRes.setEmail(URLEncoder.encode(tblUser.getCustomerEmail(), "UTF-8").toString());
                cardRes.setReturn_url(URLEncoder.encode(CommonModule.getCardpayment(), "UTF-8").toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cardRes.setOrder_number(randomno);

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

            Intent scanIntent = new Intent(getApplicationContext(), PaymentActivity.class);
            scanIntent.putExtra("randomno", randomno);
            try {
                scanIntent.putExtra("total", cartModule.getTotalcart());
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanIntent.putExtra("paytype", "cardpay");
            scanIntent.putExtra("cardRes", cardRes);
            scanIntent.putExtra("url", url);
            scanIntent.putExtra("topup", topup);
            scanIntent.putExtra("transfer_id", transfer_id);
            startActivity(scanIntent);
            overridePendingTransition(0, 0);
        }
    }


    public class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            listview.setClickable(false);
            listview.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            try {
                payList = paymentModule.getpaymentdetail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            progress.setVisibility(View.INVISIBLE);
            listview.setClickable(true);
            listview.setEnabled(true);

            if (MyScanActivity.this != null)
                if (!MyScanActivity.this.isFinishing()) {

                    if (payList != null && payList.size() != 0) {
                        payURLList=new ArrayList<>();
                        for (PaymentDetailEntity res: payList) {
                            if (res.getPaymentStatus() != null && res.getPaymentStatus().equals("A")) {
                                payURLList.add(res);
                            }
                        }
                        if (staffRes.getUserType() != null && staffRes.getUserType().equals("S")) {
                            PaymentDetailEntity resval = new PaymentDetailEntity();
                            resval.setPaymentName("Cash");
                            resval.setPaymentType("Cash");
                            payURLList.add(resval);
                        }

                        listview.setAdapter(new MyScanAdapter(getApplicationContext(), payURLList));
                    }
                }
//
//            PaymentlistRes resval = paymentDao.getPaymentbyid();
//
//            if (resval != null) {
//                if (resval.getProductionSandbox().equals("Sandbox")) {
//
//                    CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
//                    CONFIG_CLIENT_ID = resval.getSandboxKey();
//                } else if (resval.getProductionSandbox().equals("Production")) {
//
//                    CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
//                    CONFIG_CLIENT_ID = resval.getProductionKey();
//                }
//
//                config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID);
//
//                Intent intent = new Intent(MyScanActivity.this, PayPalService.class);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                startService(intent);
//            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //PAYPAL
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
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
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("Msg", Msg);
                            editor.putString("RespNo", RespNo);
                            editor.putString("RefNo", RefNo);
                            editor.putString("Respcode", Respcode);
                            editor.commit();
                            if (Respcode.equals("0")) {
                                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_succe", "pay_succe");
                                intent.putExtra("transfer_id", transfer_id);
//                                intent.putExtra("msg_text", msg_text);
//                                intent.putExtra("remark", remark);
                                intent.putExtra("topup", topup);
                                intent.putExtra("total", total);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_fail", "pay_fail");
                                intent.putExtra("topup", topup);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                common.Snackbar(layout, "An invalid MZPayment");
            }
        }

        //card IO
        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            if (scanResult != null && requestCode == 100) {

                String year = String.valueOf(scanResult.expiryYear);

                for (PaymentDetailEntity paymentlistRes : payURLList) {
                    if (paymentlistRes.getPaymentType().equals("Red Dot")) {
                        AsyncCallWS asyncCallWS = new AsyncCallWS();
                        asyncCallWS.execute(scanResult.cardNumber, String.format("%02d", scanResult.expiryMonth) + "" + year.substring(Math.max(year.length() - 2, 0)), scanResult.cvv, paymentlistRes.getPaymentKey(), paymentlistRes.getPaymentMerchantId());
                    }
                }
            }

        } else {
            resultStr = "Scan was canceled.";
        }
    }

    public void alertmessage(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyScanActivity.this);
//        builder1.setTitle(getString(R.string.Coupon_Downloaded));
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertlog = builder1.create();
        alertlog.show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", "cart");
        startActivity(intent);
        overridePendingTransition(0, 0);
        MyScanActivity.this.finish();

    }
}



