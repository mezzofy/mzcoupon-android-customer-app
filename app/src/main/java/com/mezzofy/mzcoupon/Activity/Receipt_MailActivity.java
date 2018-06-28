package com.mezzofy.mzcoupon.Activity;

import java.util.ArrayList;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.pojo.TransactionRes;
import com.mezzofy.mzcoupon.pojo.Transactiondtl;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

public class Receipt_MailActivity extends Activity {

    final Context context = this;

    String emailAddress, emailSubject;

    EditText edittextEmailAddress, edittextEmailSubject;
    WebView edittextEmailText;

    int transId;

    TransactionRes transactionRes = null;
    ArrayList<Transactiondtl> transList = null;

    private Customer_Module customer_module;
    double total;
    StringBuilder sb = new StringBuilder();
    SharedPreferences settings;

    LinearLayout layout;
    CommonUtils common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.feedback_mail);
            final ProgressBar pp = (ProgressBar) findViewById(R.id.progressBar1);
            pp.setVisibility(View.GONE);

            customer_module = new Customer_Module(context);

            common = new CommonUtils();
            layout = (LinearLayout) findViewById(R.id.feedklayout);

            CustomerEntity staffRes = customer_module.getUser();

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Bundle extras = getIntent().getExtras();
            transId = extras.getInt("transId");

            transList = null;



            TextView texttitle = (TextView) findViewById(R.id.txtTitle);
            texttitle.setText("Invoice Number : " + transactionRes.getTransactionId());

            edittextEmailAddress = (EditText) findViewById(R.id.email_address);
            edittextEmailSubject = (EditText) findViewById(R.id.email_subject);
            edittextEmailText = (WebView) findViewById(R.id.email_text);

            edittextEmailAddress.setText(staffRes.getCustomerEmail());
            edittextEmailSubject.setText("Invoice Number : " + transactionRes.getTransactionId());

            sb.append("<html><table style=\"width:100%;background-color:#6A7C8B;\" ><tr><td height=\"150\" align=\"center\"><img src=\"https://s3-ap-southeast-1.amazonaws.com/stgpromzgetso/logo/thumnail.png\" style=\"width:200px;\"></td></tr></table><br/></html>");
            sb.append("<html><table style=\"width:100%\"><tr><td>Invoice 發票號碼 : " + transactionRes.getTransactionId() + "</td><td align=\"right\">" + transactionRes.getCreatedOn() + "</td></tr></table></html>");
            sb.append("<html><hr>\n</html>");

            for (int i = 0; i < transList.size(); i++) {
                if (settings.getString("decimal", "N").equals("Y")) {
                    sb.append("<html><table style=\"width:100%\"><tr><td>" + transList.get(i).getProdName() + " x " + transList.get(i).getProdQty().intValue() + "</td><td align=\"right\"> " + settings.getString("currency", "IDR") + String.format("%,.2f", transList.get(i).getProdTotal()) + "</td></tr></table></html>");
                } else {
                    sb.append("<html><table style=\"width:100%\"><tr><td>" + transList.get(i).getProdName() + " x " + transList.get(i).getProdQty().intValue() + "</td><td align=\"right\"> " + settings.getString("currency", "IDR") + String.format("%,.0f", transList.get(i).getProdTotal()) + "</td></tr></table></html>");

                }
                total = total + transList.get(i).getProdTotal();
            }

            sb.append("<html><hr>\n</html>");
            sb.append("<html><table style=\"width:100%\"><tr><td><b>Total 總金額</b></td><td align=\"right\"><b> $" + total + "</b></td></tr></table></html>");

            sb.append("<html><table style=\"width:100%;background-color:#6A7C8B;\"><tr><td height=\"30\" align=\"center\"><font size=\"4\">2015 &copy; Edenred HK</font></td></tr></table><br/></html>");

            edittextEmailText.getSettings().setJavaScriptEnabled(true);
            edittextEmailText.loadDataWithBaseURL("", sb.toString(), "text/html", "UTF-8", "");


            Button sharePhoto = (Button) findViewById(R.id.imageViewMail);
            sharePhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (DetectConnection.checkInternetConnection(getApplicationContext())) {
                        emailAddress = edittextEmailAddress.getText().toString();
                        emailSubject = edittextEmailSubject.getText().toString();
                        //emailText = edittextEmailText.getText().toString();

                        pp.setIndeterminate(true);
                        pp.setVisibility(View.VISIBLE);

                        common.Snackbar(layout, getString(R.string.Mail_Sending));
//                        spp.setGravity(Gravity.CENTER, 0, 0);
//                        spp.show();

                        //String emailAddressList[] = {emailAddress};
                        AsyncCallWS task = new AsyncCallWS(pp);
                        //Call execute
                        task.execute();
                    } else {
                        common.Snackbar(layout, getString(R.string.No_Internet));
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(context, PaymentTranscationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        overridePendingTransition(0, 0);
        Receipt_MailActivity.this.finish();
    }


    public class AsyncCallWS extends AsyncTask<String, Void, Void> {
        ProgressBar pps;

        public AsyncCallWS(ProgressBar pp) {
            // TODO Auto-generated constructor stub
            this.pps = pp;
        }

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            Log.i("TAG", "onPostExecute");
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");


//			 GMailSender sender = new GMailSender();
//		    	try {
//
//
//		    		emailText =  sb.toString();
//					sender.sendMail( emailSubject, emailText, "noreply@mezzofy.com",
//							emailAddress);
//
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            pps.setVisibility(View.GONE);
            edittextEmailAddress.setText("");
            edittextEmailSubject.setText("");
            //edittextEmailText.setText("");
            common.Snackbar(layout, getString(R.string.Mail_Sent));

            Intent intent = new Intent(context, TabViewActivtiy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("tabName", "");
            intent.putExtra("currTab", 4);
            startActivity(intent);
            overridePendingTransition(0, 0);
            Receipt_MailActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }
}
