package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.Entity.MassCouponmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class QrCodewalnetActivity extends Activity {

    TextView coupname;
    final Context context = this;
    String exp, couponId,siteId;
    String campaignId,ref_no,masscouponstatus;
    SharedPreferences settings;

    private Campaign_Module campaignModule;
    private Coupon_Module couponModule;
    private CustomerCouponmEntity rescustomerCouponModel = null;
    private MassCouponmEntity massCouponModel=null;
    CampaignEntity campaignData = null;
    AsyncCallWS task = new AsyncCallWS();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_image);

        try {
            couponModule = new Coupon_Module(context);
            campaignModule = new Campaign_Module(context);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            campaignId = settings.getString("campaignId", null);
            couponId = settings.getString("couponId", null);
            exp = settings.getString("exp", null);



            Intent intent = getIntent();
            if(intent!=null) {
                ref_no = intent.getStringExtra("ref_no");
                masscouponstatus = intent.getStringExtra("masscouponstatus");
                siteId=intent.getStringExtra("site_id");

            }

            CouponEntity couponData = couponModule.getCoupon(couponId);
            CampaignmEntity campaignModel = campaignModule.getCampaign(campaignId);
            if(campaignModel!=null)
                 campaignData=campaignModel.getCampaign();

             coupname = (TextView) findViewById(R.id.textView1);
            TextView coupno = (TextView) findViewById(R.id.textView2);
            TextView cancelredeem = (TextView) findViewById(R.id.TextViewCal);
            ImageView tximageView = (ImageView) findViewById(R.id.txtTitle);
            TextView date = (TextView) findViewById(R.id.date);
            date.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new Date()));


            if(ref_no!=null) {
                coupno.setText(ref_no);
                coupname.setText(masscouponstatus);
            }
            else {
                coupno.setText(couponData.getCouponNo());
                coupname.setText(couponData.getCouponName());
            }

//	        if(productRes.getBrand().equals("TX")){
//	        	tximageView.setImageResource(R.drawable.tx);
//	        }

            cancelredeem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (rescustomerCouponModel != null)
                        if (!rescustomerCouponModel.getCoupon().getCouponStatus().equals("R")) {
//                            couponModule.getRelaseTrack(prodid, cupid);
                        }
                    Intent intent = new Intent(context, CouponDetail_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    QrCodewalnetActivity.this.finish();
                    if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                        task.cancel(true);
                }
            });

            // barcode image
            Bitmap bitmap = null;
            ImageView imageView = (ImageView) findViewById(R.id.imageView1);


            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Merch_RedeemActivity.class);
                    intent.putExtra("merchid", campaignData.getMerchantId());
                    intent.putExtra("ref_no", ref_no);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    QrCodewalnetActivity.this.finish();
                    if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                        task.cancel(true);
                }
            });

            try {
                bitmap=null;
                if(ref_no!=null)
                    bitmap = encodeAsBitmap(ref_no, BarcodeFormat.QR_CODE, 212, 212);
                else
                    bitmap = encodeAsBitmap(couponId, BarcodeFormat.QR_CODE, 212, 212);

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }

            //Call execute
            task.execute();

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

            if(ref_no!=null)
            {
                massCouponModel = couponModule.getMasscoupondetailfromserver(siteId,ref_no);
                if (massCouponModel != null && massCouponModel.getMasscoupon().getMassStatus() != null) {
                    if (rescustomerCouponModel.getCoupon().getCouponStatus().equals("R")) {
//                    couponModule.getRedeemStockv(prodid, cupid);
                    } else {
                        if (!task.isCancelled()) {
                            task.doInBackground();
                        }
                    }
                } else {
                    if (!task.isCancelled()) {
                        task.doInBackground();
                    }
                }
            }else {
                rescustomerCouponModel = couponModule.getcoupondetailfromserver(couponId);
                if (rescustomerCouponModel != null && rescustomerCouponModel.getCoupon().getCouponStatus() != null) {
                    if (rescustomerCouponModel.getCoupon().getCouponStatus().equals("R")) {
//                    couponModule.getRedeemStockv(prodid, cupid);
                    } else {
                        if (!task.isCancelled()) {
                            task.doInBackground();
                        }
                    }
                } else {
                    if (!task.isCancelled()) {
                        task.doInBackground();
                    }
                }
            }


            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            if(ref_no!=null){
                if (massCouponModel != null) {
                    if (massCouponModel.getMasscoupon().getMassStatus().equals("R")) {
                        CommonUtils common = new CommonUtils();
                        common.Snackbar(coupname, getString(R.string.Redeemed));
//                    couponDao.updateCoupon("R", cupid);

                        Intent intent = new Intent(context, Redeem_StatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("stk", "successful");

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        QrCodewalnetActivity.this.finish();

                        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                            task.cancel(true);
                    }

                }
            }
            else {
                if (rescustomerCouponModel != null) {
                    if (rescustomerCouponModel.getCoupon().getCouponStatus().equals("R")) {
                        CommonUtils common = new CommonUtils();
                        common.Snackbar(coupname, getString(R.string.Redeemed));
//                    couponDao.updateCoupon("R", cupid);

                        Intent intent = new Intent(context, Redeem_StatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("stk", "successful");

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        QrCodewalnetActivity.this.finish();

                        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
                            task.cancel(true);
                    }

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
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(context, CouponDetail_Activity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        QrCodewalnetActivity.this.finish();
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
            task.cancel(true);
    }
}