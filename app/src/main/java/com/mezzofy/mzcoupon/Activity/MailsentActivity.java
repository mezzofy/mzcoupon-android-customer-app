package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mezzofy.mzcoupon.R;

public class MailsentActivity extends Activity {
    Button frgbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mailsent);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        frgbtn = (Button) findViewById(R.id.button1);
        frgbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent browserIntent = new Intent(MailsentActivity.this, SignInActivity.class);
                startActivity(browserIntent);
                overridePendingTransition(0, 0);
                MailsentActivity.this.finish();
            }

        });
    }
}
