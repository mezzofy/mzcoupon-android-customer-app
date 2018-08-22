package com.mezzofy.mzcoupon.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mezzofy.com.libmzcoupon.utills.APIServerException;

@SuppressLint("NewApi")
public class ForgetActivity extends Activity {

    RelativeLayout layout;
    Button loginbtn;
    final Context context = this;
    EditText emailtxt;
    private Customer_Module customerModule;
    CommonUtils common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.forgotpassword);

            common = new CommonUtils();
            customerModule = new Customer_Module(ForgetActivity.this);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            layout = (RelativeLayout) this.findViewById(R.id.frgetpage);
            layout.setOnTouchListener(new OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent ev) {
                    hideKeyboard(view);
                    return false;
                }

            });

            emailtxt = (EditText) findViewById(R.id.editText1);
            final TextView resetpassword = (TextView) findViewById(R.id.resetpassword);
            resetpassword.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!emailtxt.getText().toString().equals("") && emailValidator(emailtxt.getText().toString())) {

                        CustomermEntity customerModel = new CustomermEntity();
                        CustomerEntity customerData=new CustomerEntity();
                        customerData.setCustomerEmail(emailtxt.getText().toString());
                        customerModel.setCustomer(customerData);

                        try {
                            customerModel = customerModule.getforgetpwd(customerModel);
                        } catch (APIServerException e) {
                            common.Snackbar(v, e.getMessage());
                            return;
                        }
                        if (customerModel != null && customerModel.getCustomer().getCustomerEmail() != null) {
                                try {
                                   customerModule.UpdateCustomer(customerModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(context, MailsentActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                ForgetActivity.this.finish();
                                common.Snackbar(v, getString(R.string.Password_has_been_sent));
                            } else {
                                common.Snackbar(v,Applaunch.ErrorMessage );
                           }

                    } else {
                        common.Snackbar(v, getString(R.string.Please_enter_registered));
                    }
                }
            });

            emailtxt.setOnEditorActionListener(new OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView arg0, int actionId,
                                              KeyEvent arg2) {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        resetpassword.performClick();
                    }

                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        ForgetActivity.this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
