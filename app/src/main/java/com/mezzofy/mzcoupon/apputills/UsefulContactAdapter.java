package com.mezzofy.mzcoupon.apputills;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.mezzofy.mzcoupon.R;

import java.util.ArrayList;

/**
 * Created by LENOVO on 26/06/2015.
 */
public class UsefulContactAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private LayoutInflater vi;
    String[] CONTENT;
    String user_name;
    Context context;
    public UsefulContactAdapter(Context context,ArrayList<Item> items) {
        super(context,0, items);
        this.items = items;
        this.context = context;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CONTENT = context.getResources().getStringArray(R.array.Cards_Pay);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        user_name=settings.getString("user_name","");
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.section_groupname, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.list_section_entry_title);
                sectionView.setText(si.getTitle());

            }else{
                EntryItem ei = (EntryItem)i;
                v = vi.inflate(R.layout.useful_contact_page, null);
                final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
                title.setMaxLines(3);
                title.setEllipsize(TextUtils.TruncateAt.END);

                if (title != null) title.setText(ei.title);

            }
        }
        return v;
    }

}