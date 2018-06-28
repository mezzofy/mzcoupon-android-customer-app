package com.mezzofy.mzcoupon.Activity;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

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

public class TermConditionActivity extends Activity {

    final Context context = this;
    int specialId, prodid;
    String specialImg, specialname, prodterm;
    private WebView webView;
    String prodfav;
    String txttitle;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.termcondition);

        Bundle extras = getIntent().getExtras();
        prodid = extras.getInt("prodid");
        specialId = extras.getInt("specialId");
        specialImg = extras.getString("specialimg");
        specialname = extras.getString("specialname");
        prodterm = extras.getString("prodterm");
        prodfav = extras.getString("prodfav");
        txttitle = extras.getString("txttitle");
        flag = extras.getString("flag");

        TextView textView = (TextView) findViewById(R.id.txtTitle);
        textView.setText(txttitle);

        //Get webview
        webView = (WebView) findViewById(R.id.webView1);

        if (DetectConnection.checkInternetConnection(context))
            startWebView(prodterm);
        else
            CommonUtils.Snackbar(webView, getString(R.string.No_Internet));
        } catch (Exception e){
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
//						if (i == 0) {
//							if (progressDialog == null) {
//								// in standard case YourActivity.this
//								progressDialog = new ProgressDialog(TermConditionActivity.this);
//								progressDialog.setMessage("Loading...");
//								progressDialog.show();
//							}
//						}
//						i++;
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
                        progressDialog = new ProgressDialog(TermConditionActivity.this);
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
        if(flag != null){
            finish();
        }else if (specialId != 0) {
//            intent = new Intent(context, SpecialDetail_ProdActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("prodid", prodid);
//            intent.putExtra("specialId", specialId);
//            intent.putExtra("specialimg", specialImg);
//            intent.putExtra("specialname", specialname);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            TermConditionActivity.this.finish();
        } else {
            intent = new Intent(context, Campaigndetail_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", prodid);
            intent.putExtra("prodfav", prodfav);
            startActivity(intent);
            overridePendingTransition(0, 0);
            TermConditionActivity.this.finish();
        }


    }
}
