package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.SitemEnity;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends Activity {

    private Merchantsite_Module merchantsiteModule;

    private List<SitemEnity> locArrayList;
    private ArrayList<String> filterlocList = new ArrayList<String>();
    private ArrayList<String> filtersiteList = new ArrayList<String>();
    ListView listView;

    ImageView apply;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private SearchView.OnQueryTextListener searchList;
    private boolean isScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.category);

            TextView txttitle = (TextView) findViewById(R.id.txtTitle);
            txttitle.setText(getString(R.string.all_location));

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = settings.edit();

            try {
                filterlocList = (ArrayList) ObjectSerializer.deserialize(settings.getString("filterlocList", ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            merchantsiteModule = new Merchantsite_Module(getApplicationContext());

            locArrayList = merchantsiteModule.getMerchantsites();

            if(locArrayList==null)
                locArrayList=new ArrayList<SitemEnity>();

            SitemEnity siteModel = new SitemEnity();
            SiteEntity siteData=new SiteEntity();
            siteData.setSiteId("");
            siteData.setLocationId("");
            siteData.setSiteName(getString(R.string.all_location));
            siteData.setSiteStatus("A");
            siteModel.setSite(siteData);
            locArrayList.add(0, siteModel);

            listView = (ListView) findViewById(R.id.listView1);
            listView.setSelector(R.drawable.listselector);
            listView.setAdapter(new NoteAdapter(getApplicationContext(), locArrayList));

            apply = (ImageView) findViewById(R.id.filter);
            apply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        editor.putString("filterlocList", ObjectSerializer.serialize(filtersiteList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    LocationActivity.this.finish();
                }
            });


            final SearchView mySearchView = (SearchView) findViewById(R.id.searchly);
            searchList = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub
                    Log.d("input", query);

//                    isScroll = false;
//                    locArrayList.clear();
//                    locArrayList.addAll(locationDao.getLocationListbysearch(query));
//                    listView.setAdapter(new NoteAdapter(getApplicationContext(), locArrayList));

                    mySearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // TODO Auto-generated method stub
                    Log.d("inputkey", newText);

//                    isScroll = false;
//                    locArrayList.clear();
//                    locArrayList.addAll(locationDao.getLocationListbysearch(newText));
//                    listView.setAdapter(new NoteAdapter(getApplicationContext(), locArrayList));

                    return false;
                }
            };

            mySearchView.setOnQueryTextListener(searchList);
            mySearchView.setSubmitButtonEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        LocationActivity.this.finish();
    }

    public class NoteAdapter extends BaseAdapter {

        Context MyContext;
        List<SitemEnity> albumList;
        int pos = 0;
        // ImageLoader class instance

        public NoteAdapter(Context _MyContext, List<SitemEnity> _albumList) {
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
                MyView = li.inflate(R.layout.listtextview, null);
            } else {
                MyView = convertView;
            }
//            ImageView imageView = (ImageView) MyView.findViewById(R.id.imageView9);
            TextView bustext = (TextView) MyView.findViewById(R.id.list_content);
//            TextView count = (TextView) MyView.findViewById(R.id.textView13);
            RelativeLayout layoutbg = (RelativeLayout) MyView.findViewById(R.id.bg_item_entry_drawable);

//            count.setVisibility(View.GONE);
//            imageView.setVisibility(View.GONE);

            bustext.setText(albumList.get(position).getSite().getSiteName());


            final ImageView chkmk = (ImageView) MyView.findViewById(R.id.imageView1);

            if(filterlocList.size()==0)
            {
                ImageView otherchkmk = (ImageView) parent.getChildAt(0).findViewById(R.id.imageView1);
                otherchkmk.setVisibility(View.VISIBLE);
            }

            if (filterlocList.size()>0) {
                if (filterlocList.contains(locArrayList.get(position).getSite().getSiteId())) {
                    chkmk.setVisibility(View.VISIBLE);
                } else {
                    chkmk.setVisibility(View.INVISIBLE);
                }
            }

            layoutbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (chkmk.getVisibility() != View.VISIBLE) {
                        if(position==0){
                            for (int j = 0; j < parent.getChildCount(); j++) {
                                ImageView otherchkmk = (ImageView) parent.getChildAt(j).findViewById(R.id.imageView1);
                                otherchkmk.setVisibility(View.INVISIBLE);
                            }
                            chkmk.setVisibility(View.VISIBLE);

                            for(int j=0;j<filtersiteList.size();j++)
                                    filtersiteList.remove(j);

                        } else {
                            ImageView Allchkmk = (ImageView) parent.getChildAt(0).findViewById(R.id.imageView1);
                            Allchkmk.setVisibility(View.INVISIBLE);
                            chkmk.setVisibility(View.VISIBLE);
                            filtersiteList.add(locArrayList.get(position).getSite().getSiteId());
                        }

                    } else {
                        if(position==0){
                        } else {
                            chkmk.setVisibility(View.INVISIBLE);
                            for(int j=0;j<filtersiteList.size();j++)
                            {
                                if(locArrayList.get(position).getSite().getSiteId().equals(filtersiteList.get(j)))
                                    filtersiteList.remove(j);

                            }
                            if(filtersiteList.size()==0)
                            {
                                ImageView Allchkmk = (ImageView) parent.getChildAt(0).findViewById(R.id.imageView1);
                                Allchkmk.setVisibility(View.VISIBLE);
                            }

                        }

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


}