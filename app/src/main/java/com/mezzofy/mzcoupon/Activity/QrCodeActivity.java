package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.WalletmEntity;
import com.mezzofy.mzcoupon.module.Wallet_Module;

import java.util.EnumMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QrCodeActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;
    private Wallet_Module walletModule;
    WalletEntity wallet;
    String custid;
    String custname;
    TextView coupname;
    private int progressStatus = 60;
    private int barStatus = 0;
    private Handler handler = new Handler();
    ProgressBar progress;
    AsyncCallWS task = new AsyncCallWS();
    Thread thread = null;
    Timer timer;
    TimerTask timerTask;
    Bitmap bitmap;
    ImageView imageView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_wallet_image);
        walletModule = new Wallet_Module(context);

        progress = (ProgressBar) findViewById(R.id.progress1);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        custid = settings.getString("staff_id", null);
        custname = settings.getString("user_name", "");

        WalletmEntity walletModel = walletModule.getwalletQRfromserver(custid);
//        if (walletModel != null)
//            walletModule.updateQr(walletModel.getWallet());


        coupname = (TextView) findViewById(R.id.textView1);
        TextView coupno = (TextView) findViewById(R.id.textView2);
        TextView cancelredeem = (TextView) findViewById(R.id.TextViewCal);
        ImageView tximageView = (ImageView) findViewById(R.id.txtTitle);

        coupname.setText(getString(R.string.VALID_For) + " 60 " + getString(R.string.SECONDS_For));
        coupno.setText(custname);

        cancelredeem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                QrCodeActivity.this.finish();
                if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                    task.cancel(true);
            }
        });


        // barcode image
        bitmap = null;
        imageView = (ImageView) findViewById(R.id.imageView1);

        try {
            bitmap=null;
            bitmap = encodeAsBitmap(custid + "_" + walletModel.getWallet().getQrCode(), BarcodeFormat.QR_CODE, 212, 212);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }


    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");


            progressStatus = 60;
            barStatus = 0;
            // Start long running operation in a background thread

            thread = new Thread(new Runnable() {
                public void run() {
                    while (progressStatus > 0) {

                        // Update the progress bar and display the
                        //current value in the text view
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressStatus -= 1;

                                if (progressStatus == 0) {
                                    if (!task.isCancelled()) {
                                        thread.interrupt();
                                        task.doInBackground();
                                    }
                                }
                                progress.setProgress(barStatus += 1);
                                coupname.setText(getString(R.string.VALID_For) + " " + progressStatus + " " + getString(R.string.SECONDS_For));
                            }
                        }, 1000);
                        try {
                            // Sleep for 200 milliseconds.
                            //Just to display the progress slowly
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


            });


            thread.start();

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        stoptimertask();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        progressStatus -= 1;

                        if (progressStatus == 0) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                                progressStatus = 60;
                                barStatus = 0;
                                WalletmEntity walletModel = walletModule.getwalletQRfromserver(custid);
                                if (walletModel != null)
                                    try {
                                        walletModule.updateQr(walletModel.getWallet());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                try {
                                    wallet = walletModule.getwalletdetail(custid);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {

                                    bitmap = encodeAsBitmap(custid + "_" + wallet.getQrCode(), BarcodeFormat.QR_CODE, 212, 212);
                                    if (bitmap != null) {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                                startTimer();
                            }
                        }
                        progress.setProgress(barStatus += 1);
                        coupname.setText(getString(R.string.VALID_For) + " " + progressStatus + " " + getString(R.string.SECONDS_For));
                    }
                });
            }
        };
    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
            task.cancel(true);
        QrCodeActivity.this.finish();
    }


}