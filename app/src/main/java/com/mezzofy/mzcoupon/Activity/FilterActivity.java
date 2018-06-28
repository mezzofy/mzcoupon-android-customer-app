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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.SitemEnity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 24/08/2015.
 */
public class FilterActivity extends Activity {

    private Merchantsite_Module merchantsiteModule;
    private Campaign_Module campaignModule;

    String filter;
    ImageView apply;
    TextView clrbtn;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    ListView listView;
    private List<CampGrpEntity> cateArrayList;
    ArrayList<String> filterArray = new ArrayList<String>();
    private List<SitemEnity> locArrayList;
    private ArrayList<String> filterCateList = new ArrayList<String>();
    private ArrayList<String> filterLocList = new ArrayList<String>();
    ArrayList<String> catgryArray = new ArrayList<String>();
    ArrayList<String> locArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.filter_page);

            merchantsiteModule = new Merchantsite_Module(getApplicationContext());
            campaignModule = new Campaign_Module(getApplicationContext());


            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = settings.edit();

            filter = settings.getString("filter", "no");

            filterArray.add(getString(R.string.Categories));
            filterArray.add(getString(R.string.Locations));

            try {
                filterCateList = (ArrayList) ObjectSerializer.deserialize(settings.getString("filterCategry", ObjectSerializer.serialize(new ArrayList())));
                filterLocList = (ArrayList) ObjectSerializer.deserialize(settings.getString("filterlocList", ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (filterCateList.size() == 0) {
                filterCateList.add("");
                try {
                    editor.putString("filterCategry", ObjectSerializer.serialize(filterCateList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor.commit();
            }

            if (filterLocList.size() == 0) {
                filterLocList.add("");
                try {
                    editor.putString("filterlocList", ObjectSerializer.serialize(filterLocList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor.commit();
            }


            cateArrayList = campaignModule.getCampaginGrpDtllist();
            if(cateArrayList==null)
                cateArrayList=new ArrayList<CampGrpEntity>();

            CampGrpEntity categoryResw = new CampGrpEntity();
            categoryResw.setCampgrpId("");
            categoryResw.setCampgrpName(getString(R.string.all_category));
            cateArrayList.add(0, categoryResw);

            for (int i = 0; i < cateArrayList.size(); i++) {
                if (filterCateList.contains(cateArrayList.get(i).getCampgrpId())) {
                    catgryArray.add(cateArrayList.get(i).getCampgrpName());
                }
            }


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

            for (int i = 0; i < locArrayList.size(); i++) {
                if (filterLocList.contains(locArrayList.get(i).getSite().getSiteId())) {
                    locArray.add(locArrayList.get(i).getSite().getSiteName());
                }
            }


            listView = (ListView) findViewById(R.id.listView1);

            listView.setSelector(R.drawable.listselector);
            listView.setAdapter(new NoteAdapter(getApplicationContext(), filterArray));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                    Intent intent;
                    if (position == 0) {
                        intent = new Intent(getApplicationContext(), CategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        FilterActivity.this.finish();
                    } else if (position == 1) {
                        intent = new Intent(getApplicationContext(), LocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        FilterActivity.this.finish();
                    }
                }
            });

            apply = (ImageView) findViewById(R.id.filter);
            apply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        editor.putString("sendfilterCateList", ObjectSerializer.serialize(filterCateList));
                        editor.putString("sendfilterLocList", ObjectSerializer.serialize(filterLocList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

//                    try {
//                        ArrayList<Integer> Filterresult = new ArrayList<Integer>();
//                        Filterresult.removeAll(Filterresult);
//                        editor.putString("Filterresult", ObjectSerializer.serialize(Filterresult));
//                        editor.commit();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("tabName", "");
                    intent.putExtra("currTab", 0);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    FilterActivity.this.finish();
                }
            });

            clrbtn = (TextView) findViewById(R.id.TextViewClr);
            clrbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    filterCateList.removeAll(filterCateList);
                    filterLocList.removeAll(filterLocList);


                    catgryArray.removeAll(catgryArray);
                    locArray.removeAll(locArray);

                    catgryArray.add(getString(R.string.all_category));
                    locArray.add(getString(R.string.all_location));
;

                    if (filterCateList.size() == 0) {
                        filterCateList.add("");
                        try {
                            editor.putString("filterCategry", ObjectSerializer.serialize(filterCateList));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }


                    if (filterLocList.size() == 0) {
                        filterLocList.add("");
                        try {
                            editor.putString("filterlocList", ObjectSerializer.serialize(filterLocList));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                    try {
                        ArrayList<Integer> Filterresult = new ArrayList<Integer>();
                        Filterresult.removeAll(Filterresult);
                        editor.putString("Filterresult", ObjectSerializer.serialize(Filterresult));
                        editor.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        editor.putString("sendfilterCateList", ObjectSerializer.serialize(filterCateList));
                        editor.putString("sendfilterLocList", ObjectSerializer.serialize(filterLocList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    listView.setAdapter(new NoteAdapter(getApplicationContext(), filterArray));
                }
            });

        } catch (Exception e) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;

            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.filter_itempage, null);
            } else {
                MyView = convertView;
            }
            ImageView imageView = (ImageView) MyView.findViewById(R.id.imageView9);
            TextView bustext = (TextView) MyView.findViewById(R.id.list_content);
            TextView count = (TextView) MyView.findViewById(R.id.textView13);
            LinearLayout linearLayout = (LinearLayout) MyView.findViewById(R.id.linearlist);
            TextView listViewitem = (TextView) MyView.findViewById(R.id.itemlist);

            bustext.setText(albumList.get(position));

            if (albumList.get(position).equals("Categories")) {
                imageView.setImageResource(R.drawable.categories);
            } else if (albumList.get(position).equals("Locations")) {
                imageView.setImageResource(R.drawable.location_map);
            }


            if (position == 0)
                count.setText(String.valueOf(cateArrayList.size() - 1));
            else if (position == 1)
                count.setText(String.valueOf(locArrayList.size() - 1));


            if (position == 0) {
                if (catgryArray != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String item : catgryArray) {
                        sb.append(item + "\n\n");
                    }
                    listViewitem.setText(sb.toString());
                }
            } else if (position == 1) {
                if (locArray != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String item : locArray) {
                        sb.append(item + "\n\n");
                    }
                    listViewitem.setText(sb.toString());
                }
            }

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
        FilterActivity.this.finish();
    }
}