package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

/**
 * Created by LENOVO on 26/06/2015.
 */
public class Point_interestActivity extends Activity {

    ListView list;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bali_interest);

//        String[] CONTENT = getApplicationContext().getResources().getStringArray(R.array.PointInterest);

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Useful_ContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                Point_interestActivity.this.finish();
            }
        });

        list=(ListView) findViewById(R.id.listView1);
        list.setSelector(R.drawable.listselector);
//        list.setAdapter(new ImageBaseAdapter(this,CONTENT));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;
        String[] albumList;
        int pos = 0;


        public ImageBaseAdapter(Context _MyContext,String[] _albumList) {
            albumList=null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList=_albumList;
        }

        @Override
        public int getCount() {
            return albumList.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.point_of_interest, null);
            } else {
                MyView = convertView;
            }
            ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);

            switch (position){
//                case 0: iv.setImageResource(R.drawable.bali);
//                   break;
//                case 1: iv.setImageResource(R.drawable.ksl);
//                    break;
//                case 2: iv.setImageResource(R.drawable.ukg);
//                    break;
//                case 3: iv.setImageResource(R.drawable.juu);
//                    break;
//                case 4: iv.setImageResource(R.drawable.ds);
//                    break;
//                case 5: iv.setImageResource(R.drawable.ndtb);
//                    break;
//                case 6: iv.setImageResource(R.drawable.kp);
//                    break;
//                case 7: iv.setImageResource(R.drawable.li);
//                    break;
//                case 8: iv.setImageResource(R.drawable.lombok);
//                    break;

            }


            TextView bustext=(TextView) MyView.findViewById(R.id.textView1);
            bustext.setText(albumList[position]);
//            bustext.setMaxLines(9);
            bustext.setEllipsize(TextUtils.TruncateAt.END);
            return MyView;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return albumList[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
        Point_interestActivity.this.finish();
    }
}
