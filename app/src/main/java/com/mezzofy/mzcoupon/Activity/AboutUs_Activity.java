package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;


public class AboutUs_Activity extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.how_redeem);

            TextView wuctxt = (TextView) findViewById(R.id.textView2);
            TextView textView = (TextView) findViewById(R.id.TextView01);

            wuctxt.setMovementMethod(new ScrollingMovementMethod());

            textView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                Intent intent = new Intent(context, TabviewActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra("tabName", "");
//                intent.putExtra("currTab", 6);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
                    AboutUs_Activity.this.finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        // Write your code here

        super.onBackPressed();
        AboutUs_Activity.this.finish();

//        Intent it=new Intent(getApplicationContext(),SettingActivity.class);
//        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(it);


//		        Intent intent = new Intent(context, TabviewActivity.class);
//		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		        intent.putExtra("tabName", "");
//		        intent.putExtra("currTab", 4);
//			    startActivity(intent);
//			    overridePendingTransition(0, 0);
       // SettingActivity.this.finish();
    }
}