package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignGroupmEntity;

import java.util.ArrayList;

/**
 * Created by udhayinforios on 25/10/16.
 */

    public class SpecialAdapter extends BaseAdapter {
    Context MyContext;
    ArrayList<CampaignGroupmEntity> albumList;
    int pos = 0;
    LayoutInflater li;

    public SpecialAdapter(Context _MyContext, ArrayList<CampaignGroupmEntity> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        li = (LayoutInflater) _MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View MyView;
        pos = position;
        if (convertView == null) {

            MyView = li.inflate(R.layout.speciallist, null);
        } else {
            MyView = convertView;
        }
        ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);

        if (albumList.get(position).getCampaigngroup().getCampgrpImageurl()!= null) {
            try {

                Glide.with(MyContext)
                        .load(albumList.get(position).getCampaigngroup().getCampgrpImageurl())
                        .error(R.drawable.no_image_icon)
                        .into(iv);
                Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

//                imgLoader.DisplayImage(albumList.get(position).getSpecialImage(), loader, iv);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
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



