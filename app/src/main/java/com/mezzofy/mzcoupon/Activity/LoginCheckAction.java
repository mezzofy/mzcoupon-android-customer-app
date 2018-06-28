package com.mezzofy.mzcoupon.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.apputills.FontsOverride;


public class LoginCheckAction extends Activity {

    final Context context = this;
    private Customer_Module usrmodule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        usrmodule = new Customer_Module(LoginCheckAction.this);

        FontsOverride.setDefaultFont(this, "DEFAULT", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Roboto-Light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Roboto-Light.ttf");

        CustomerEntity tblUser = null;
        try {
            tblUser = usrmodule.getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tblUser != null) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("staff_id", tblUser.getCustomerId());
            editor.putString("user_name", tblUser.getCustomerFirstName());
            editor.commit();
            String profill = settings.getString("profill", "P");
            Intent intent;
//            if (profill.equals("A")) {
                intent = new Intent(context,ProgressActivity.class);
//            } else {
//                intent = new Intent(context, UserProfileActivity.class);
//            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
            LoginCheckAction.this.finish();
        } else {
            Intent intent1 = null;

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String agree = settings.getString("user_agree", "no");

            if (agree.equals("agree"))
                intent1 = new Intent(context, WelcomeActivity.class);
            else
                intent1 = new Intent(context, AgreeActivity.class);

            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent1);
            overridePendingTransition(0, 0);
            LoginCheckAction.this.finish();
        }
    }


}
   

    
