package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by udhayinforios on 20/10/15.
 */
public class LanguageActivity extends Activity {
    ArrayList<String> languArray = new ArrayList<String>();
    ListView listView;
    String filter;
    ImageView apply;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.language_choose);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        languArray.add(getString(R.string.lang_simplif));
        languArray.add(getString(R.string.lang_trand));
        languArray.add(getString(R.string.lang_english));

        listView = (ListView) findViewById(R.id.listView1);

        listView.setSelector(R.drawable.listselector);
        listView.setAdapter(new NoteAdapter(getApplicationContext(), languArray));

        apply = (ImageView) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.apply();
                String languageToLoad = settings.getString("language", "zh-rTW");
                Locale locale = null;
                if (languageToLoad.equals("en")) {
                    locale = new Locale(Locale.ENGLISH.getLanguage());
                } else if (languageToLoad.equals("zh-rCN")) {
                    locale = new Locale(Locale.SIMPLIFIED_CHINESE.getLanguage(), Locale.SIMPLIFIED_CHINESE.getCountry());
                } else if (languageToLoad.equals("zh-rTW")) {
                    locale = new Locale(Locale.TRADITIONAL_CHINESE.getLanguage(), Locale.TRADITIONAL_CHINESE.getCountry());
                }
                Locale.setDefault(locale);
                android.content.res.Configuration config = new android.content.res.Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("tabName", "");
                intent.putExtra("currTab", 4);
                startActivity(intent);
                overridePendingTransition(0, 0);
                LanguageActivity.this.finish();
            }
        });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class NoteAdapter extends BaseAdapter {

        Context MyContext;
        ArrayList<String> albumList;
        int pos = 0;
        // ImageLoader class instance

        public NoteAdapter(Context _MyContext, ArrayList<String> _albumList) {
            albumList = null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList = _albumList;
        }


        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.languagetextview, null);
            } else {
                MyView = convertView;
            }
            ImageView imageView = (ImageView) MyView.findViewById(R.id.imageView9);
            TextView bustext=(TextView) MyView.findViewById(R.id.list_content);
            TextView count=(TextView) MyView.findViewById(R.id.textView13);
            RelativeLayout layoutbg= (RelativeLayout)MyView.findViewById(R.id.bg_item_entry_drawable);

            count.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            bustext.setText(albumList.get(position));

            final ImageView chkmk = (ImageView) MyView.findViewById(R.id.imageView1);
            String chlang = settings.getString("language","zh-rTW");
            if(chlang.equals("en") && position ==2) {
                chkmk.setVisibility(View.VISIBLE);
            }else  if(chlang.equals("zh-rCN") && position ==0) {
                chkmk.setVisibility(View.VISIBLE);
            }else  if(chlang.equals("zh-rTW") && position ==1) {
                chkmk.setVisibility(View.VISIBLE);
            }

            layoutbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (chkmk.getVisibility() != View.VISIBLE) {
                        for (int j = 0; j < parent.getChildCount(); j++) {
                            ImageView otherchkmk = (ImageView) parent.getChildAt(j).findViewById(R.id.imageView1);
                            otherchkmk.setVisibility(View.INVISIBLE);
                        }
                        chkmk.setVisibility(View.VISIBLE);
                        if(position==2)
                            editor.putString("language","en");
                        else if(position==0)
                            editor.putString("language","zh-rCN");
                        else if(position==1)
                            editor.putString("language","zh-rTW");
                    }
                }

            });

            return MyView;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return albumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), TabMainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        intent.putExtra("tabName", "");
//        intent.putExtra("currTab", 3);
//        startActivity(intent);
//        overridePendingTransition(0, 0);

        LanguageActivity.this.finish();
    }

}
