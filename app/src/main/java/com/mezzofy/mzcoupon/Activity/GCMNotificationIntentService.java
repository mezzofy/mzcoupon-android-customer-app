package com.mezzofy.mzcoupon.Activity;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mezzofy.mzcoupon.R;

public class GCMNotificationIntentService extends IntentService {

    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        String extras = intent.getStringExtra("collapse_key");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.equals("")) {

            String id = intent.getStringExtra("MZCoupon Id:");
            String trk = intent.getStringExtra("Track :");

            if (id != null && !id.equals("")) {
                try {
                    sendNotification(URLDecoder.decode(extras, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendNotificationnotifi(URLDecoder.decode(extras, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotificationnotifi(String msg) {
//		Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap smallBitmap = null;
        BufferedInputStream buf = null;

        try {
            buf = new BufferedInputStream(getAssets().open("appicon.pngg"));
            // Create the bitmap to be set in notification.
            smallBitmap = BitmapFactory.decodeStream(buf);
            buf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Creates an Intent for the Activity
//        Intent notifyIntent = new Intent(this, EventActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.appicon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setTicker("MZCoupon")
                .setContentTitle("MZCoupon")
                        //.setSubText(text)
                .setLargeIcon(smallBitmap)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        //.addAction(android.R.drawable.sym_action_chat,"Open EB app", contentIntent);

//        mBuilder.setContentIntent(contentIntent);
        NOTIFICATION_ID = randInt(1, 50);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//		Log.d(TAG, "MZNotification sent successfully.");
    }

    private void sendNotification(String msg) {
//		Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap smallBitmap = null;
        BufferedInputStream buf = null;

        try {
            buf = new BufferedInputStream(getAssets().open("appicon.pngg"));

            // Create the bitmap to be set in notification.
            smallBitmap = BitmapFactory.decodeStream(buf);
            buf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Creates an Intent for the Activity
        Intent notifyIntent = new Intent(this, TabViewActivtiy.class);
        notifyIntent.putExtra("tabName", "");
        notifyIntent.putExtra("currTab", 2);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.appicon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setTicker("MZCoupon")
                .setContentTitle("MZCoupon")
                        //.setSubText(text)
                .setLargeIcon(smallBitmap)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        //.addAction(android.R.drawable.sym_action_chat,"Open EB app", contentIntent);

        mBuilder.setContentIntent(contentIntent);
        NOTIFICATION_ID = randInt(1, 50);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//		Log.d(TAG, "MZNotification sent successfully.");
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
