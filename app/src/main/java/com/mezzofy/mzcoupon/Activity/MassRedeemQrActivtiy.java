package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.pojo.Transactioncoupon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by udhayinforios on 22/1/16.
 */
public class MassRedeemQrActivtiy extends Activity {

    RelativeLayout layout;
    final Context context = this;
    String exp, cupid;
    String ref_no;
    Transactioncoupon transactioncoupon = null;




    String masscouponstatus;
    Integer mer_id, site_id, prodId;
    AsyncCallWS task = new AsyncCallWS();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.qr_image);

            Intent intent = getIntent();
            ref_no = intent.getStringExtra("ref_no");
            masscouponstatus = intent.getStringExtra("masscouponstatus");



            TextView coupname = (TextView) findViewById(R.id.textView1);
            TextView coupno = (TextView) findViewById(R.id.textView2);
            TextView cancelredeem = (TextView) findViewById(R.id.TextViewCal);
            ImageView tximageView = (ImageView) findViewById(R.id.txtTitle);
            TextView no = (TextView) findViewById(R.id.no);

            TextView date = (TextView) findViewById(R.id.date);
            date.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new Date()));


//        coupname.setText(mer_name);
            no.setText(ref_no);

            cancelredeem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    finish();

                }
            });

            // barcode image
            Bitmap bitmap = null;
            ImageView imageView = (ImageView) findViewById(R.id.imageView1);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Merch_RedeemActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("prodid", prodid);
                    intent.putExtra("cupid", ref_no);
                    intent.putExtra("prodid", ref_no);
                    intent.putExtra("exp", exp);
                    intent.putExtra("merchid", mer_id);
                    intent.putExtra("site_id", site_id);
                    intent.putExtra("flag", "mass");

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            });

            try {

                if (!ref_no.equals("") || !ref_no.equals(null))
                    bitmap = encodeAsBitmap(ref_no, BarcodeFormat.QR_CODE, 212, 212);
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

    private static final int WHITE = 0x00FFFFFF;
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

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

//            transactioncoupon = couponModule.getRedeemTrack(cupid);
//            if (transactioncoupon != null && transactioncoupon.getStatus() != null) {
//                if (transactioncoupon.getStatus().equals("R")) {
//                    couponModule.getRedeemStockv(prodid, cupid);
//                } else {
//                    if (!task.isCancelled()) {
//                        task.doInBackground();
//                    }
//                }
//            } else {
//                if (!task.isCancelled()) {
//                    task.doInBackground();
//                }
//            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if (transactioncoupon != null) {
                if (transactioncoupon.getStatus().equals("R")) {
//                    CommonUtils common = new CommonUtils();
//                    common.Snackbar(coupname, getString(R.string.Redeemed));
//                    couponDao.updateCoupon("R", cupid);

                    Intent intent = new Intent(context, Redeem_StatusActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("prodid", prodid);
                    intent.putExtra("cupid", cupid);
                    intent.putExtra("stk", "successful");
                    intent.putExtra("exp", exp);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    MassRedeemQrActivtiy.this.finish();

                    if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                        task.cancel(true);
                }

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

}
