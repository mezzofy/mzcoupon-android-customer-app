package com.mezzofy.mzcoupon.apputills;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezzofy.mzcoupon.R;

import java.util.ArrayList;

/**
 * Created by LENOVO on 23/06/2015.
 */
public class CardPayAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private LayoutInflater vi;
    String[] CONTENT;
    String user_name;
    Context context;
    public CardPayAdapter(Context context,ArrayList<Item> items) {
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
                v = vi.inflate(R.layout.list_cards_pay_detail, null);
                final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);

                ImageView imgicon =(ImageView)v.findViewById(R.id.item_entry_drawable);
                ImageView imgicon2 =(ImageView)v.findViewById(R.id.item_entry_drawable2);
                imgicon2.setVisibility(View.GONE);

                ImageView arrowicon =(ImageView)v.findViewById(R.id.list_item_entry_drawable);


                if (title != null)
                    title.setText(ei.title);
                if(ei.title.equals(CONTENT[0]))
                {
                    imgicon.setImageResource(R.drawable.master);
                    imgicon2.setVisibility(View.VISIBLE);
                    imgicon2.setImageResource(R.drawable.visa);
                }else if(ei.title.equals(CONTENT[1]))
                {
                    imgicon.setImageResource(R.drawable.enets_logo);

                }else if(ei.title.equals(CONTENT[2]))
                {
                    imgicon.setImageResource(R.drawable.unionpay);
                }
            }
        }
        return v;
    }

}
