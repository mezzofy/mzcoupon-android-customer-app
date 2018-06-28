package com.mezzofy.mzcoupon.apputills;

import java.util.ArrayList;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Setting_Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items = new ArrayList<Item>();
    private LayoutInflater vi;
    String[] CONTENT;
    Context context;
    private Setting_Module settingModule;
    private String userType,Username;

    public EntryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CONTENT = context.getResources().getStringArray(R.array.Settings);

        settingModule=new Setting_Module(context);

        try {
            userType=settingModule.getSettings("Type");
            Username=settingModule.getSettings("User");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            final Item i = items.get(position);
            if (i != null) {
                if (i.isSection()) {
                    SectionItem si = (SectionItem) i;
                    v = vi.inflate(R.layout.section_groupname, null);

                    v.setOnClickListener(null);
                    v.setOnLongClickListener(null);
                    v.setLongClickable(false);

                    final TextView sectionView = (TextView) v.findViewById(R.id.list_section_entry_title);
                    sectionView.setText(si.getTitle());
//				sectionView.setTextSize(14);
//				sectionView.setPadding(10, 5, 0, 0);
//				sectionView.setBackgroundColor(Color.parseColor("#DEDEDE"));
//				sectionView.setTextColor(Color.DKGRAY);

                } else {
                    EntryItem ei = (EntryItem) i;
                    v = vi.inflate(R.layout.list_item_entry, null);
                    final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);

                    ImageView imgicon = (ImageView) v.findViewById(R.id.item_entry_drawable);
                    imgicon.setVisibility(View.GONE);
                    TextView countxt = (TextView) v.findViewById(R.id.counttextView);
                    countxt.setVisibility(View.GONE);
                    ImageView arrowicon = (ImageView) v.findViewById(R.id.list_item_entry_drawable);
                    arrowicon.setVisibility(View.GONE);

//                    CompanyDao companyDao = new CompanyDao(context);
//                    CompanyRes companyRes = companyDao.getCompanyname();
//                    if (companyRes != null && companyRes.getPlaceName() == null) {
//                        companyRes.setPlaceName("");
//                    }

                    if (title != null)
                        title.setText(ei.title);
                    if (ei.title.equals(userType)) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.location_map);
                    } else if (ei.title.equals(Username)) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.user);
                    } else if (ei.title.equals(CONTENT[3])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.library);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[4])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.lock);
                        arrowicon.setVisibility(View.VISIBLE);
                    }else if(ei.title.equals(CONTENT[5])){
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.language);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[7])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.upload);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[9])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.library);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[10])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.email);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[11])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.appicon);
//						arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[13])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.info);
                        arrowicon.setVisibility(View.VISIBLE);
                    } else if (ei.title.equals(CONTENT[14])) {
                        imgicon.setVisibility(View.VISIBLE);
                        imgicon.setImageResource(R.drawable.info);
                        arrowicon.setVisibility(View.VISIBLE);
                    }
//					else if(ei.title.equals(CONTENT[14]))
//					{
//						imgicon.setVisibility(View.VISIBLE);
//						imgicon.setImageResource(R.drawable.filter_black);
//						arrowicon.setVisibility(View.VISIBLE);
//					}

      //              else if(ei.title.equals(CONTENT[15]));
//					{
//						imgicon.setVisibility(View.VISIBLE);
//						imgicon.setImageResource(R.drawable.baliappicon);
//					}else if(ei.title.equals(CONTENT[17]))
//					{
//						imgicon.setVisibility(View.VISIBLE);
//						imgicon.setImageResource(R.drawable.info);
//						arrowicon.setVisibility(View.VISIBLE);
//					}else if(ei.title.equals(CONTENT[18]))
//					{
//						imgicon.setVisibility(View.VISIBLE);
//						imgicon.setImageResource(R.drawable.info);
//						arrowicon.setVisibility(View.VISIBLE);
//					}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

}
