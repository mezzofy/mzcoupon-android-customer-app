package com.mezzofy.mzcoupon.apputills;

import android.content.Context;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

/**
 * Created by udhayinforios on 13/6/16.
 */
public class BeconProperties {

    private static List<Beacon> beaconList = null;
    private static BeaconManager beaconManager = null;
//    private static DragModule objdrag;

    static Region region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

    public static void initBecon(Context context) {
        beaconManager = new BeaconManager(context);
//        objdrag = new DragModule(context);
    }

    public List<Beacon> getBeaconList() {

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    beaconList = list;
//                    objdrag.returnbeaconList(beaconList);
                }
            }
        });

        return beaconList;
    }

    public List<Beacon> getdragBeaconList() {

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    beaconList = list;
//                    objdrag.returndragbeaconList(beaconList);
                }
            }
        });

        return beaconList;
    }


    public static void startBeacon(Context context) {

        if (beaconManager == null)
            initBecon(context);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    public static void stopBeacon() {

        if (beaconManager != null) {
            beaconManager.stopRanging(region);
            beaconManager.disconnect();
        }
    }


}
