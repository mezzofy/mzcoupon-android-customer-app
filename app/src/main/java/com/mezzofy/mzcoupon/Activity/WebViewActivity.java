package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;

/**
 * Created by aruna on 6/26/18.
 */

public class WebViewActivity extends Activity {
    String url;
    private WebView Webview;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);


        Webview= (WebView) findViewById(R.id.Webview);

        if (DetectConnection.checkInternetConnection(getApplicationContext()))
            startWebView("http://www.mezzofy.com/test/events");
        else
            Toast.makeText(getApplicationContext(),getString(R.string.No_Internet), Toast.LENGTH_SHORT)
                    .show();



    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        Webview.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                boolean send = false;
                Uri uri = Uri.parse(url);

                view.loadUrl(url);
                return send;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(WebViewActivity.this);
                    progressDialog.setMessage("Loading...");
                    //progressDialog.show();
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
        Webview.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        Webview.loadUrl(url);

    }

}
