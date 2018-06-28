package com.mezzofy.mzcoupon.Activity;


import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.pojo.CardRes;

import com.mezzofy.mzcoupon.apputills.CommonModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PaymentActivity extends Activity {

    private WebView webView;

    private String randomno, paytype, paykey, merhid;

    private double total;

    private int tot, transfer_id;

    CustomerEntity tblUser;

    String msg_text, remark, topup;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.payment);

            Customer_Module usrmodule = new Customer_Module(getApplicationContext());
            tblUser = usrmodule.getUser();

            Bundle extras = getIntent().getExtras();
            randomno = extras.getString("randomno").replace("-", "");
            total = extras.getDouble("total");
            tot = (int) (total * 100);
            paytype = extras.getString("paytype");
            paykey = extras.getString("paykey");
            merhid = extras.getString("merhid");
            url = extras.getString("url");
            remark = extras.getString("remark");
            topup = extras.getString("topup");

            msg_text = extras.getString("msg_text", null);
            transfer_id = extras.getInt("transfer_id", 0);

            Intent intent = getIntent();
            CardRes res = (CardRes) intent.getSerializableExtra("cardRes");
            //Get webview
            webView = (WebView) findViewById(R.id.webView1);

            try {
                if (paytype.equals("enets"))
                    startWebView(url+"?order_number=" + randomno + "&merchant_id=" + merhid + "&key=" + paykey + "&amount=" + tot + "&currency_code=SGD&email=" + tblUser.getCustomerEmail() + "&transaction_type=Sale&return_url=" + URLEncoder.encode(CommonModule.getCardpayment(), "UTF-8").toString());
                else if (paytype.equals("unionpay"))
                    startWebView(url+"?order_number=" + randomno + "&merchant_id=" + merhid + "&key=" + paykey + "&amount=" + tot + "&currency_code=SGD&email=" + tblUser.getCustomerEmail() + "&transaction_type=Sale&return_url=" + URLEncoder.encode(CommonModule.getCardpayment(), "UTF-8").toString());
                else if (paytype.equals("Alipay"))
//                startWebView(url + "?order_number=" + randomno + "&merchant_id=" + merhid + "&key=" + paykey + "&amount=" + tot + "&currency_code=SGD&email=" + tblUser.getEmail() + "&transaction_type=Sale&return_url=" + URLEncoder.encode(CommonModule.getCardpayment(), "UTF-8").toString());
                    startWebView(url + "?order_number=" + randomno + "&merchant_id=" + merhid + "&key=" + paykey + "&amount=" + tot + "&currency_code=SGD&email=" + tblUser.getCustomerEmail() + "&transaction_type=Sale&return_url=" + URLEncoder.encode(CommonModule.getCardpayment(), "UTF-8").toString());
                else
                    startWebView(url + "?order_number=" + res.getOrder_number() + "&merchant_id=" + res.getMerchant_id() + "&key=" + res.getKey() + "&amount=" + res.getAmount() + "&currency_code=SGD&email=" + res.getEmail() + "&transaction_type=Sale&return_url=" + res.getReturn_url() + "&card_number=" + res.getCard_number() + "&expiry_date=" + res.getExpiry_date() + "&cvv2=" + res.getCvv2());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TextView back = (TextView) findViewById(R.id.imageView1);
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getApplicationContext(), MyScanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("randomno", CommonModule.uniqueid());
                    intent.putExtra("total", total);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    PaymentActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String link, Msg, RespNo, RefNo, Respcode, error_code = null;
                boolean send = false;
                Uri uri = Uri.parse(url);

                link = uri.toString().substring(0, uri.toString().indexOf("?"));

                if (link != null) {
                    Msg = uri.getQueryParameter("result");
                    RespNo = uri.getQueryParameter("order_number");
                    RefNo = uri.getQueryParameter("confirmation_code");
                    error_code = uri.getQueryParameter("error_code");

                    if (link.equals("http://www.mezzofy.com/")) {
                        if (Msg != null) {

                            if (Msg.equals("Paid"))
                                Respcode = "0";
                            else
                                Respcode = error_code;

                            if (RefNo == null)
                                RefNo = "";

                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("Msg", Msg);
                            editor.putString("RespNo", RespNo);
                            editor.putString("RefNo", RefNo);
                            editor.putString("Respcode", Respcode);
                            editor.commit();

                            if (Respcode.equals("0")) {

                                editor.putString("Msg", "approved");
                                editor.putString("RespNo", RespNo);
                                editor.putString("RefNo", RefNo);
                                editor.putString("Respcode", Respcode);
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_succe", "pay_succe");
                                intent.putExtra("transfer_id", transfer_id);
                                intent.putExtra("msg_text", msg_text);
                                intent.putExtra("total", total);
                                intent.putExtra("tabview", "topup");
                                intent.putExtra("topup", topup);
                                intent.putExtra("message", remark);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                PaymentActivity.this.finish();
                            } else {

                                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("pay_fail", "pay_fail");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                PaymentActivity.this.finish();
                            }
                        }
                    }
                } else {
                    send = true;
                }

                view.loadUrl(url);
                return send;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (!isFinishing()) {
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(PaymentActivity.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), MyScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("randomno", CommonModule.uniqueid());
            intent.putExtra("total", total);

            startActivity(intent);
            overridePendingTransition(0, 0);
            PaymentActivity.this.finish();
        }
    }
}