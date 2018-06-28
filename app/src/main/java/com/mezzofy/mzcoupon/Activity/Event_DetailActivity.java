package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

/**
 * Created by LENOVO on 16/06/2015.
 */
public class Event_DetailActivity extends Activity {

    final Context context = this;
    String prodterm;
    private WebView webView;
    String txttitle;

    RelativeLayout layout;
    CommonUtils common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.event_webpage);

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            Bundle extras = getIntent().getExtras();
            prodterm = extras.getString("prodterm");

            TextView textView = (TextView) findViewById(R.id.TextView01);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

//                    intent = new Intent(context, EventActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    Event_DetailActivity.this.finish();
                }
            });

            //Get webview
            webView = (WebView) findViewById(R.id.webView1);

            if (DetectConnection.checkInternetConnection(context))
                startWebView(prodterm);
            else
                common.Snackbar(layout, getString(R.string.No_Internet));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                boolean send = false;
                Uri uri = Uri.parse(url);

                view.loadUrl(url);
                return send;
            }

            //            //Show loader on url load
//            public void onLoadResource (WebView view, String url) {
//                if (progressDialog == null) {
//                    // in standard case YourActivity.this
//                    progressDialog = new ProgressDialog(Event_DetailActivity.this);
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//                }
//            }
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
                        progressDialog = new ProgressDialog(Event_DetailActivity.this);
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

//        intent = new Intent(context, EventActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//        Event_DetailActivity.this.finish();
    }


    @Override
    public void onLowMemory() {
        // use it only for older API version
        super.onLowMemory();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}
