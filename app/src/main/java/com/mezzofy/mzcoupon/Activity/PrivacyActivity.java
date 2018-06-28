package com.mezzofy.mzcoupon.Activity;

import com.mezzofy.mzcoupon.Entity.WalletTxnEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.Entity.WalletmEntity;
import com.mezzofy.mzcoupon.Entity.WalletTxnmEntity;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.WalletTranscation_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class PrivacyActivity extends Activity {

    final Context context = this;

    String prodterm, txttitle, flag,remark,PayToken,topupflag,staffid;

    private WebView webView;
    private double total;
    private int tot, totl;
    private Merchant_Module merchantModule;

    SharedPreferences settings;

    private WalletTranscation_Module wallettransactionmodule;
    private Wallet_Module walletModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.termcondition);

            wallettransactionmodule=new WalletTranscation_Module(getApplicationContext());
            walletModule=new Wallet_Module(getApplicationContext());

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffid = settings.getString("staff_id", null);

            Bundle extras = getIntent().getExtras();
            prodterm = extras.getString("prodterm");
            txttitle = extras.getString("txttitle");
            flag = extras.getString("flag");
            remark=extras.getString("Remark");
            PayToken=extras.getString("paytoken");
            topupflag=extras.getString("topupflag");

            total = extras.getDouble("total");
            tot = (int) (total * 100);
            totl = (int) (total);


            Log.d("paymentpaypal",prodterm);

            TextView textView = (TextView) findViewById(R.id.txtTitle);

            if (flag != null && flag.equals("paypal") || topupflag.equals("topupflag")) {
                textView.setText(getString(R.string.Payment_title));
            } else {
                textView.setText(txttitle);
            }

            //Get webview
            webView = (WebView) findViewById(R.id.webView1);

            if (DetectConnection.checkInternetConnection(getApplicationContext()))
                startWebView(prodterm);
            else
                Toast.makeText(getApplicationContext(), getString(R.string.No_Internet), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startWebView(String url) {

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                boolean send = false;
//                Log.d("url",url);
//                return send;
//            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {

                    Intent intent = null;

                    Log.d("url",url);
                    if (flag.equals("paypal") || topupflag.equals("topuppaypal"))
                        if (url.contains("paypalweb_success.do")) {
                            intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("pay_succe", "pay_succe");

                            if (url.contains("paypalweb_success.do") && flag.equals("paypal") && topupflag==null)
                                intent.putExtra("topup", "Paypal");
                            else if (url.contains("paypalweb_success.do") && topupflag.equals("topuppaypal")) {
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                WalletTxnEntity walletTxnData=new WalletTxnEntity();
                                walletTxnData.setCustomerId(settings.getString("staff_id", null));
                                walletTxnData.setTransactionType(3);
                                walletTxnData.setPaymentResponse(0);
                                walletTxnData.setTransactionNotes(remark);
                                walletTxnData.setTransactionAmount(total);
                                if(PayToken!=null)
                                    walletTxnData.setTransactionPayRef(PayToken);
                                else
                                    walletTxnData.setTransactionPayRef("0");

                                WalletTxnmEntity wallettxnmodel=new WalletTxnmEntity();
                                wallettxnmodel.setWallettxn(walletTxnData);

                                WalletTxnmEntity res=wallettransactionmodule.PostWallettxnToServer(wallettxnmodel);

                                if(res!=null) {
                                    WalletmEntity walletModel = walletModule.getWalletlistfromserver(staffid);
                                    if (walletModel != null)
                                        walletModule.addWallet(walletModel.getWallet());
                                }

                                intent.putExtra("topup", topupflag);
                            }

                            flag = "finish";
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        } else if (url.contains("paypalweb_failure.do")) {
                            intent = new Intent(getApplicationContext(), PromerceDollarActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("pay_fail", "pay_fail");

                            if (url.contains("paypalweb_failure.do") && flag.equals("paypal"))
                                intent.putExtra("topup", "Paypal");
                            else if (url.contains("paypalweb_failure.do") && flag.equals("topuppaypal"))
                                intent.putExtra("topup", "topup");

                            flag = "finish";
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                // TODO Auto-generated method stub
//                super.onPageStarted(view, url, favicon);
//
//                try {
//                    if (progressDialog == null) {
//                        // in standard case YourActivity.this
//                        progressDialog = new ProgressDialog(PrivacyActivity.this);
//                        progressDialog.setMessage("Loading...");
//                        progressDialog.show();
//
//                    }
//                    message = url;
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
        });
    }


    @Override
    public void onBackPressed() {
//               // Write your code here
        super.onBackPressed();
        PrivacyActivity.this.finish();

//        Intent it=new Intent(getApplicationContext(),SettingActivity.class);
//        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(it);
//        RegisterActivity.this.finish();
    }


}