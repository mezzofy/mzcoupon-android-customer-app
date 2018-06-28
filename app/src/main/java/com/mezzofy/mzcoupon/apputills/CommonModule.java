package com.mezzofy.mzcoupon.apputills;

import com.mezzofy.mzcoupon.Activity.Applaunch;

import java.util.UUID;


public class CommonModule {

    public static String getlocal() {
        return "http://172.16.25.16:8080/admin/";
    }

    public static String getplatformpath() {
//        return "http://platform.mezzofy.com/api/v1/";
        return "http://platform.uat.mezzofy.com/api/v1/";
    }

    public static String getUserpath() {
//        return "http://"+ Applaunch.MerchantId+".mezzofy.com/";
          return "http://"+ Applaunch.MerchantId+".uat.mezzofy.com/";
    }

    public static String getKey() {

//        return "3TN1FZ";
        return "NPNN10";


    }

    public static String getSecretkey() {

        return "x";
    }

    public static String getCardpayment() {
        String retval = null;
        try {
            retval = TripleDES.decrypt(AppConstants.car);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }


    public synchronized static String uniqueid() {
        String uniqueID = null;

        uniqueID = UUID.randomUUID().toString();

        return uniqueID;
    }

}
