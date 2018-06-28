package com.mezzofy.mzcoupon.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;

public class CategoryActivity extends Activity {


    private List<CampGrpEntity> cateArrayList;
    private ArrayList<String> filterCateList = new ArrayList<String>();
    private ArrayList<String>filterGroupList=new ArrayList<String>();
    private Campaign_Module campaignModule;
    ListView listView;

    ImageView apply;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private boolean isScroll = true;

    int total_catcount = 0;
    private SearchView.OnQueryTextListener searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.category);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = settings.edit();

            try {
                filterCateList = (ArrayList) ObjectSerializer.deserialize(settings.getString("filterCategry", ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            campaignModule = new Campaign_Module(getApplicationContext());

            cateArrayList =campaignModule.getCampaginGrpDtllist();

            if(cateArrayList==null)
                cateArrayList=new ArrayList<CampGrpEntity>();


            CampGrpEntity categoryResw = new CampGrpEntity();
            categoryResw.setCampgrpId("");
            categoryResw.setCampgrpName(getString(R.string.all_category));
            cateArrayList.add(0, categoryResw);

            listView = (ListView) findViewById(R.id.listView1);

            listView.setSelector(R.drawable.listselector);
            listView.setAdapter(new NoteAdapter(getApplicationContext(), cateArrayList));

            apply = (ImageView) findViewById(R.id.filter);
            apply.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {
                        editor.putString("filterCategry", ObjectSerializer.serialize(filterGroupList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    CategoryActivity.this.finish();
                }
            });


            final SearchView mySearchView = (SearchView) findViewById(R.id.searchly);
            searchList = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub
                    Log.d("input", query);

                    isScroll = false;
                    cateArrayList.clear();
//                    cateArrayList.addAll(categoryDao.getCatergoryListforsearch(query));
                    listView.setAdapter(new NoteAdapter(getApplicationContext(), cateArrayList));

                    mySearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // TODO Auto-generated method stub
                    Log.d("inputkey", newText);

                    isScroll = false;
                    cateArrayList.clear();
//                    cateArrayList.addAll(categoryDao.getCatergoryListforsearch(newText));
                    listView.setAdapter(new NoteAdapter(getApplicationContext(), cateArrayList));

                    return false;
                }
            };

            mySearchView.setOnQueryTextListener(searchList);
            mySearchView.setSubmitButtonEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class NoteAdapter extends BaseAdapter {

        Context MyContext;
        List<CampGrpEntity> albumList;
        int pos = 0;
        // ImageLoader class instance
//		ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        public NoteAdapter(Context _MyContext, List<CampGrpEntity> _albumList) {
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
//			ImageView imageView = (ImageView) MyView.findViewById(R.id.imageView9);
            TextView bustext = (TextView) MyView.findViewById(R.id.list_content);
//            TextView count = (TextView) MyView.findViewById(R.id.textView13);
            RelativeLayout layoutbg = (RelativeLayout) MyView.findViewById(R.id.bg_item_entry_drawable);

            bustext.setText(albumList.get(position).getCampgrpName());

//            if (position == 0)
//                count.setText(String.valueOf(total_catcount));
//            else
//                count.setText(String.valueOf(cateArrayList.get(position).getProductCount()));

            if (cateArrayList.get(position).getCampgrpId() != null && !cateArrayList.get(position).getCampgrpId().equals("")) {
                try {
                    // Loader image - will be shown before loading image
//					int loader = R.drawable.no_image_icon;

//					imgLoader.DisplayImage(cateArrayList.get(position).getCategoryImage(), loader, imageView);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
//				imageView.setImageResource(R.drawable.logo);
            }

            final ImageView chkmk = (ImageView) MyView.findViewById(R.id.imageView1);

            if(filterCateList.size()==0)
            {
                ImageView otherchkmk = (ImageView) parent.getChildAt(0).findViewById(R.id.imageView1);
                otherchkmk.setVisibility(View.VISIBLE);
            }

            if (filterCateList != null) {
                if (filterCateList.contains(cateArrayList.get(position).getCampgrpId())) {
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

                            for(int j=0;j<filterGroupList.size();j++)
                                filterGroupList.remove(j);

                        } else {
                            ImageView Allchkmk = (ImageView) parent.getChildAt(0).findViewById(R.id.imageView1);
                            Allchkmk.setVisibility(View.INVISIBLE);
                            chkmk.setVisibility(View.VISIBLE);
                            filterGroupList.add(cateArrayList.get(position).getCampgrpId());
                        }

                    } else {
                        if(position==0){
                        } else {
                            chkmk.setVisibility(View.INVISIBLE);
                            for(int j=0;j<filterGroupList.size();j++)
                            {
                                if(cateArrayList.get(position).getCampgrpId().equals(filterGroupList.get(j)))
                                    filterGroupList.remove(j);

                            }
                            if(filterGroupList.size()==0)
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        CategoryActivity.this.finish();
    }

}
