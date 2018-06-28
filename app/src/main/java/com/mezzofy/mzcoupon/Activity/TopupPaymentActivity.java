
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

import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailEntity;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.WalletmEntity;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Payment_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.mezzofy.mzcoupon.Adapter.MyScanAdapter;
import com.mezzofy.mzcoupon.apputills.CommonModule;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.pojo.CardRes;
import com.mezzofy.mzcoupon.pojo.PaypalRes;
import com.mezzofy.mzcoupon.pojo.TransactionRes;
import com.mezzofy.mzcoupon.pojo.Transactiondtl;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by udhayinforios on 28/12/15.
 */
public class TopupPaymentActivity extends Activity implements AdapterView.OnItemClickListener {

    final String TAG = getClass().getName();

    private String randomno;

    private double total;

    private int tot, totl;
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    ListView listview = null;
    ArrayList<Item> items = new ArrayList<Item>();
    List<PaymentDetailEntity> payURLList = new ArrayList<>();
    List<PaymentDetailEntity> payList;

    ProgressDialog progressDialog;

    Payment_Module paymentModule;

    private ProgressBar progress;

    SharedPreferences settings;

    private Cart_Module cartModule;
    private Customer_Module userModule;

    private static String CONFIG_ENVIRONMENT;
    private static String CONFIG_CLIENT_ID;

    private static final int REQUEST_CODE_PAYMENT = 1;

    List<CartEntity> cartList = null;

    private static PayPalConfiguration config;

    String Msg, RespNo, RefNo, Respcode;

    private JSONObject jsonobj = null;
    Gson gson = new Gson();

    String url;
    CustomerEntity staffRes;

    String remark, message;

    Wallet_Module walletModule;
    String custid;

    WalletEntity wallet;

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

            walletModule = new Wallet_Module(getApplicationContext());
            userModule = new Customer_Module(getApplicationContext());
            paymentModule=new Payment_Module(getApplicationContext());
            cartModule = new Cart_Module(getApplicationContext());


            staffRes = userModule.getUser();
            wallet = walletModule.getwalletdetail(custid);

            cartList = cartModule.getCartList();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                randomno = CommonModule.uniqueid().replace("-", "").toUpperCase();
                total = extras.getDouble("total");
                remark = extras.getString("remark", null);
                tot = (int) (total * 100);
                totl = (int) (total);
                message = extras.getString("message");
            }

//        CONTENT = getApplicationContext().getResources().getStringArray(R.array.Cards_Pay);

            paymentModule = new Payment_Module(getApplicationContext());
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
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Intent scanIntent;
        url = payURLList.get(position).getPaymentUrl();
        switch (payURLList.get(position).getPaymentType()) {

            case "Red Dot":
                scanIntent = new Intent(this, CardIOActivity.class);
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


            case "Paypal":
                String ref_no = UUID.randomUUID().toString();
                TransactionRes Res = new TransactionRes();
                Res.setTransactionNo(ref_no);
                Res.setPayReceipt("");
                Res.setPayResponse("");
                Res.setTransactionId(0);
                Res.setCouponType("C");
                Res.setPlaceId(staffRes.getMerchantId());
                Res.setCustomerId(staffRes.getCustomerId());
                Res.setTransactionTotal((double) totl);
                Res.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()).toString());
                Res.setHashCode("0");
                Res.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()).toString());
                Res.setStatus("A");
                Res.setTransferId("");
                Res.setTransferRemark("");

                List<Transactiondtl> transdtls = new ArrayList<Transactiondtl>();
                if (cartList != null && cartList.size() > 0)
                    for (CartEntity res : cartList) {
//                        Transactiondtl transd = new Transactiondtl();
////                        transd.setProdId(res.getProdId());
//                        transd.setProdName(res.getCampaignName());
//                        transd.setProdPrice(res.getOrginalPrice());
//                        transd.setProdQty((double) res.getProductQty());
//                        transd.setProdTotal(res.getTotalPrice());
//                        transdtls.add(transd);
                    }

//                Res.setTransdtls(transdtls);
//
//                TransactionRes response = module.postPaypal(Res);

//                if (response.getTransactionId() != null) {
//                    Intent intent = new Intent(getApplicationContext(), PrivacyActivity.class);
//                    intent.putExtra("flag", "topuppaypal");
//                    intent.putExtra("prodterm",  "paypal_process.do?transactionId=" + response.getTransactionId());
//                    startActivity(intent);
//                }

                break;

            case "Cash":
                Customer_Module usrmodule = new Customer_Module(getApplicationContext());
                CustomerEntity res = null;
                try {
                    res = usrmodule.getUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scanIntent = new Intent(TopupPaymentActivity.this, CashConformActivity.class);
                scanIntent.putExtra("total", total);
                scanIntent.putExtra("email", res.getCustomerEmail());
                scanIntent.putExtra("transfer_id", res.getCustomerId());
                scanIntent.putExtra("staff_name", res.getCustomerFirstName());
                scanIntent.putExtra("mobile", res.getCustomerMobile());
                scanIntent.putExtra("topup", "topup");
                startActivity(scanIntent);
                break;

            case "MZWallet":
                promerceDollar();
                break;
        }
    }

    public void promerceDollar() {
        WalletmEntity walletModel;
        if (total > Double.parseDouble(wallet.getWalletCredit())) {
            alertmessage(getString(R.string.balance_alert));
        } else {
            walletModel = walletModule.getwalletQRfromserver(custid);

            if (walletModel != null ) {
                Intent intent = new Intent(getApplicationContext(), PromerceDollarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pay_succe", "pay_succe");
//                intent.putExtra("transfer_id", custid);
                intent.putExtra("qrcode", walletModel.getWallet().getQrCode());
                intent.putExtra("remark", remark);
                intent.putExtra("total", total);
                intent.putExtra("topup", "topup");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else {
                Intent intent = new Intent(getApplicationContext(), PromerceDollarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pay_fail", "pay_fail");
                intent.putExtra("remark", remark);
                intent.putExtra("total", total);
                intent.putExtra("topup", "topup");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
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
                                intent.putExtra("remark", remark);
                                intent.putExtra("tabview", "topup");
                                intent.putExtra("topup", "topup");
                                intent.putExtra("total", total);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                this.finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_fail", "pay_fail");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                this.finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                CommonUtils.Snackbar(listview, "An invalid MZPayment");
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

//    private PayPalPayment getThingToBuy(String paymentIntent) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        return new PayPalPayment(new BigDecimal(total), "HKD", "MZPayment", paymentIntent);
//    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        CardRes cardRes = null;

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(TopupPaymentActivity.this);
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

            cardRes.setAmount(total);

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
            scanIntent.putExtra("total", total);
            scanIntent.putExtra("paytype", "cardpay");
            scanIntent.putExtra("cardRes", cardRes);
            scanIntent.putExtra("url", url);
            scanIntent.putExtra("remark", remark);
            scanIntent.putExtra("topup", "topup");
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

            payList = null;
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

            if (TopupPaymentActivity.this != null)
                if (!TopupPaymentActivity.this.isFinishing()) {

                    if (payList != null && payList.size() != 0) {
                        for (PaymentDetailEntity res : payList) {

                            if (res != null && res.getPaymentStatus().equals("A")) {
                                if (res.getPaymentName() != null && !res.getPaymentName().equals("Promerce Dollar"))
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
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    public void alertmessage(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(TopupPaymentActivity.this);
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
        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 3);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }
}

