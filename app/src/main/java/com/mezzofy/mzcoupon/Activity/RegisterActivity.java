package com.mezzofy.mzcoupon.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.MzCountry;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.DetectConnection;
import com.mezzofy.mzcoupon.apputills.GCM_Register;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.Entity.MzCountryList;
import com.mezzofy.mzcoupon.module.Customer_Module;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aruna on 08/06/2017.
 */
public class RegisterActivity extends GCM_Register {

    Context context = this;
    String regId;

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    private static final int REQUEST_APP_SETTINGS = 168;
    private AutoCompleteTextView spinnercountry;

    ProgressDialog progressDialog;
    String countryCode = null;
    String countryName = null;

    private CustomermEntity customerModel=new CustomermEntity();
    private Customer_Module customer_module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.register_page);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            final EditText userfield = (EditText) findViewById(R.id.userfield);
            final EditText passwordfield = (EditText) findViewById(R.id.name);
            final EditText confirmfield = (EditText) findViewById(R.id.confirmfield);
            final EditText emailfield = (EditText) findViewById(R.id.emailfield);
            final EditText mobilefield = (EditText) findViewById(R.id.mobilefield);
            final TextView termtxt = (TextView) findViewById(R.id.textView);
            spinnercountry = (AutoCompleteTextView) findViewById(R.id.countryfield);

            final Button regbtn = (Button) findViewById(R.id.Regisbutton);
            customer_module=new Customer_Module(getApplicationContext());

            if (DetectConnection.checkInternetConnection(getApplicationContext())) {

                AsyncCallWS asyncCallWS = new AsyncCallWS();
                asyncCallWS.execute();
            } else
                Toast.makeText(getApplicationContext(),getString(R.string.No_Internet), Toast.LENGTH_SHORT)
                        .show();


            regbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (DetectConnection.checkInternetConnection(getApplicationContext())) {

                        String merchantname = userfield.getText().toString();


                        if (!emailfield.getText().toString().equals("") && !passwordfield.getText().toString().equals("") && !confirmfield.getText().toString().equals("")  && !userfield.getText().toString().equals("")) {

                            if (emailValidator(emailfield.getText().toString())) {
                                if (passwordfield.getText().toString().length() > 7) {
                                    if (passwordfield.getText().toString().equals(confirmfield.getText().toString())) {
                                        if (countryCode != null) {

                                            if (TextUtils.isEmpty(regId)) {

                                                try {
                                                    regId = registerGCM(emailfield.getText().toString().trim(), passwordfield.getText().toString(), "S", null,countryCode);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (!regId.equals("")) {
                                                storeRegistrationId(getApplicationContext(), regId);
                                                CustomerEntity customerRes = new CustomerEntity();
                                                customerRes.setCustomerFirstName(userfield.getText().toString());
                                                customerRes.setCustomerLastName("");
                                                customerRes.setCustomerMobile(mobilefield.getText().toString());
                                                customerRes.setCustomerPassword(passwordfield.getText().toString());
                                                customerRes.setCustomerEmail(emailfield.getText().toString().trim());
                                                customerRes.setUserType("C");


                                                CustomermEntity customerModel=new CustomermEntity();
                                                customerModel.setCustomer(customerRes);

                                                CustomermEntity ReguserModel = null;
                                                try {
                                                    ReguserModel = customer_module.signup(customerModel);
                                                } catch (APIServerException e) {
                                                    Toast.makeText(getApplicationContext(),
                                                           e.getMessage(), Toast.LENGTH_SHORT)
                                                            .show();
                                                    return;
                                                }

                                                if (ReguserModel != null && ReguserModel.getCustomer()!=null) {


                                                    try {
                                                        customer_module.addStaff(ReguserModel.getCustomer());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                    SharedPreferences.Editor editor = settings.edit();
                                                    editor.putString("staff_id", ReguserModel.getCustomer().getCustomerId());
                                                    editor.putString("user_name", ReguserModel.getCustomer().getCustomerFirstName());
                                                    editor.putString("company_Id", ReguserModel.getCustomer().getMerchantId());
                                                    editor.putString("mobile", ReguserModel.getCustomer().getCustomerMobile());
                                                    editor.putString("profill", ReguserModel.getCustomer().getCustomerStatus());
                                                    editor.putString("agent", ReguserModel.getCustomer().getUserType());
                                                    editor.putString("tokendevice", regId);
                                                    editor.commit();


                                                    Intent intent = new Intent(context, ProgressActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    overridePendingTransition(0, 0);
                                                    RegisterActivity.this.finish();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        Applaunch.ErrorMessage, Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.Select_Country), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.ensure_password_valid), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.Password_must_8_20), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.Please_check_email), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.do_not_leave_emty), Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            });

            // Setting EditorActionListener for the EditText
            passwordfield.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView arg0, int actionId,
                                              KeyEvent arg2) {
                    // TODO Auto-generated method stub

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        regbtn.performClick();
                    }

                    return false;
                }
            });


            termtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PrivacyActivity.class);
                    intent.putExtra("txttitle", getString(R.string.Term));
                    intent.putExtra("prodterm", "http://www.mojo-domo.com/terms");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public boolean validate(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*[a-z]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// Permission Granted


                } else { // Permission Denied
                    Toast.makeText(getApplicationContext(), "Please grant Phone & Storage permissions", Toast.LENGTH_LONG).show();
                    goToSettings();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        RegisterActivity.this.finish();
    }


    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        List<String> Spinlist = new ArrayList<>();
        ArrayList<MzCountryList> countryLists;

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            Customer_Module userModule = new Customer_Module(getApplicationContext());
            countryLists = userModule.getCountrylist();

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");

            for (MzCountryList mzctylist : countryLists) {
                MzCountry mzCountry = mzctylist.getCountry();
                if (mzCountry != null && mzCountry.getCountryName() != null)
                    Spinlist.add(mzCountry.getCountryCode() + " - " + mzCountry.getCountryName());
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }


            ArrayAdapter<String> spnrAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, Spinlist) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return setCentered(super.getView(position, convertView, parent));
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    return setCentered(super.getDropDownView(position, convertView, parent));
                }

                private View setCentered(View view) {
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setGravity(Gravity.CENTER);
                    return view;
                }
            };

            spnrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnercountry.setAdapter(spnrAdapter);
            spinnercountry.setThreshold(1);

            spinnercountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (Spinlist != null && Spinlist.size() > 0) {
                        int selectedpos = Spinlist.indexOf((((TextView) view).getText()).toString());
                        if (selectedpos != 0) {
                            MzCountry country = countryLists.get(selectedpos).getCountry();
                            countryCode = country.getCountryCode();
                            countryName = country.getCountryName();
                        }
                    }
                }

            });

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }
}