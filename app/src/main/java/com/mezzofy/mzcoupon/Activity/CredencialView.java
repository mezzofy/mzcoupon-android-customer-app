package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mezzofy.mzcoupon.R;

/**
 * Created by aruna on 5/4/18.
 */

public class CredencialView extends Activity {
    final Context context = this;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;


    EditText MerchantId,Credencialkey,Credencialsecret;
    TextView Reset;

    String merchantid,key,secret;

    Button savebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.credencialviewlayout);

            this.findViewById(R.id.scrollView1).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    findPostion(event);
                    return true;
                }
            });


            MerchantId = (EditText) findViewById(R.id.merchantId);
            Credencialkey = (EditText) findViewById(R.id.key);
            Credencialsecret = (EditText) findViewById(R.id.Secret);
            Reset = (TextView) findViewById(R.id.reset);


            savebutton=(Button)findViewById(R.id.savebutton);

            Reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("MerchantId", null);
                    editor.putString("Credencialkey", null);
                    editor.putString("Credencialsecret", null);
                    editor.putString("Credencialflag", null);

                    editor.commit();

                    MerchantId.setText("");
                    Credencialkey.setText("");
                    Credencialsecret.setText("");

                    MerchantId.requestFocus();
                }
            });
            savebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(MerchantId.getText().toString()!=null && MerchantId.getText().toString().length()>0) {
                        merchantid = MerchantId.getText().toString();
                        if(Credencialkey.getText().toString()!=null && Credencialkey.getText().toString().length()>0) {
                            key = Credencialkey.getText().toString();
                            if(Credencialsecret.getText().toString()!=null && Credencialsecret.getText().toString().length()>0) {
                                secret = Credencialsecret.getText().toString();

                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();

                                editor.putString("MerchantId", merchantid);
                                editor.putString("Credencialkey", key);
                                editor.putString("Credencialsecret", secret);
                                editor.putString("Credencialflag", "Yes");

                                editor.commit();

                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

                            }else
                                Toast.makeText(context, "Credencialsecret Null", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(context, "Credencialkey Null", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(context, "MerchantId Null", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findPostion(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {

                    }

                    // Right to left swipe action
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                        startActivity(intent);

                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }

        return null;
    }
}
