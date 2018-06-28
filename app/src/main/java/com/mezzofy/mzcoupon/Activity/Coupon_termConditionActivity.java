package com.mezzofy.mzcoupon.Activity;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Coupon_termConditionActivity extends Activity {

    final Context context = this;
    String campaignId;
    String prodterm, exp, expday, couponId;
    private WebView webView;
    String txttitle;
    RelativeLayout layout;
    CommonUtils common;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.termcondition);

            Bundle extras = getIntent().getExtras();
            prodterm = extras.getString("prodterm");
            expday = extras.getString("expday");
            txttitle = extras.getString("txttitle");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            TextView textView = (TextView) findViewById(R.id.txtTitle);
            textView.setText(txttitle);
            //Get webview
            webView = (WebView) findViewById(R.id.webView1);

            if (DetectConnection.checkInternetConnection(context))
                startWebView(prodterm);
            else
                common.Snackbar(layout, getString(R.string.No_Internet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startWebView(String url) {

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        webView.loadUrl(url);


        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                boolean send = false;
                Uri uri = Uri.parse(url);

                view.loadUrl(url);
                return send;
            }

//					//Show loader on url load
//					public void onLoadResource(WebView view, String url) {
//						if (progressDialog == null) {
//							// in standard case YourActivity.this
//							progressDialog = new ProgressDialog(Coupon_termConditionActivity.this);
//							progressDialog.setMessage("Loading...");
//							progressDialog.show();
//						}
//					}

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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                try {
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(Coupon_termConditionActivity.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }

        });


    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent;

        intent = new Intent(context, CouponDetail_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("expday", expday);

        startActivity(intent);
        overridePendingTransition(0, 0);
        Coupon_termConditionActivity.this.finish();
    }
}
