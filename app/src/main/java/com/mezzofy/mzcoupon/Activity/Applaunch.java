package com.mezzofy.mzcoupon.Activity;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.facebook.FacebookSdk;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.DragModule;
import com.mezzofy.mzcoupon.module.Setting_Module;

import java.util.List;
import java.util.UUID;
import io.branch.referral.Branch;

/**
 * Created by LENOVO on 06/08/2015.
 */
public class Applaunch extends Application {

    private BeaconManager beaconManager;
    private Beacon nearestBeacon = null;
    private Region region;
    private String userId=null;
    private Setting_Module settingModule;
    public static DragModule moddrag;

    public static String ErrorMessage=null;
    public static String MerchantId="dmha";
//    public static String MerchantId="biyz";



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        MultiDex.install(this);

        FacebookSdk.sdkInitialize(this);


        settingModule=new Setting_Module(getApplicationContext());
        try {
            userId=settingModule.getSettings("User");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Branch branch = Branch.getInstance(getApplicationContext());

        EstimoteSDK.initialize(getApplicationContext(), "com.mezzofy.coupon.getso-lka", "864532236c372ef1be9117cc5f4ab53b");

        beaconManager = new BeaconManager(getApplicationContext());
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
//                beaconManager.startMonitoring(new Region("monitored region",
//                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null,null));
                beaconManager.startMonitoring(region);
            }
        });


        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                showNotification("MZCoupon", "MZCoupon STOP");
            }

            @Override
            public void onExitedRegion(Region region) {
               // showNotification("Getso", "GetSo Promo");
            }
        });

        moddrag = new DragModule(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
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



//    public static void deleteCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            deleteDir(dir);
//        } catch (Exception e) {}
//    }

//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//            return dir.delete();
//        } else if(dir!= null && dir.isFile()) {
//            return dir.delete();
//        } else {
//            return false;
//        }
//    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = null;
        if(userId != null) {
            notifyIntent = new Intent(this, TabViewActivtiy.class);
            notifyIntent.putExtra("tabName", "");
            notifyIntent.putExtra("currTab", 2);
        } else {
            notifyIntent = new Intent(this, WelcomeActivity.class);
        }
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.appicon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}