package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;

import java.util.List;

public class MercouponAdapter extends BaseAdapter {
    Context MyContext;
    List<CampaignEntity> albumList;
    int pos = 0;
    Typeface regular;

    public MercouponAdapter(Context _MyContext, List<CampaignEntity> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        regular = Typeface.createFromAsset(MyContext.getAssets(), "Roboto-Light.ttf");
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
            LayoutInflater li = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyView = li.inflate(R.layout.mycoupon_page, null);
        } else {
            MyView = convertView;
        }
        ImageView iv = (ImageView) MyView.findViewById(R.id.imageview1);
        TextView bustext = (TextView) MyView.findViewById(R.id.textview1);
        TextView pricetext = (TextView) MyView.findViewById(R.id.textview2);
        TextView desc = (TextView) MyView.findViewById(R.id.textview3);
//        TextView redeemed = (TextView) MyView.findViewById(R.id.textview4);
        TextView expired=(TextView) MyView.findViewById(R.id.textview5);

        bustext.setTypeface(regular, Typeface.BOLD);
        pricetext.setTypeface(regular, Typeface.BOLD);
        desc.setTypeface(regular);
        CampaignEntity campaignData=albumList.get(position);

        if (campaignData.getCampaignimages()!=null && campaignData.getCampaignimages().get(0).getCampaignimage() != null && campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage()!=null) {
            Glide.with(MyContext)
                    .load(campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage())
                    .error(R.drawable.appicon_large)
                    .into(iv);Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
        }

        if (campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")) {

            expired.setText(MyContext.getResources().getString(R.string.Due) + " "+campaignData.getExpiryDays()+" "+MyContext.getResources().getString(R.string.Days));

            }else{
            expired.setText(MyContext.getResources().getString(R.string.Due) + " "+campaignData.getExpiryDays()+" "+MyContext.getResources().getString(R.string.Hours));

    }

        bustext.setText(campaignData.getCampaignName());

        pricetext.setText(campaignData.getSize());

        desc.setText(campaignData.getCampaignDesc());
        desc.setMaxLines(3);
        desc.setEllipsize(TextUtils.TruncateAt.END);

//        if(albumList.get(position).getDailyLimit()!=null && albumList.get(position).getDailyLimit().equals("A")) {
//            redeemed.setText(MyContext.getResources().getString(R.string.RedemptionQuota) + " " + albumList.get(position).getTotalRedeem());
//        }else{
//            redeemed.setVisibility(View.GONE);
//        }

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