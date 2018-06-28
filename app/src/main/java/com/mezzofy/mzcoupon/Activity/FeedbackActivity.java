package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

public class FeedbackActivity extends Activity {

    final Context context = this;
    String emailAddress, emailSubject, emailText;
    EditText edittextEmailAddress, edittextEmailSubject, edittextEmailText;

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

            common = new CommonUtils();
            layout = (LinearLayout) findViewById(R.id.feedklayout);

            edittextEmailAddress = (EditText) findViewById(R.id.email_address);
            edittextEmailSubject = (EditText) findViewById(R.id.email_subject);
            edittextEmailText = (EditText) findViewById(R.id.email_text);

            edittextEmailAddress.setText("user@superk.com.hk");
            edittextEmailSubject.setText(R.string.Feed_Back);

            Button sharePhoto = (Button) findViewById(R.id.imageViewMail);
            sharePhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (DetectConnection.checkInternetConnection(getApplicationContext())) {
                        emailAddress = edittextEmailAddress.getText().toString();
                        emailSubject = edittextEmailSubject.getText().toString();
                        emailText = edittextEmailText.getText().toString();
                        pp.setIndeterminate(true);
                        pp.setVisibility(View.VISIBLE);

                        common.Snackbar(layout, getString(R.string.Mail_Sending));
//                        Toast spp = Toast.makeText(FeedbackActivity.this, R.string.Mail_Sending, Toast.LENGTH_LONG);
//                        spp.setGravity(Gravity.CENTER, 0, 0);
//                        spp.show();

                        //String emailAddressList[] = {emailAddress};
                        AsyncCallWS task = new AsyncCallWS(pp);
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
        Intent intent = new Intent(context, TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 6);
        startActivity(intent);
        overridePendingTransition(0, 0);
        FeedbackActivity.this.finish();
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
//					sender.sendMail( emailSubject, emailText, "noreply@mezzofy.com",
//							emailAddress);
//
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}


            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            pps.setVisibility(View.GONE);
            edittextEmailAddress.setText("");
            edittextEmailSubject.setText("");
            edittextEmailText.setText("");
            common.Snackbar(layout, getString(R.string.Mail_Sent));

            Intent intent = new Intent(context, TabViewActivtiy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("tabName", "");
            intent.putExtra("currTab", 6);
            startActivity(intent);
            overridePendingTransition(0, 0);
            FeedbackActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }
}
