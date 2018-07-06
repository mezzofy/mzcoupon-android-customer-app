package com.mezzofy.mzcoupon.Activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Setting_Module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static com.mezzofy.mzcoupon.R.drawable.appicon;

public class SettingActivity extends Fragment implements OnClickListener {



    private int staffid;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ListView listview = null;

    private boolean doubleBackToExitPressedOnce = false;

    private AlertDialog alertlog;
    private Customer_Module userModule;

    private Setting_Module settingModule;
    SharedPreferences settings;

    private String userType,Username;

    private Button loginbtn;
    View view;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.setting_page, null);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            userModule = new Customer_Module(getActivity());
            settingModule = new Setting_Module(getActivity());

            loginbtn = (Button) view.findViewById(R.id.button1);
            loginbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    if(userType!=null && userType.equals("G"))
                        signInAlert();
                    else
                        signOutAlert();

                }
            });


            userType=settingModule.getSettings("Type");
            Username=settingModule.getSettings("User");

            if(userType!=null && userType.equals("G"))
                loginbtn.setText(getString(R.string.Sign_In));
            else
                loginbtn.setText(getString(R.string.Logout));


            TextView usertype = (TextView)view.findViewById(R.id.txtusertype);
            TextView txtusername = (TextView)view.findViewById(R.id.txtusername);


            usertype.setText(userType);
            txtusername.setText(Username);

            LinearLayout profileClick = (LinearLayout)view.findViewById(R.id.editprofile);
            profileClick.setOnClickListener(this);
            LinearLayout changepwClick = (LinearLayout)view.findViewById(R.id.changepw);
            changepwClick.setOnClickListener(this);
            LinearLayout langClick = (LinearLayout)view.findViewById(R.id.lang);
            langClick.setOnClickListener(this);
            LinearLayout sharetheappClick = (LinearLayout) view.findViewById(R.id.sharetheapp);
            sharetheappClick.setOnClickListener(this);
            LinearLayout aboutusClick = (LinearLayout)view.findViewById(R.id.aboutus);
            aboutusClick.setOnClickListener(this);
            LinearLayout feedbackClick = (LinearLayout) view.findViewById(R.id.feedback);
            feedbackClick.setOnClickListener(this);
            LinearLayout termsofuseClick = (LinearLayout) view.findViewById(R.id.termsofuse);
            termsofuseClick.setOnClickListener(this);
            LinearLayout privacypolicyClick = (LinearLayout)view.findViewById(R.id.privacypolicy);
            privacypolicyClick.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();
        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.editprofile:
                if(!userType.equals("G")) {
                    intent = new Intent(getActivity(), EditorActivity.class);
                    startActivity(intent);
                }
                else
                    signInAlert();
                break;

            case R.id.changepw:
                if(userType!=null && userType.equals("G") && userType.equals("F"))
                    signInAlert();
                else {

                    intent = new Intent(getActivity(), ChangepwdActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.lang:
                intent = new Intent(getActivity(), LanguageActivity.class);
                startActivity(intent);
                break;

            case R.id.sharetheapp:
                ShareFound();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), appicon);
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

                File file = new File(extStorageDirectory, "logog");
                try {
                    FileOutputStream outStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            case R.id.aboutus:
                intent = new Intent(getActivity(), PrivacyActivity.class);
                intent.putExtra("txttitle", getString(R.string.About_Us));
                intent.putExtra("prodterm", "http://www.mojo-domo.com/about");
                startActivity(intent);
                break;

            case R.id.feedback:


                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@mojo-domo.com"});
                emailIntent.putExtra(Intent.EXTRA_CC, "");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Feed_Back));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.termsofuse:
                intent = new Intent(getActivity(), PrivacyActivity.class);
                intent.putExtra("txttitle", getString(R.string.Term));
                intent.putExtra("prodterm", "http://www.mojo-domo.com/terms");
                startActivity(intent);
                break;
            case R.id.privacypolicy:
                intent = new Intent(getActivity(), PrivacyActivity.class);
                intent.putExtra("txttitle", getString(R.string.Privacy));
                intent.putExtra("prodterm", "http://www.mojo-domo.com/privacy");
                startActivity(intent);
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        try {
            userType=settingModule.getSettings("Type");
            Username=settingModule.getSettings("User");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(userType!=null && userType.equals("G"))
            loginbtn.setText(getString(R.string.Sign_In));
        else
            loginbtn.setText(getString(R.string.Logout));

        TextView usertype = (TextView)view.findViewById(R.id.txtusertype);
        TextView txtusername = (TextView)view.findViewById(R.id.txtusername);


        usertype.setText(userType);
        txtusername.setText(Username);

        LinearLayout profileClick = (LinearLayout)view.findViewById(R.id.editprofile);
        profileClick.setOnClickListener(this);
        LinearLayout changepwClick = (LinearLayout)view.findViewById(R.id.changepw);
        changepwClick.setOnClickListener(this);
        LinearLayout langClick = (LinearLayout)view.findViewById(R.id.lang);
        langClick.setOnClickListener(this);
        LinearLayout sharetheappClick = (LinearLayout) view.findViewById(R.id.sharetheapp);
        sharetheappClick.setOnClickListener(this);
        LinearLayout aboutusClick = (LinearLayout)view.findViewById(R.id.aboutus);
        aboutusClick.setOnClickListener(this);
        LinearLayout feedbackClick = (LinearLayout) view.findViewById(R.id.feedback);
        feedbackClick.setOnClickListener(this);
        LinearLayout termsofuseClick = (LinearLayout) view.findViewById(R.id.termsofuse);
        termsofuseClick.setOnClickListener(this);
        LinearLayout privacypolicyClick = (LinearLayout)view.findViewById(R.id.privacypolicy);
        privacypolicyClick.setOnClickListener(this);
    }



    public void signOutAlert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder1.setMessage(getString(R.string.Logout_msg));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

                String registrationId = settings.getString("tokendevice", "");
//                if (!registrationId.equals("")) {
//                    CustomerRes res = userModule.logout(userres.getCustomer_id(), registrationId);
//                }

                CouponDB dbHelper = new CouponDB(getActivity().getApplicationContext());
                dbHelper.clearTables();

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("staff_id", 0);
                editor.putString("user_name", "");
                editor.putString("regId", "");
                editor.putString("profill", "p");
                editor.apply();


                LoginManager.getInstance().logOut();

                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder1.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder1.show();
    }


    public void signInAlert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder1.setMessage(getString(R.string.LogIn_msg));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                CouponDB dbHelper = new CouponDB(getActivity().getApplicationContext());
                dbHelper.clearTables();

                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder1.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder1.show();
    }


   public void ShareFound() {
        File file = null;

       final String mystring = "This is an awesome app for MZCoupon.\n\n Download now";
       BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
               .setCanonicalIdentifier("coupon://post?staffid=" + staffid)
               .setTitle("MZCoupon")
               .setContentDescription(mystring)
               .setContentImageUrl("https://s3-ap-southeast-1.amazonaws.com/stgpromzgetso/logo/thumnail.png").setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

       LinkProperties linkProperties = new LinkProperties().setFeature("sharing");
       branchUniversalObject.generateShortUrl(getActivity().getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
           @Override
           public void onLinkCreate(String url, BranchError error) {
               if (error == null) {
                   Log.i("MyApp", "got my Branch link to share: " + url);
                   StringBuffer shareBody = new StringBuffer();
                   shareBody.append(mystring);
                   shareBody.append("\n\n" + url);
                   Intent shareIntent = new Intent();
                   shareIntent.setAction(Intent.ACTION_SEND);
                   shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
                   shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                   shareIntent.setType("text/plain");
                   startActivity(Intent.createChooser(shareIntent, "Share via"));
               }
           }
       });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Setting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mezzofy.mzcoupon/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Setting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mezzofy.mzcoupon/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
