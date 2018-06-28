package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Activity.CampaignMainList_Activity;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;

import java.util.ArrayList;

public class ProductBaseAdapter extends BaseAdapter {
    private Context MyContext;
    private ArrayList<CampaignmEntity> albumList;
    private LayoutInflater li;
    private SharedPreferences settings;
    private int companyId;

    private Campaign_Module campaignModule;
    String flag;

    public ProductBaseAdapter(Context _MyContext, ArrayList<CampaignmEntity> _albumList, String flag) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        campaignModule=new Campaign_Module(_MyContext);
        li = (LayoutInflater) _MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        settings = PreferenceManager.getDefaultSharedPreferences(_MyContext);
        this.flag = flag;

    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View MyView;

        Log.d("postion",String.valueOf(position));
        Log.d("Count",String.valueOf(albumList.size()));

        if (convertView == null) {
            MyView = li.inflate(R.layout.specialdetailprod, null);
        } else {
            MyView = convertView;
        }

        CampaignEntity campaignData=albumList.get(position).getCampaign();


        ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
        TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
        TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
        TextView selltext = (TextView) MyView.findViewById(R.id.textView3);
        TextView gift = (TextView) MyView.findViewById(R.id.textView28);
        ImageView imageView = (ImageView) MyView.findViewById(R.id.imageView18);

        TextView note1 = (TextView) MyView.findViewById(R.id.TextView01);
        TextView note2 = (TextView) MyView.findViewById(R.id.TextView02);
        TextView note3 = (TextView) MyView.findViewById(R.id.note3);
        TextView distance = (TextView) MyView.findViewById(R.id.textView6);

        final ImageView favImage = (ImageView) MyView.findViewById(R.id.imageView2);
        favImage.setImageResource(R.drawable.fav);

        if (flag.equals("H")) {
            imageView.setVisibility(View.VISIBLE);
            gift.setVisibility(View.VISIBLE);
            gift.setText(String.valueOf(position + 1));

        } else {
            imageView.setVisibility(View.GONE);
            gift.setVisibility(View.GONE);

        }


        ImageView catgryimg = (ImageView) MyView.findViewById(R.id.imageView8);

        if (campaignData.getCampaignimages()!= null && campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage() != null) {
            try {
                Glide.with(MyContext)
                        .load(campaignData.getCampaignimages().get(0).getCampaignimage().getCampaignImage())
                        .error(R.drawable.appicon)
                        .into(iv);Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
        }

//        if (campaignData.getCategoryId() != null) {
//            CategoryRes categoryRes = categoryDao.getCategoryRes(albumList.get(position).getCategoryId());
//
//            if (categoryRes != null)
//                if (categoryRes.getCategoryImage() != null && !categoryRes.getCategoryImage().equals("")) {
//                    try {
//                        Glide.with(MyContext)
//                                .load(categoryRes.getCategoryImage())
//                                .error(R.drawable.appicon_large)
//                                .into(catgryimg);
//
////                        imgLoader.DisplayImage(categoryRes.getCategoryImage(), loader, catgryimg);
//
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                } else {
//                    catgryimg.setImageResource(R.drawable.point_of_interest);
//                }
//       }


            if(campaignData.getExpiryname()!=null && campaignData.getExpiryname().equals("D")){
                distance.setText(campaignData.getDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Days));
            }else{
                distance.setText(campaignData.getDistance()+" "+MyContext.getResources().getString(R.string.Due)+" "+campaignData.getExpirydue()+" "+MyContext.getResources().getString(R.string.Hours));
        }

        TextView left = (TextView) MyView.findViewById(R.id.textView8);

        if(campaignData.getDailyLimit()!=null && campaignData.getDailyLimit().equals("A")) {
            if (campaignData.getExpiryname() != null && campaignData.getExpiryname().equals("D")) {

                distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Days));
            } else {
                distance.setText(MyContext.getResources().getString(R.string.Due) + " " + campaignData.getExpirydue() + " " + MyContext.getResources().getString(R.string.Hours));
            }
        }


        if(campaignData.getIssuedcoupon()!=null)
            if(campaignData.getRedeemcoupon()!=null && campaignData.getDailyLimit()!=null && campaignData.getDailyLimit().equals("A"))
                left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getIssuedcoupon() + " | " + MyContext.getResources().getString(R.string.left) + " " + (campaignData.getTotalRedeem()-campaignData.getIssuedcoupon()));
            else
                left.setText(MyContext.getResources().getString(R.string.sold) + " " + campaignData.getIssuedcoupon() );




        bustext.setText(campaignData.getCampaignName());
        bustext.setMaxLines(2);
        bustext.setEllipsize(TextUtils.TruncateAt.END);


        if(campaignData.getBrand()!=null && campaignData.getBrand().equals("F"))
        {
            pricetext.setText("Free MZCoupon");
            selltext.setVisibility(View.INVISIBLE);
        }
        else {

            if (settings.getString("decimal", "N").equals("Y")) {
                pricetext.setText(settings.getString("currency", "IDR") + " " + String.format("%,.0f", campaignData.getSellingPrice()));
                if (campaignData.getOrginalPrice() > 0) {
                    selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                } else {

                    selltext.setVisibility(View.INVISIBLE);

                }

                if (String.format("%,.0f", campaignData.getOrginalPrice()).equals("0.0")) {
                    selltext.setText("");
                }
            } else {
                pricetext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getSellingPrice()));
                selltext.setText(settings.getString("currency", "IDR") + String.format("%,.0f", campaignData.getOrginalPrice()));
                if (String.format("%,.0f", campaignData.getOrginalPrice()).equals("0.0")) {
                    selltext.setText("");
                }
            }
        }

        if(campaignData.getFavourite()!=null && campaignData.getFavourite().equals("Y"))
            favImage.setImageResource(R.drawable.fav_white);
        else
            favImage.setImageResource(R.drawable.fav);




        selltext.setPaintFlags(selltext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (campaignData.getCampaignNote1() != null) {
            note1.setText(campaignData.getCampaignNote1());
        } else {
            note1.setVisibility(View.GONE);
        }
        if (campaignData.getCampaignNote2() != null) {
            note2.setText(campaignData.getCampaignNote2());
        } else {
            note2.setVisibility(View.GONE);
        }
        if (campaignData.getCampaignNote3() != null) {
            note3.setText(campaignData.getCampaignNote3());
        } else {
            note3.setVisibility(View.GONE);
        }

        if (campaignData.getFavourite() != null)
            if (campaignData.getFavourite().equals("Y")) {
                //toggle.setChecked(true);
                favImage.setImageResource(R.drawable.fav_white);
            } else {
                favImage.setImageResource(R.drawable.fav);
            }


        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumList.get(position).getCampaign().getFavourite()!=null && albumList.get(position).getCampaign().getFavourite().equals("N")) {
                    favImage.setImageResource(R.drawable.fav_white);
                    try {
                        campaignModule.updateCampaignFavourite(albumList.get(position).getCampaign().getCampaignId(), "Y");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    albumList.get(position).getCampaign().setFavourite("Y");

                } else {
                    favImage.setImageResource(R.drawable.fav);
                    try {
                        campaignModule.updateCampaignFavourite(albumList.get(position).getCampaign().getCampaignId(), "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    albumList.get(position).getCampaign().setFavourite("N");
                }


                campaignModule=new Campaign_Module(MyContext);
                try {
                    int count=campaignModule.getFavouriteCount();
                    CampaignMainList_Activity.setfavCount(count);
                } catch (Exception e) {
                    e.printStackTrace();
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
