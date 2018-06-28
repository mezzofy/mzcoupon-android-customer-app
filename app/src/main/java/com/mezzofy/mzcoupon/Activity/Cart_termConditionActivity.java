package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

public class Cart_termConditionActivity extends Activity {

    final Context context = this;

    int prodid;

    private WebView webView;

    String prodterm;
    String txttitle;
    String cartid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.termcondition);

        Bundle extras = getIntent().getExtras();
        prodid = extras.getInt("prodid");
        cartid = extras.getString("cartid");
        prodterm = extras.getString("prodterm");
        txttitle = extras.getString("txttitle");

        TextView textView = (TextView) findViewById(R.id.txtTitle);
        textView.setText(txttitle);

        //Get webview
        webView = (WebView) findViewById(R.id.webView1);

        startWebView(prodterm);

    }

    private void startWebView(String url) {

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
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
//							progressDialog = new ProgressDialog(Cart_termConditionActivity.this);
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
                        progressDialog = new ProgressDialog(Cart_termConditionActivity.this);
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

        intent = new Intent(context, Cart_ProdDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("prodid", prodid);
        intent.putExtra("cartid", cartid);

        startActivity(intent);
        overridePendingTransition(0, 0);
        Cart_termConditionActivity.this.finish();
    }
}
